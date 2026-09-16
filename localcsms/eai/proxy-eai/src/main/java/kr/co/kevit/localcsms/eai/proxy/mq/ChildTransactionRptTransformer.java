package kr.co.kevit.localcsms.eai.proxy.mq;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;

/**
 * LH 모드에서 충전기의 StopTransaction 에 대응하는 {@link Recharging} 1건을 OCPP1.6
 * {@code DataTransfer.req}(vendorId=kr.co.kevit, messageId=ChildTransactionRpt) 로 변환한다.
 *
 * <p>
 * {@code chrgDetailList} 는 충전시작~충전종료 사이에 존재하는 매시 정각(00분)의 갯수만큼 생성된다.
 * 각 구간의 {@code usEle} 는 전체 사용전력량({@link Recharging#getChUseAmount()})을 구간 점유시간
 * 비율로 안분하고(마지막 구간은 잔차를 흡수해 합계가 정확히 총량과 일치하도록 함), {@code usChrg} 는
 * {@code usEle * 단가}(chUseUnitCost) 로 계산한다. 정각이 하나도 없는 구간(1시간 이내 충전)은
 * 예외적으로 전체를 1건으로 처리한다(이 경우 chrgDttm 은 충전종료시각으로 대체).
 * </p>
 *
 * @author bckim
 */
public final class ChildTransactionRptTransformer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter ISO_UTC = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneOffset.UTC);

    private static final String VENDOR_ID = "kr.co.kevit";
    private static final String MESSAGE_ID = "ChildTransactionRpt";

    private ChildTransactionRptTransformer() {
    }

    public static String transform(Recharging recharging, String cpCsId) throws Exception {
        ZonedDateTime start = toUtc(recharging.getChStartDate());
        ZonedDateTime end = toUtc(recharging.getChEndDate());

        List<Map<String, Object>> chrgDetailList = buildDetailList(start, end, recharging.getChUseAmount(),
                recharging.getChUseUnitCost());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("chargeBoxSerialNumber", cpCsId);
        data.put("connectorId", recharging.getEvseId());
        data.put("chrgStDttm", ISO_UTC.format(start));
        data.put("chrgEdDttm", ISO_UTC.format(end));
        data.put("chrgDetailList", chrgDetailList);

        Map<String, Object> dataTransferPayload = new LinkedHashMap<>();
        dataTransferPayload.put("vendorId", VENDOR_ID);
        dataTransferPayload.put("messageId", MESSAGE_ID);
        dataTransferPayload.put("data", MAPPER.writeValueAsString(data));

        List<Object> call = List.of(2, UUID.randomUUID().toString(), "DataTransfer", dataTransferPayload);
        return MAPPER.writeValueAsString(call);
    }

    private static ZonedDateTime toUtc(Date date) {
        return date.toInstant().atZone(ZoneOffset.UTC);
    }

    private static List<Map<String, Object>> buildDetailList(ZonedDateTime start, ZonedDateTime end,
            BigDecimal totalUseAmount, BigDecimal unitCost) {
        List<ZonedDateTime> marks = new ArrayList<>();
        ZonedDateTime candidate = start.withMinute(0).withSecond(0).withNano(0);
        if (!candidate.isAfter(start)) {
            candidate = candidate.plusHours(1);
        }
        while (candidate.isBefore(end)) {
            marks.add(candidate);
            candidate = candidate.plusHours(1);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        if (marks.isEmpty()) {
            result.add(buildEntry(totalUseAmount, unitCost, end));
            return result;
        }

        long totalSeconds = Duration.between(start, end).getSeconds();
        BigDecimal allocated = BigDecimal.ZERO;
        ZonedDateTime segmentStart = start;
        for (int i = 0; i < marks.size(); i++) {
            boolean isLast = i == marks.size() - 1;
            ZonedDateTime segmentEnd = isLast ? end : marks.get(i);
            BigDecimal usEle;
            if (isLast) {
                usEle = totalUseAmount.subtract(allocated);
            } else {
                long segmentSeconds = Duration.between(segmentStart, segmentEnd).getSeconds();
                usEle = totalUseAmount.multiply(BigDecimal.valueOf(segmentSeconds))
                        .divide(BigDecimal.valueOf(totalSeconds), 3, RoundingMode.HALF_UP);
                allocated = allocated.add(usEle);
            }
            result.add(buildEntry(usEle, unitCost, marks.get(i)));
            segmentStart = segmentEnd;
        }
        return result;
    }

    private static Map<String, Object> buildEntry(BigDecimal usEle, BigDecimal unitCost, ZonedDateTime chrgDttm) {
        BigDecimal usChrg = usEle.multiply(unitCost).setScale(0, RoundingMode.HALF_UP);
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("usEle", usEle);
        entry.put("usChrg", usChrg);
        entry.put("chrgDttm", ISO_UTC.format(chrgDttm));
        return entry;
    }
}
