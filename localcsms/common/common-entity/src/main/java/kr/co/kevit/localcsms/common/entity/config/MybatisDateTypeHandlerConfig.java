/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.config;

import java.util.Date;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * 글로벌 {@code java.util.Date ↔ DATETIME(TIMESTAMP)} 매핑 강제 설정.
 *
 * <p>resultMap의 {@code jdbcType="TIMESTAMP"} 만으로는 일부 환경에서
 * {@code DateOnlyTypeHandler(getDate())} 가 선택되어 시분초가 잘리는 문제가 있다.
 * 이 설정은 SqlSessionFactory 가 초기화된 직후 TypeHandlerRegistry 의
 * {@code Date.class} 매핑 3종을 모두 {@code DateTypeHandler(getTimestamp())} 로
 * 강제하여 매퍼 단위의 {@code typeHandler} 명시 없이도 시분초·밀리초가 보존되도록 한다.</p>
 *
 * <p>등록 대상</p>
 * <ul>
 *   <li>{@code (Date.class, default)} — jdbcType 미지정 케이스</li>
 *   <li>{@code (Date.class, TIMESTAMP)} — jdbcType=TIMESTAMP 케이스</li>
 *   <li>{@code (Date.class, DATE)} — jdbcType=DATE 케이스도 의도적으로 Timestamp 핸들러로 override
 *       (DATE 컬럼이라도 시분초 보존을 우선)</li>
 * </ul>
 */
@Configuration
public class MybatisDateTypeHandlerConfig implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisDateTypeHandlerConfig.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof SqlSessionFactory) {
            TypeHandlerRegistry registry = ((SqlSessionFactory) bean).getConfiguration().getTypeHandlerRegistry();
            DateTypeHandler handler = new DateTypeHandler();
            registry.register(Date.class, handler);
            registry.register(Date.class, JdbcType.TIMESTAMP, handler);
            registry.register(Date.class, JdbcType.DATE, handler);
            LOGGER.info("[MybatisDateTypeHandlerConfig] Date -> DateTypeHandler globally registered on SqlSessionFactory '{}'", beanName);
        }
        return bean;
    }
}
