const WebSocket = require('ws');
const crypto = require('crypto');

/**
 * OCPP1.6 충전기 한 대에 대한 WebSocket 연결 + call() 헬퍼.
 * ocpp16-daemon의 WebSocketConfig("/ocpp16/{cpCsId}")·OcppProtocolHandshakeInterceptor(서브프로토콜 필수)에 맞춘 최소 클라이언트.
 *
 * @param cpCsId "CP_ID-CS_ID" (예: "223406-01")
 * @param opts.csPassword daemon이 sp=1,2(Basic Auth 필수)로 기동된 경우에만 필요. 기본 기동(sp 미지정)에서는 불필요.
 */
function connect(cpCsId, { host = 'localhost', port = 8900, csPassword } = {}) {
  return new Promise((resolve, reject) => {
    const headers = {};
    if (csPassword) {
      headers.Authorization = 'Basic ' + Buffer.from(`${cpCsId}:${csPassword}`).toString('base64');
    }
    const ws = new WebSocket(`ws://${host}:${port}/ocpp16/${cpCsId}`, ['ocpp1.6'], { headers });
    const pending = new Map(); // uniqueId -> {resolve, reject}

    ws.on('message', (raw) => {
      const [type, uniqueId, a, b] = JSON.parse(raw.toString());
      const p = pending.get(uniqueId);
      if (!p) return; // 서버발 CALL(RemoteStartTransaction 등, type 2)은 응답하지 않음 — 범위 밖
      pending.delete(uniqueId);
      if (type === 3) p.resolve(a); // CALLRESULT
      else if (type === 4) p.reject(new Error(`CALLERROR ${a}: ${b}`)); // CALLERROR
    });

    ws.once('open', () => resolve({
      cpCsId,
      call(action, payload) {
        return new Promise((res, rej) => {
          const uniqueId = crypto.randomUUID();
          pending.set(uniqueId, { resolve: res, reject: rej });
          ws.send(JSON.stringify([2, uniqueId, action, payload]));
        });
      },
      close: () => ws.close(),
    }));
    ws.once('error', reject);
  });
}

module.exports = { connect };
