// copy.js는 TB_CHCS001(커넥터)과 TB_CHCS005(커넥터 현재상태)를 각각 독립적으로 LIMIT 잘라서 복사하기 때문에
// 커넥터는 있는데 상태 행이 없는 경우가 생긴다 -> 관제 화면에서 그 커넥터 상태가 비어 보임.
// TB_CHCS001에 있는데 TB_CHCS005가 없는 커넥터에 기본 상태(충전대기/CHRS03) 행을 채워준다.
const fs = require('fs');
const path = require('path');
const mysql = require('mysql2/promise');

function loadEnvFile() {
  const envPath = path.join(__dirname, '.env');
  if (!fs.existsSync(envPath)) return;
  for (const line of fs.readFileSync(envPath, 'utf8').split('\n')) {
    const m = line.match(/^\s*([\w.]+)\s*=\s*(.*?)\s*$/);
    if (m && !(m[1] in process.env)) process.env[m[1]] = m[2];
  }
}
loadEnvFile();

async function main() {
  const conn = await mysql.createConnection({
    host: process.env.LOCAL_DB_HOST, port: Number(process.env.LOCAL_DB_PORT) || 3306,
    user: process.env.LOCAL_DB_USER, password: process.env.LOCAL_DB_PASSWORD,
    database: process.env.LOCAL_DB_DATABASE,
  });

  const [result] = await conn.query(`
    INSERT INTO TB_CHCS005 (CP_ID, CS_ID, EVSE_ID, CS_CAT_CD, CS_STAT_CD, CS_CBL_STAT, CU_ELE_NRG, CA_ELE_NRG, INFO_COLL_DT, UPD_DT)
    SELECT c.CP_ID, c.CS_ID, 1, c.CS_CAT_CD, 'CHRS03', '0', 0, 0, NOW(), NOW()
    FROM TB_CHCS001 c
    WHERE NOT EXISTS (SELECT 1 FROM TB_CHCS005 s WHERE s.CP_ID = c.CP_ID AND s.CS_ID = c.CS_ID)
  `);
  console.log(`TB_CHCS005: ${result.affectedRows}개 커넥터에 기본 상태(충전대기/CHRS03) 행 생성`);

  await conn.end();
}
main().catch((e) => { console.error(e); process.exit(1); });
