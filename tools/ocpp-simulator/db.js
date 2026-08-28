const fs = require('fs');
const path = require('path');
const mysql = require('mysql2/promise');

// 별도 dotenv 의존성 없이 .env 파일 하나만 읽어 process.env에 채운다. (backup-db-copy/copy.js와 동일 패턴)
function loadEnvFile() {
  const envPath = path.join(__dirname, '.env');
  if (!fs.existsSync(envPath)) return;
  for (const line of fs.readFileSync(envPath, 'utf8').split('\n')) {
    const m = line.match(/^\s*([\w.]+)\s*=\s*(.*?)\s*$/);
    if (m && !(m[1] in process.env)) process.env[m[1]] = m[2];
  }
}
loadEnvFile();

const pool = mysql.createPool({
  host: process.env.LOCAL_DB_HOST,
  port: process.env.LOCAL_DB_PORT,
  user: process.env.LOCAL_DB_USER,
  password: process.env.LOCAL_DB_PASSWORD,
  database: process.env.LOCAL_DB_DATABASE,
});

// TB_CHCS005.CA_ELE_NRG(누적 계량값, kWh)를 읽어 OCPP meterStart 단위(Wh)로 변환해 돌려준다.
// daemon의 StartTransactionBean이 meterStart(Wh)를 1000으로 나눠 CA_ELE_NRG(kWh)로 저장하는 것과 반대 방향 변환.
async function getStartMeterWh(cpId, csId, evseId) {
  const [rows] = await pool.query(
    'SELECT CA_ELE_NRG FROM TB_CHCS005 WHERE CP_ID = ? AND CS_ID = ? AND EVSE_ID = ?',
    [cpId, csId, evseId],
  );
  const caEleNrg = rows[0]?.CA_ELE_NRG ?? 0;
  return Math.round(Number(caEleNrg) * 1000);
}

module.exports = { getStartMeterWh, close: () => pool.end() };
