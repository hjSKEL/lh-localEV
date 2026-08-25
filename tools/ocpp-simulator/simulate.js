const fs = require('fs');
const path = require('path');
const { connect } = require('./client');

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));
const iso = () => new Date().toISOString().split('.')[0] + 'Z';

async function statusNotification(client, connectorId, status) {
  await client.call('StatusNotification', {
    connectorId, errorCode: 'NoError', info: '', status, vendorId: 'kr.co.kevit', timestamp: iso(),
  });
}

// idTag 하나로 Authorize -> (Accepted면) StartTransaction -> MeterValues N회 -> StopTransaction 진행.
// 실제 충전기와 동일하게, Authorize가 Accepted가 아니면 세션을 시작하지 않고 그대로 종료한다.
async function runSession(client, connectorId, session) {
  const { idTag, meterStartWh = 0, kwhPerTick = 500, ticks = 4, tickIntervalMs = 500 } = session;

  const auth = await client.call('Authorize', { idTag });
  const status = auth.idTagInfo?.status;
  console.log(`[${client.cpCsId}] Authorize idTag=${idTag} -> ${status}`);
  if (status !== 'Accepted') return;

  await statusNotification(client, connectorId, 'Preparing');
  const start = await client.call('StartTransaction', {
    connectorId, idTag, meterStart: meterStartWh, timestamp: iso(),
  });
  const transactionId = start.transactionId;
  console.log(`[${client.cpCsId}] StartTransaction idTag=${idTag} -> transactionId=${transactionId}`);

  await statusNotification(client, connectorId, 'Charging');
  let meter = meterStartWh;
  for (let i = 0; i < ticks; i++) {
    await sleep(tickIntervalMs);
    meter += kwhPerTick;
    await client.call('MeterValues', {
      connectorId,
      transactionId,
      meterValue: [{
        timestamp: iso(),
        sampledValue: [{ value: String(meter), measurand: 'Energy.Active.Import.Register', unit: 'Wh' }],
      }],
    });
  }

  await client.call('StopTransaction', {
    transactionId, idTag, meterStop: meter, timestamp: iso(), reason: 'Local',
  });
  console.log(`[${client.cpCsId}] StopTransaction transactionId=${transactionId} meterStop=${meter}`);
  await statusNotification(client, connectorId, 'Finishing');
  await statusNotification(client, connectorId, 'Available');
}

async function runCharger(cfg) {
  const cpCsId = `${cfg.cpId}-${cfg.csId}`;
  const client = await connect(cpCsId, { host: cfg.host, port: cfg.port, csPassword: cfg.csPassword });
  console.log(`[${cpCsId}] connected`);

  await client.call('BootNotification', {
    chargePointVendor: 'KEVIT', chargePointModel: 'SIMULATOR', chargeBoxSerialNumber: cpCsId,
  });
  await statusNotification(client, cfg.connectorId ?? 1, 'Available');

  for (const session of cfg.sessions || []) {
    await runSession(client, cfg.connectorId ?? 1, session);
  }

  client.close();
  console.log(`[${cpCsId}] done`);
}

async function main() {
  const file = process.argv[2];
  if (!file) {
    console.error('사용법: node simulate.js <scenario.json>');
    process.exit(1);
  }
  const scenario = JSON.parse(fs.readFileSync(path.resolve(file), 'utf8'));
  const chargers = Array.isArray(scenario) ? scenario : [scenario];
  await Promise.all(chargers.map((cfg) =>
    runCharger(cfg).catch((e) => console.error(`[${cfg.cpId}-${cfg.csId}] ERROR:`, e.message))));
}

main();
