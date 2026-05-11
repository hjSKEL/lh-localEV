// 충전기 데이터
const chargers = [
  {
    id: 1,
    name: '충전기 A-01',
    type: 'Fast',
    status: 'Available',
    power: '50kW',
    location: '서울시 강남구 테헤란로 123',
    price: '400원/kWh',
    connector: 'DC콤보',
    lastMaintenance: '2026-03-01'
  },
  {
    id: 2,
    name: '충전기 A-02',
    type: 'Fast',
    status: 'Occupied',
    power: '50kW',
    location: '서울시 강남구 테헤란로 123',
    price: '400원/kWh',
    connector: 'DC콤보',
    lastMaintenance: '2026-03-02'
  },
  {
    id: 3,
    name: '충전기 B-01',
    type: 'Slow',
    status: 'Available',
    power: '7kW',
    location: '서울시 서초구 서초대로 456',
    price: '200원/kWh',
    connector: 'AC완속5핀',
    lastMaintenance: '2026-02-28'
  },
  {
    id: 4,
    name: '충전기 B-02',
    type: 'Slow',
    status: 'Faulted',
    power: '7kW',
    location: '서울시 서초구 서초대로 456',
    price: '200원/kWh',
    connector: 'AC완속5핀',
    lastMaintenance: '2026-02-25'
  },
  {
    id: 5,
    name: '충전기 C-01',
    type: 'Fast',
    status: 'Available',
    power: '100kW',
    location: '경기도 성남시 분당구 판교로 789',
    price: '450원/kWh',
    connector: 'DC콤보',
    lastMaintenance: '2026-03-03'
  },
  {
    id: 6,
    name: '충전기 C-02',
    type: 'Fast',
    status: 'Unavailable',
    power: '100kW',
    location: '경기도 성남시 분당구 판교로 789',
    price: '450원/kWh',
    connector: 'DC콤보',
    lastMaintenance: '2026-02-20'
  },
  {
    id: 7,
    name: '충전기 D-01',
    type: 'Slow',
    status: 'Available',
    power: '11kW',
    location: '인천시 연수구 센트럴로 321',
    price: '250원/kWh',
    connector: 'AC완속7핀',
    lastMaintenance: '2026-03-05'
  },
  {
    id: 8,
    name: '충전기 D-02',
    type: 'Fast',
    status: 'Available',
    power: '50kW',
    location: '인천시 연수구 센트럴로 321',
    price: '400원/kWh',
    connector: 'DC차데모',
    lastMaintenance: '2026-03-04'
  }
];

// 예약 이력 데이터
const reservationHistories = [
  {
    id: 'RES00000000001',
    chargerId: 1,
    chargerName: '충전기 A-01',
    reservationId: 12345,
    idTag: 'USER001',
    parentIdTag: 'PARENT001',
    expiryDate: '2026-03-08 18:00:00',
    status: 'Completed',
    createdDate: '2026-03-08 14:30:00',
    startDate: '2026-03-08 15:00:00',
    endDate: '2026-03-08 16:45:00'
  },
  {
    id: 'RES00000000002',
    chargerId: 1,
    chargerName: '충전기 A-01',
    reservationId: 12346,
    idTag: 'USER002',
    parentIdTag: '',
    expiryDate: '2026-03-07 20:00:00',
    status: 'Expired',
    createdDate: '2026-03-07 16:00:00',
    startDate: '',
    endDate: ''
  },
  {
    id: 'RES00000000003',
    chargerId: 2,
    chargerName: '충전기 A-02',
    reservationId: 12347,
    idTag: 'USER001',
    parentIdTag: '',
    expiryDate: '2026-03-06 19:00:00',
    status: 'Completed',
    createdDate: '2026-03-06 14:00:00',
    startDate: '2026-03-06 15:30:00',
    endDate: '2026-03-06 17:20:00'
  },
  {
    id: 'RES00000000004',
    chargerId: 3,
    chargerName: '충전기 B-01',
    reservationId: 12348,
    idTag: 'USER003',
    parentIdTag: '',
    expiryDate: '2026-03-05 18:00:00',
    status: 'Cancelled',
    createdDate: '2026-03-05 12:00:00',
    startDate: '',
    endDate: ''
  },
  {
    id: 'RES00000000005',
    chargerId: 1,
    chargerName: '충전기 A-01',
    reservationId: 12349,
    idTag: 'USER004',
    parentIdTag: 'PARENT002',
    expiryDate: '2026-03-04 17:00:00',
    status: 'Completed',
    createdDate: '2026-03-04 10:00:00',
    startDate: '2026-03-04 14:00:00',
    endDate: '2026-03-04 15:30:00'
  },
  {
    id: 'RES00000000006',
    chargerId: 5,
    chargerName: '충전기 C-01',
    reservationId: 12350,
    idTag: 'USER002',
    parentIdTag: '',
    expiryDate: '2026-03-03 19:00:00',
    status: 'Rejected',
    createdDate: '2026-03-03 13:00:00',
    startDate: '',
    endDate: ''
  },
  {
    id: 'RES00000000007',
    chargerId: 1,
    chargerName: '충전기 A-01',
    reservationId: 12351,
    idTag: 'USER005',
    parentIdTag: '',
    expiryDate: '2026-03-02 20:00:00',
    status: 'Completed',
    createdDate: '2026-03-02 11:00:00',
    startDate: '2026-03-02 16:00:00',
    endDate: '2026-03-02 18:30:00'
  },
  {
    id: 'RES00000000008',
    chargerId: 8,
    chargerName: '충전기 D-02',
    reservationId: 12352,
    idTag: 'USER001',
    parentIdTag: '',
    expiryDate: '2026-03-01 18:00:00',
    status: 'Completed',
    createdDate: '2026-03-01 09:00:00',
    startDate: '2026-03-01 14:30:00',
    endDate: '2026-03-01 16:00:00'
  }
];
