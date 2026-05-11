package kr.co.kevit.localcsms.charger.process.logic;

import kr.co.kevit.localcsms.charger.entity.CsLocalCustomerProvider;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomer;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomerToken;
import kr.co.kevit.localcsms.charger.entity.shared.CsLocalCustomerSearchCond;
import kr.co.kevit.localcsms.charger.process.CsLocalCustomerService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TB_CHLC001 / TB_CHLC002 - 충전기 로컬 고객 Service 구현
 */
@Service
public class CsLocalCustomerServiceImpl implements CsLocalCustomerService {

    private final CsLocalCustomerProvider csLocalCustomerProvider;

    public CsLocalCustomerServiceImpl(CsLocalCustomerProvider csLocalCustomerProvider) {
        this.csLocalCustomerProvider = csLocalCustomerProvider;
    }

    @Override
    public void saveHeader(CsLocalCustomer header) {
        csLocalCustomerProvider.saveHeader(header);
    }

    @Override
    public void updateHeader(CsLocalCustomer header) {
        csLocalCustomerProvider.updateHeader(header);
    }

    @Override
    public CsLocalCustomer findOne(String cpId, String csId) {
        return csLocalCustomerProvider.findOne(cpId, csId);
    }

    @Override
    public Page<CsLocalCustomer> retrieveBySearchCond(CsLocalCustomerSearchCond cond) {
        return csLocalCustomerProvider.findBySearchCond(cond);
    }

    @Override
    public void deleteHeader(String cpId, String csId) {
        csLocalCustomerProvider.deleteHeader(cpId, csId);
    }

    @Override
    public void addToken(CsLocalCustomerToken token) {
        csLocalCustomerProvider.saveToken(token);
    }

    @Override
    public void mergeToken(CsLocalCustomerToken token) {
        csLocalCustomerProvider.mergeToken(token);
    }

    @Override
    public void replaceAllTokens(String cpId, String csId, List<CsLocalCustomerToken> tokens) {
        csLocalCustomerProvider.replaceAllTokens(cpId, csId, tokens);
    }

    @Override
    public List<CsLocalCustomerToken> findTokens(String cpId, String csId) {
        return csLocalCustomerProvider.findTokens(cpId, csId);
    }

    @Override
    public void deleteToken(String cpId, String csId, String token, String tokenType) {
        csLocalCustomerProvider.deleteToken(cpId, csId, token, tokenType);
    }
}
