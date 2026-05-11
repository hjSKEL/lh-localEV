    /**
        RSA — 1.2.840.113549.1.1.11

        1         → ITU-T (국제 전기통신 연합)
        .2      → 멤버 바디 (Member Body)
            .840  → 미국 (US, ANSI)
            .113549     → RSADSI (RSA Data Security Inc. 등록 번호)
                .1        → PKCS (Public Key Cryptography Standards)
                .1      → PKCS#1 (RSA 암호화 표준)
                    .11   → sha256WithRSAEncryption
        의미: SHA-256 해시로 서명된 RSA 알고리즘

        전체 명칭: sha256WithRSAEncryption

        PKCS#1 끝 번호	알고리즘
        .4	md5WithRSAEncryption
        .5	sha1WithRSAEncryption
        .11	sha256WithRSAEncryption ← 이 코드
        .12	sha384WithRSAEncryption
        .13	sha512WithRSAEncryption
        ECDSA — 1.2.840.10045

        1         → ITU-T
        .2      → 멤버 바디
            .840  → 미국 (ANSI)
            .10045      → ANSI X9.62 (타원곡선 암호화 표준)
                .4        → 서명 알고리즘
                .3      → ecdsa-with-SHA2 계열
                    .2    → ecdsa-with-SHA256  ← 실제 서명용
        1.2.840.10045는 루트 OID — ECDSA 관련 OID들의 최상위 노드

        하위 OID	알고리즘
        1.2.840.10045.4.3.1	ecdsa-with-SHA224
        1.2.840.10045.4.3.2	ecdsa-with-SHA256
        1.2.840.10045.4.3.3	ecdsa-with-SHA384
        1.2.840.10045.4.3.4	ecdsa-with-SHA512
     */
    // sha256WithRSAEncryption
    public static final String OID_RSA   = "1.2.840.113549.1.1.11";
    // ecdsa-with-SHA256
    public static final String OID_ECDSA = "1.2.840.10045.4.3.2";