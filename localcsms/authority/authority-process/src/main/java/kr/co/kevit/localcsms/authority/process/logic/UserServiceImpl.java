/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.process.logic;

import kr.co.kevit.localcsms.authority.entity.UserProvider;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.shared.UserInfoDto;
import kr.co.kevit.localcsms.authority.entity.shared.UserSearchCond;
import kr.co.kevit.localcsms.authority.process.UserService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.number.NumberConstants;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.external.EmployeeExtProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 27.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserProvider provider;

    @Autowired
    private EmployeeExtProcess employeeExtProcess;

    @Override
    public boolean modifyUser(User user) {
        //
        return provider.modifyUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public User retrieveUserByIdNType(String loginId, String type) {
        //
        return provider.retrieveUserByIdNType(loginId, type);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public User retrieveUserById(String loginId) {
        //
        return provider.retrieveUserById(loginId);
    }

    @Override
    public boolean saveUser(User user) {
        //
        boolean result = false;
        User existUser = provider.retrieveUserByUserId(user.getUserId());
        //String beforeLoginId = existUser.getLoginId();

        if (existUser != null) {
            // 존재하면 수정
            if (StringUtils.isNoneEmpty(user.getLoginId())) {
                existUser.setLoginId(user.getLoginId());
            }
            if (StringUtils.isNoneEmpty(user.getUserPwd())) {
                existUser.setSalt(user.getSalt());
                existUser.setUserPwd(user.getUserPwd());
                existUser.setPwUpdateDate(user.getWriter().getUpdateDate());
                existUser.setPwInitYn(user.getPwInitYn());
                existUser.setPwFailCount(NumberConstants.ZERO);
            }
            existUser.setWriter(user.getWriter());
            result = provider.modifyUserAll(existUser);
        } else {
            // 미존재 시 등록
            result = provider.registerUser(user);
        }
        // 로그인 권한은 TB_USAU002(UserRole)에 별도 저장하지 않고 로그인 시점에 TB_OREM001.MN_ROLE_TP를 직접 조회한다
        // (WebAuthenticationProvider 참고) - 여기서 더 이상 사용자/역할 매핑을 만들 필요가 없다.
        return result;
    }

    @Override public Page<UserInfoDto> retrieveUserInfoDtoBySearchCond(UserSearchCond searchCond) {
        //
        Page<UserInfoDto> resultSet = new Page<>();
        Page<User> userSet = provider.retrieveUserBySearchCond(searchCond);
        resultSet.setCriteria(userSet.getCriteria());
        if(userSet.getCriteria().getTotalItemCount() == 0 || userSet.getResult() == null || userSet.getResult().isEmpty()) {
            return resultSet;
        }
        List<UserInfoDto> userInfoDtos = new ArrayList<>();
        Map<String, EmployeeDto> employeeMap =retrieveEmployee(userSet.getResult());
        EmployeeDto employee = null;
        for(User user :userSet.getResult()) {
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setLoginId(user.getLoginId());
            userInfoDto.setUserId(user.getUserId());
            employee = employeeMap.get(user.getUserId());
            if (employee != null) {
                userInfoDto.setUserName(employee.getEmplName());
                userInfoDto.setCompanyName(employee.getCompanyName());
                userInfoDto.setUserRole(employee.getRoleType().getDesc());
                userInfoDtos.add(userInfoDto);
            }
        }
        resultSet.setResult(userInfoDtos);
        return resultSet;
    }

    private Map<String, EmployeeDto> retrieveEmployee(List<User> users){
        //
        Map<String, EmployeeDto> employeeMap = new HashMap<String, EmployeeDto>();
        int index = 0;
        List<String> employeeIds = new ArrayList<String>();
        for(int i = 0, size = users.size(); i < size ; ++i) {
            ++index;
            employeeIds.add(users.get(i).getUserId());
            if(index == 100) {
                List<EmployeeDto> employeeDtos = employeeExtProcess.retrieveEmployeeByIds(employeeIds);
                setEmployeeMap(employeeDtos, employeeMap);
                employeeIds.clear();
                index = 0;
            }
        }
        if(!employeeIds.isEmpty()) {
            List<EmployeeDto> employeeDtos = employeeExtProcess.retrieveEmployeeByIds(employeeIds);
            setEmployeeMap(employeeDtos, employeeMap);
        }
        return employeeMap;
    }

    private void setEmployeeMap(List<EmployeeDto> employeeDtos, Map<String, EmployeeDto> employeeMap) {
        for(EmployeeDto employeeDto : employeeDtos) {
            employeeMap.put(employeeDto.getEmployeeId(), employeeDto);
        }
    }
    
    @Override
    public void modifyPassWord(String loginId, String newPassword) {
        //
        User user = provider.retrieveUserById(loginId);
        user.setUserPwd(newPassword);
        user.setPwFailCount(0);
        user.setWriter(new Writer(loginId));
        provider.modifyUserAll(user);
    }
}
