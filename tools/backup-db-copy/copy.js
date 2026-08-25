// 백업 DB의 관제 관련 테이블을 익명화해 로컬 LOCAL_CSMS로 복사한다.
// 연결정보가 없는 지금은 실행 대상이 아님 — .env 채운 뒤 `node copy.js`로 실행.
const fs = require('fs');
const path = require('path');
const mysql = require('mysql2/promise');

// 별도 dotenv 의존성 없이 .env 파일 하나만 읽어 process.env에 채운다.
function loadEnvFile() {
  const envPath = path.join(__dirname, '.env');
  if (!fs.existsSync(envPath)) return;
  for (const line of fs.readFileSync(envPath, 'utf8').split('\n')) {
    const m = line.match(/^\s*([\w.]+)\s*=\s*(.*?)\s*$/);
    if (m && !(m[1] in process.env)) process.env[m[1]] = m[2];
  }
}
loadEnvFile();

const LIMIT = process.env.COPY_LIMIT ? Number(process.env.COPY_LIMIT) : null;

// 원본값 -> 치환값을 종류별로 일관되게 유지 (같은 원본은 항상 같은 치환값 -> 테이블 간 참조 무결성 유지)
const anonMaps = {};
function anonymize(kind, original, format) {
  if (original == null) return original;
  if (!anonMaps[kind]) anonMaps[kind] = new Map();
  const map = anonMaps[kind];
  if (!map.has(original)) map.set(original, format(map.size + 1));
  return map.get(original);
}

const ANONYMIZERS = {
  name: (v) => anonymize('name', v, (n) => `테스트${n}`),
  phone: (v) => anonymize('phone', v, (n) => `010-0000-${String(n).padStart(4, '0')}`),
  email: (v) => anonymize('email', v, (n) => `test${n}@example.com`),
  dong: (v) => anonymize('dong', v, (n) => String(1000 + n)),
  ho: (v) => anonymize('ho', v, (n) => String(1000 + n)),
  card: (v) => anonymize('card', v, (n) => `CARD${String(n).padStart(12, '0')}`), // 16자 고정
  vin: (v) => anonymize('vin', v, (n) => `VIN${String(n).padStart(14, '0')}`), // 17자 고정
  evcc: (v) => anonymize('evcc', v, (n) => `EVCC${n}`),
  carNo: (v) => anonymize('carNo', v, (n) => `테스트차량${n}호`),
  carNm: (v) => anonymize('carNm', v, (n) => `테스트차종${n}`),
  password: () => 'TEST_PASSWORD_0000', // 고정값 (원본 추정 불가하게)
};

// 복사 순서 = 참조 관계상 자연스러운 순서. 각 테이블의 PII 컬럼 -> 익명화 종류 매핑.
const TABLES = [
  { name: 'TB_ORCX001', pii: {} },
  { name: 'TB_CHCP001', pii: {} },
  { name: 'TB_CHCS001', pii: { CS_PWD: 'password', LST_CS_PWD: 'password' } },
  { name: 'TB_CHCS002', pii: {} },
  { name: 'TB_CUCU001', pii: { CUT_NM: 'name', MBL_PHN_NO: 'phone', EMAIL: 'email', DONG: 'dong', HO: 'ho' } },
  { name: 'TB_CUCU002', pii: { CUT_CRD_NO: 'card', PRNT_CRD_NO: 'card' } },
  { name: 'TB_CUEV001', pii: { VIN_NO: 'vin', CAR_NO: 'carNo', CAR_NM: 'carNm', EVCC_ID: 'evcc' } },
  { name: 'TB_CHCS005', pii: { CUT_CRD_NO: 'card' } },
  { name: 'TB_CHCS006', pii: { CUT_CRD_NO: 'card' } },
  { name: 'TB_RCRC001', pii: { CUT_CRD_NO: 'card' } },
  { name: 'TB_RCRC002', pii: {} },
];

async function copyTable(backupConn, localConn, table) {
  const limitSql = LIMIT ? ` LIMIT ${LIMIT}` : '';
  let rows;
  try {
    [rows] = await backupConn.query(`SELECT * FROM ${table.name}${limitSql}`);
  } catch (e) {
    if (e.code === 'ER_NO_SUCH_TABLE') {
      console.log(`[${table.name}] 백업 DB에 없음 (LH 신규 테이블, skip)`);
      return;
    }
    throw e;
  }
  if (rows.length === 0) {
    console.log(`[${table.name}] 0 rows (skip)`);
    return;
  }

  // 백업 DB(구 스키마)와 로컬 DB(LH용으로 재정리된 스키마)는 컬럼 구성이 다를 수 있으므로
  // 로컬 테이블에 실제 존재하는 컬럼만 골라서 복사한다.
  const [localCols] = await localConn.query(`DESCRIBE ${table.name}`);
  const localColSet = new Set(localCols.map((c) => c.Field));
  const sourceColumns = Object.keys(rows[0]);
  const columns = sourceColumns.filter((c) => localColSet.has(c));
  const dropped = sourceColumns.filter((c) => !localColSet.has(c));
  if (dropped.length) console.log(`[${table.name}] 로컬에 없어 제외된 컬럼: ${dropped.join(',')}`);

  const anonymized = rows.map((row) => columns.map((col) => {
    const kind = table.pii[col];
    return kind ? ANONYMIZERS[kind](row[col]) : row[col];
  }));

  const placeholders = `(${columns.map(() => '?').join(',')})`;
  const sql = `INSERT IGNORE INTO ${table.name} (${columns.join(',')}) VALUES ${anonymized.map(() => placeholders).join(',')}`;
  const [result] = await localConn.query(sql, anonymized.flat());
  console.log(`[${table.name}] ${rows.length} rows read -> ${result.affectedRows} inserted (중복 PK는 무시됨)`);
}

async function main() {
  const backupConn = await mysql.createConnection({
    host: process.env.BACKUP_DB_HOST, port: Number(process.env.BACKUP_DB_PORT) || 3306,
    user: process.env.BACKUP_DB_USER, password: process.env.BACKUP_DB_PASSWORD,
    database: process.env.BACKUP_DB_DATABASE,
  });
  const localConn = await mysql.createConnection({
    host: process.env.LOCAL_DB_HOST, port: Number(process.env.LOCAL_DB_PORT) || 3306,
    user: process.env.LOCAL_DB_USER, password: process.env.LOCAL_DB_PASSWORD,
    database: process.env.LOCAL_DB_DATABASE,
  });

  for (const table of TABLES) {
    await copyTable(backupConn, localConn, table);
  }

  await backupConn.end();
  await localConn.end();
}

main().catch((e) => { console.error(e); process.exit(1); });
