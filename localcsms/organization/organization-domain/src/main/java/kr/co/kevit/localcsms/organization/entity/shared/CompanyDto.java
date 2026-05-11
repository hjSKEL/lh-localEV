/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.shared;

import kr.co.kevit.localcsms.organization.entity.domain.Company;

/**
 * 조직 -  법인별도정산 DTO
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a> 
 * @since 2019. 3. 25.
 */
public class CompanyDto extends Company {

        /**
         * 청구방법코드
         * 공통코드 APPA00
         * APPA01:일괄결제, APPA02 : 건별결제
         * BILL_METH_CD  CHAR(6 BYTE),
         */
        private String billMethodCode;

        /**
         * 결제요청코드
         * 공통코드 PAYF00
         * PAYF01 카드
         * PAYF02 세금계산서
         * PAYF03 별도
         * PAYF04 취소
         * PYMT_REQ_CD  CHAR(6 BYTE),
         */
        private String pymtReqCode;

        /**
         * 카드키값
         * BILL_KEY     VARCHAR2(40 BYTE)                 NOT NULL,
         */
        private String billKey;

        /**
         * 카드인증승인번호 거래번호
         * CRD_TRXN_NO  VARCHAR2(20 BYTE)                 NOT NULL,
         */
        private String cardTrxnNo;

        /**
         * 카드사유형
         * CRD_CO_TP  VARCHAR2(2 BYTE)                  NOT NULL,
         */
        private String cardCoType;

        /**
         * 결제카드 ID
         */
        private int id;

        /**
         * 법인관리자 로그인아이디
         */
        private String coUserLoginId;


        public String getBillMethodCode() {
                return billMethodCode;
        }

        public void setBillMethodCode(String billMethodCode) {
                this.billMethodCode = billMethodCode;
        }

        public String getPymtReqCode() {
                return pymtReqCode;
        }

        public void setPymtReqCode(String pymtReqCode) {
                this.pymtReqCode = pymtReqCode;
        }

        public String getBillKey() {
                return billKey;
        }

        public void setBillKey(String billKey) {
                this.billKey = billKey;
        }

        public String getCardTrxnNo() {
                return cardTrxnNo;
        }

        public void setCardTrxnNo(String cardTrxnNo) {
                this.cardTrxnNo = cardTrxnNo;
        }

        public String getCardCoType() {
                return cardCoType;
        }

        public void setCardCoType(String cardCoType) {
                this.cardCoType = cardCoType;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getCoUserLoginId() {
                return coUserLoginId;
        }

        public void setCoUserLoginId(String coUserLoginId) {
                this.coUserLoginId = coUserLoginId;
        }
}
