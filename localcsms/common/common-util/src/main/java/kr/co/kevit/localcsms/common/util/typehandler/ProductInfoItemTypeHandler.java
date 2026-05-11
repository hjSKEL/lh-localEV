/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.product.ProductInfoItemType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 2.
 */
public class ProductInfoItemTypeHandler extends BaseTypeHandler<ProductInfoItemType> {

    @Override
    public ProductInfoItemType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return ProductInfoItemType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public ProductInfoItemType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return ProductInfoItemType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public ProductInfoItemType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return ProductInfoItemType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ProductInfoItemType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}