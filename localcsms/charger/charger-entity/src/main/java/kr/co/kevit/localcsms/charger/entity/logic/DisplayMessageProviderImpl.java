package kr.co.kevit.localcsms.charger.entity.logic;

import kr.co.kevit.localcsms.charger.entity.DisplayMessageProvider;
import kr.co.kevit.localcsms.charger.entity.dao.DisplayMessageMapper;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessage;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessageContent;
import kr.co.kevit.localcsms.charger.entity.shared.DisplayMessageSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TB_CHDM001 / TB_CHDM002 - 충전기 디스플레이 메시지 Provider 구현
 */
@Service
public class DisplayMessageProviderImpl implements DisplayMessageProvider {

    private final DisplayMessageMapper mapper;

    public DisplayMessageProviderImpl(DisplayMessageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void saveMessage(DisplayMessage message) {
        mapper.insertMessage(message);
    }

    @Override
    public void updateMessage(DisplayMessage message) {
        mapper.updateMessage(message);
    }

    @Override
    public void saveMessageWithContents(DisplayMessage message) {
        mapper.insertMessage(message);
        if (message.getContents() != null) {
            for (DisplayMessageContent c : message.getContents()) {
                c.setMessageId(message.getMessageId());
                mapper.mergeContent(c);
            }
        }
    }

    @Override
    public void deleteMessage(int messageId, String cpId, String csId) {
        mapper.deleteContents(messageId);
        mapper.deleteMessage(messageId, cpId, csId);
    }

    @Override
    public void deleteAllByCpIdAndCsId(String cpId, String csId) {
        mapper.deleteAllByCpIdAndCsId(cpId, csId);
    }

    @Override
    public DisplayMessage findOne(int messageId, String cpId, String csId) {
        DisplayMessage message = mapper.findOne(messageId, cpId, csId);
        if (message != null) {
            List<DisplayMessageContent> contents = mapper.selectContents(messageId);
            message.setContents(contents);
        }
        return message;
    }

    @Override
    public List<DisplayMessage> findByCpIdAndCsId(String cpId, String csId) {
        return mapper.selectByCpIdAndCsId(cpId, csId);
    }

    @Override
    public Page<DisplayMessage> findBySearchCond(DisplayMessageSearchCond cond) {
        int total = mapper.countBySearchCond(cond);
        cond.setTotalItemCount(total);
        List<DisplayMessage> list = total > 0 ? mapper.selectBySearchCond(cond) : new ArrayList<>();
        return new Page<>(cond, list);
    }

    @Override
    public void saveContent(DisplayMessageContent content) {
        mapper.mergeContent(content);
    }
}
