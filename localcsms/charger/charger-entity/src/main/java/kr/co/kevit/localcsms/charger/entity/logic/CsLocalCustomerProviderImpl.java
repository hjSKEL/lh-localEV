package kr.co.kevit.localcsms.charger.entity.logic;

import kr.co.kevit.localcsms.charger.entity.CsLocalCustomerProvider;
import kr.co.kevit.localcsms.charger.entity.dao.CsLocalCustomerMapper;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomer;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomerToken;
import kr.co.kevit.localcsms.charger.entity.shared.CsLocalCustomerSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TB_CHLC001 / TB_CHLC002 - 충전기 로컬 고객 Provider 구현
 */
@Service
public class CsLocalCustomerProviderImpl implements CsLocalCustomerProvider {

    private final CsLocalCustomerMapper mapper;

    public CsLocalCustomerProviderImpl(CsLocalCustomerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void saveHeader(CsLocalCustomer header) {
        mapper.insertHeader(header);
    }

    @Override
    public void updateHeader(CsLocalCustomer header) {
        mapper.updateHeader(header);
    }

    @Override
    public CsLocalCustomer findOne(String cpId, String csId) {
        CsLocalCustomer header = mapper.findHeader(cpId, csId);
        if (header == null) return null;
        header.setTokens(mapper.findTokens(cpId, csId));
        return header;
    }

    @Override
    public Page<CsLocalCustomer> findBySearchCond(CsLocalCustomerSearchCond cond) {
        int total = mapper.countBySearchCond(cond);
        cond.setTotalItemCount(total);
        List<CsLocalCustomer> list = total > 0 ? mapper.selectBySearchCond(cond) : new ArrayList<>();
        return new Page<>(cond, list);
    }

    @Override
    public void deleteHeader(String cpId, String csId) {
        mapper.deleteTokens(cpId, csId);
        mapper.deleteHeader(cpId, csId);
    }

    @Override
    public void saveToken(CsLocalCustomerToken token) {
        mapper.insertToken(token);
    }

    @Override
    public void mergeToken(CsLocalCustomerToken token) {
        mapper.mergeToken(token);
    }

    @Override
    public void replaceAllTokens(String cpId, String csId, List<CsLocalCustomerToken> tokens) {
        mapper.deleteTokens(cpId, csId);
        if (tokens != null && !tokens.isEmpty()) {
            mapper.insertTokenBatch(tokens);
        }
    }

    @Override
    public List<CsLocalCustomerToken> findTokens(String cpId, String csId) {
        return mapper.findTokens(cpId, csId);
    }

    @Override
    public void deleteToken(String cpId, String csId, String token, String tokenType) {
        mapper.deleteToken(cpId, csId, token, tokenType);
    }
}
