package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.payment.entity.domain.Vat;
import kr.co.kevit.localcsms.payment.entity.domain.VatHis;
import kr.co.kevit.localcsms.payment.process.VatService;
import kr.co.kevit.ocpp201.domain.AddressType;
import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.GenericStatusEnumType;

/**
 * OCPP 2.1 VatNumberValidation 처리.
 *
 * <p>스펙 — Part 2 §1.91, Use case C18 (Authorization using locally connected payment terminal),
 * Requirements C18.FR.08~10.</p>
 *
 * <p>동작:
 *   <ol>
 *     <li>요청 vatNumber 로 TB_PAVAT01 마스터 조회.</li>
 *     <li>USE_YN='Y' 이면 status=Accepted, company(AddressType) 동봉.</li>
 *     <li>미존재/비활성 → status=Rejected, statusInfo.reasonCode 설정.</li>
 *     <li>vatNumber/evseId 응답 echo (C18.FR.10 SHALL).</li>
 *     <li>검증 결과를 TB_PAVAT02 에 이력 기록.</li>
 *   </ol>
 * </p>
 */
@Component("VatNumberValidation")
public class VatNumberValidationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(VatNumberValidationBean.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private VatService vatService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String[] csIds = cpCsId.split(StringConstants.DASH);
        String cpId = csIds.length > 0 ? csIds[0] : null;
        String csId = csIds.length > 1 ? csIds[1] : null;

        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.VatNumberValidation request = objectMapper.readValue(text,
                kr.co.kevit.ocpp201.request.VatNumberValidation.class);
        kr.co.kevit.ocpp201.response.VatNumberValidation response = new kr.co.kevit.ocpp201.response.VatNumberValidation();

        String vatNumber = request.getVatNumber();
        Integer evseId = request.getEvseId();
        LOGGER.info("[VatNumberValidation] cpId={} csId={} evseId={} vatNumber={}", cpId, csId, evseId, vatNumber);

        response.setVatNumber(vatNumber);
        response.setEvseId(evseId);

        GenericStatusEnumType status = GenericStatusEnumType.Rejected;
        String reasonCd = null;

        if (vatService == null) {
            LOGGER.warn("[VatNumberValidation] VatService 미주입 — Rejected");
            reasonCd = "NO_SERVICE";
        } else if (vatNumber == null || vatNumber.trim().isEmpty()) {
            reasonCd = "EMPTY";
        } else {
            Vat vat = vatService.retrieveVat(vatNumber);
            if (vat == null) {
                reasonCd = "NOT_FOUND";
            } else if (!Vat.USE_Y.equals(vat.getUseYn())) {
                reasonCd = "INACTIVE";
            } else {
                status = GenericStatusEnumType.Accepted;
                AddressType company = new AddressType();
                company.setName(vat.getCompanyNm());
                company.setAddress1(vat.getAddr1());
                company.setAddress2(vat.getAddr2());
                company.setCity(vat.getCity());
                company.setPostalCode(vat.getPostalCd());
                company.setCountry(vat.getCountry());
                response.setCompany(company);
            }
        }
        response.setStatus(status);

        if (reasonCd != null) {
            StatusInfoType info = new StatusInfoType();
            info.setReasonCode(reasonCd);
            response.setStatusInfo(info);
        }

        if (vatService != null) {
            try {
                VatHis his = new VatHis();
                his.setVatNo(vatNumber);
                his.setCpId(cpId);
                his.setCsId(csId);
                his.setEvseId(evseId);
                his.setResultStatus(status.name());
                his.setReasonCd(reasonCd);
                his.setVerifiedDt(new Date());
                vatService.recordVatValidation(his);
            } catch (Exception e) {
                LOGGER.warn("[VatNumberValidation] 이력 기록 실패: {}", e.getMessage());
            }
        }

        return objectMapper.valueToTree(response);
    }
}
