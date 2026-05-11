/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.util;

import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2020. 3. 6.
 */
public class StringDateUtils {

    public static String beautyDateFormat(String value) {
        //
        if (value == null || value.trim().length() != 8) {
            return StringConstants.BLANK;
        }
        String year = value.substring(0, 4);
        String month = value.substring(4, 6);
        String day = value.substring(6);
        return year + StringConstants.DASH + month + StringConstants.DASH + day;
    }

    public static String beautyTimeFormat(String value) {
        //
        if (value == null || value.trim().length() != 6) {
            return StringConstants.BLANK;
        }
        String hh = value.substring(0, 2);
        String dd = value.substring(2, 4);
        String ss = value.substring(4);
        return hh + StringConstants.COLON + dd + StringConstants.COLON + ss;
    }

    public static String phoneNoFormat(String value) {
        if (value == null) {
            return StringConstants.BLANK;
        }

        String temp = value;

        if (temp.length() == 8) {
            temp = value.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (temp.length() == 12) {
            temp = value.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        } else {
            temp = value.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
        }
        return temp;
    }
}
