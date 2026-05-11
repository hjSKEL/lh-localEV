package kr.co.kevit.localcsms.charger.process.logic;

import kr.co.kevit.localcsms.charger.entity.DisplayMessageProvider;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessage;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessageContent;
import kr.co.kevit.localcsms.charger.entity.shared.DisplayMessageSearchCond;
import kr.co.kevit.localcsms.charger.process.DisplayMessageService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TB_CHDM001 / TB_CHDM002 - 충전기 디스플레이 메시지 Service 구현
 */
@Service
public class DisplayMessageServiceImpl implements DisplayMessageService {

    private final DisplayMessageProvider displayMessageProvider;

    public DisplayMessageServiceImpl(DisplayMessageProvider displayMessageProvider) {
        this.displayMessageProvider = displayMessageProvider;
    }

    @Override
    public void saveMessage(DisplayMessage message) {
        displayMessageProvider.saveMessage(message);
    }

    @Override
    public void updateMessage(DisplayMessage message) {
        displayMessageProvider.updateMessage(message);
    }

    @Override
    public void saveMessageWithContents(DisplayMessage message) {
        displayMessageProvider.saveMessageWithContents(message);
    }

    @Override
    public void deleteMessage(int messageId, String cpId, String csId) {
        displayMessageProvider.deleteMessage(messageId, cpId, csId);
    }

    @Override
    public void deleteAllByCpIdAndCsId(String cpId, String csId) {
        displayMessageProvider.deleteAllByCpIdAndCsId(cpId, csId);
    }

    @Override
    public DisplayMessage findOne(int messageId, String cpId, String csId) {
        return displayMessageProvider.findOne(messageId, cpId, csId);
    }

    @Override
    public List<DisplayMessage> retrieveByCpIdAndCsId(String cpId, String csId) {
        return displayMessageProvider.findByCpIdAndCsId(cpId, csId);
    }

    @Override
    public Page<DisplayMessage> retrieveBySearchCond(DisplayMessageSearchCond cond) {
        return displayMessageProvider.findBySearchCond(cond);
    }

    @Override
    public void saveContent(DisplayMessageContent content) {
        displayMessageProvider.saveContent(content);
    }
}
