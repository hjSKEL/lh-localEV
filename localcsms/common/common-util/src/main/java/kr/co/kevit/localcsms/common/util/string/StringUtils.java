package kr.co.kevit.localcsms.common.util.string;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Operations on {@link String} that are {@code null} safe.</p>
 * <p>{@link org.apache.commons.lang3.StringUtils} 확장</p>
 *
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a> 
 * @since 2018. 5. 14.
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);
    
    public static boolean isEmpty(String value) {
        if( value == null) return true;
        return value.trim().length() == 0;
    }

    /**
     * 입력된 오브젝트가 널이면 빈문자열("")을 반환하고
     * 널이 아니면 오브젝트의 문자열의  trim한 결과를 반환한다.
     * <pre>
     * [null] --> [""]
     * [""] --> [""]
     * ["     "] --> [""]
     * [" aaa"] --> ["aaa"]
     * ["aaa "] --> ["aaa"]
     * [" aaa "] --> ["aaa"]
     * [" aa aaa "] --> ["aa aaa"]
     * [" aa aaa aa"] --> ["aa aaa aa"]
     * </pre>
     * @param obj 문자열로 변환할 오브젝트
     * @return 빈문자열("") 또는 trim된 문자열
     */
    public static String trimToEmpty(Object obj) {
        return obj == null ? EMPTY : trimToEmpty(String.valueOf(obj));
    }

    /**
     * 입력된 문자열이 널 또는 빈문자열("")이면 입력된 기본문자열을 반환하고
     * 그렇지않으면 입력된 문자열의 trim한 결과를 반환한다.
     * @param anObject 문자열로 변환할 오브젝트
     * @param defaultString 기본문자열
     * @return 기본문자열 또는 trim된 문자열
     */
    public static String trimToDefault(String str, String defaultStr) {
        String ts = trim(str);
        return isEmpty(ts) ? defaultStr : ts;
    }

    /**
     * 입력된 오브젝트가 널 또는 빈문자열("")이면 입력된 기본문자열을 반환하고
     * 그렇지않으면 오브젝트의 문자열의  trim한 결과를 반환한다.
     * @param obj 문자열로 변환할 오브젝트
     * @param defaultStr 기본문자열
     * @return 기본문자열 또는 trim된 문자열
     */
    public static String trimToDefault(Object obj, String defaultStr) {
        return obj == null ? defaultStr : trimToDefault(String.valueOf(obj), defaultStr);
    }

    /**
     * 문자열을 치환한다.
     * @param srcString 원본문자열
     * @param oldString 변경할 문자열
     * @param newString 대체할 문자열
     * @return 대체된 문자열
     */
    public static String replace(String srcString, String oldString, String newString) {
        if (srcString == null) {
            return null;
        }
        StringBuilder destStr = new StringBuilder(srcString.length());
        int len = oldString.length();
        int srclen = srcString.length();
        int pos = 0;
        int oldpos = 0;

        while ((pos = srcString.indexOf(oldString, oldpos)) >= 0) {
            destStr.append(srcString.substring(oldpos, pos));
            destStr.append(newString);
            oldpos = pos + len;
        }

        if (oldpos < srclen) {
            destStr.append(srcString.substring(oldpos, srclen));
        }
        return destStr.toString();
    }

    public static String findRandomString(int length) {
        //
        int index = 0;
        char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
                'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '!' , '@', '#', '$', '%', '^', '&', '*', '(', ')'};

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            index = (int) (charArr.length * Math.random());
            sb.append(charArr[index]);
        }
        return sb.toString();
    }

    /**
     * 숫자로 구성된 문자열 체크
     * @param str
     * @return 숫자 문자열 여부
     */
    public static boolean isDigit(String str) {
        return isNumeric(str);
    }

    /**
     * 원본문자열의 trim된 문자열의 길이보다 지정된 길이가 클 경우 부족한 만큼을 제공된 문자로
     * 왼쪽에서부터 채워진 문자열을 반환한다.
     * <p>
     * <pre>
     * [null, 'A', 3] --> ["AAA"]
     * ["1", 'A', 3] --> ["AA1"]
     * ["12", 'A', 3] --> ["A12"]
     * ["123", 'A', 3] --> ["123"]
     * ["1234", 'A', 3] --> ["1234"]
     * ["1234  ", 'A', 3] --> ["1234"]
     *
     * ["12 ", 'A', 3] --> ["A12"]
     * [" 12", 'A', 3] --> ["A12"]
     * </pre>
     *
     * @param srcStr 원본문자열
     * @param paddingChar 채울문자
     * @param length 문자가 채워진 후의 문자열 길이
     * @return 제공된 문자로 채워진 문자열
     */
    public static String leftPadding(String srcStr, char paddingChar, int length) {
        String result = (srcStr == null) ? "" : srcStr.trim();
        String resultString = srcStr;

        // 원본문자열보다 클 경우에만 패딩을 함.
        if (length > result.length()) {
            StringBuffer buff = new StringBuffer(length);
            int paddingSize = length - result.length();
            for (int i = 0; i < paddingSize; i++) {
                buff.append(paddingChar);
            }
            resultString = buff.append(result).toString();
        }

        return resultString;
    }
    
    /**
     * 원본문자열의 trim된 문자열의 길이보다 지정된 길이가 클 경우 부족한 만큼을 제공된 문자로
     * 오른쪽에서부터 채워진 문자열을 반환한다.
     * <p>
     * <pre>
     * [null, 'A', 3] --> ["AAA"]
     * ["1", 'A', 3] --> ["1AA"]
     * ["12", 'A', 3] --> ["12A"]
     * ["123", 'A', 3] --> ["123"]
     * ["1234", 'A', 3] --> ["1234"]
     * ["1234  ", 'A', 3] --> ["1234"]
     *
     * ["12 ", 'A', 3] --> ["12A"]
     * [" 12", 'A', 3] --> ["12A"]
     * </pre>
     *
     * @param srcStr 원본문자열
     * @param paddingChar 채울문자
     * @param length 문자가 채워진 후의 문자열 길이
     * @return 제공된 문자로 채워진 문자열
     */
    public static String rightPadding(String srcStr, char paddingChar, int length) {
        String result = (srcStr == null) ? "" : srcStr.trim();
        String resultString = srcStr;

        // 원본문자열보다 클 경우에만 패딩을 함.
        if (length > result.length()) {
            StringBuffer buff = new StringBuffer(length);
            buff.append(result);
            int paddingSize = length - result.length();
            for (int i = 0; i < paddingSize; i++) {
                buff.append(paddingChar);
            }
            resultString = buff.toString();
        }

        return resultString;
    }
    
    /**
     * 입력된 값을 지정된 글자수만큼 왼쪽에서부터 0을 채워서 반환
     */
    /**
     * 원본문자열의 trim된 문자열의 길이보다 지정된 길이가 클 경우 부족한 만큼을 '0'으로
     * 왼쪽에서부터 채워진 문자열을 반환한다.
     * <p>
     * <pre>
     * [null, 3] --> ["000"]
     * ["1", 3] --> ["002"]
     * ["12", 3] --> ["012"]
     * ["123", 3] --> ["123"]
     * ["1234", 3] --> ["1234"]
     * ["1234  ", 3] --> ["1234"]
     *
     * ["12 ", 3] --> ["012"]
     * [" 12", 3] --> ["012"]
     * </pre>
     *
     * @param srcStr 원본문자열
     * @param length '0'이 후의 문자열 길이
     * @return '0'으로 채워진 문자열
     */
    public static String leftZeroPadding(String srcStr, int length) {
        return leftPadding(srcStr, '0', length);
    }

    /**
     * 이메일의 유효성(정합성)을 체크한다.
     *
     * @param email
     * @return 유효한 이메일인 경우 true 아니면 false를 리턴
     */
    public static boolean checkEmail(String email) {
        if (StringUtils.isEmpty(email)) return false;
        boolean value = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
        return value;
    }

    /**
     * URLEncoder를 사용하여 주어진 값을 UTF-8으로 URL 인코딩 한다.
     *
     *
     * @param value URL 인코딩할 값
     * @return
     */
    public static String encodeURL(String value) {
        //
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.debug("UnsupportedEncodingException", e);
            throw new RuntimeException("Error to encode value --> " + value);
        }
    }

    /**
     * 문자열 배열의 요소에 대하여, wrapWith 로 감싼 후,
     * 그 결과를 joinSperator로 구분하여 리턴한다.
     *
     * [{"Apple", "Banana"}, "'", ","] --> ["'Apple', 'Banana'"]
     *
     * @param words
     * @param wrapWith
     * @param joinSperator
     * @return
     */
    public static String wrapAndJoin(String[] words, String wrapWith, String joinSperator) {
        //
        if (words == null || words.length == 0) {
            return "";
        }

        for (int i = 0; i < words.length; i++) {
            words[i] = wrap(words[i], wrapWith);
        }

        return join(words, joinSperator);
    }

    /**
     * 문자열 배열의 요소에 대하여, wrapWith 로 감싼 후,
     * 그 결과를 joinSperator로 구분하여 리턴한다.
     *
     * [{"Apple", "Banana"}, "'", ","] --> ["'Apple', 'Banana'"]
     *
     * @param words
     * @param wrapWith
     * @param joinSperator
     * @return
     */
    public static String wrapAndJoin(List<String> words, String wrapWith, String joinSperator) {
        //
        if (words == null || words.isEmpty()) {
            return "";
        }

        String[] wordsArray = new String[words.size()];
        wordsArray = words.toArray(wordsArray);

        return wrapAndJoin(wordsArray, wrapWith, joinSperator);
    }

    /**
     * javascript encodeURIComponent 형태로 변경
     *
     * @param component 인코딩될 문자열
     * @return 인코딩 문자열
     */
    public static String encodeURIComponent(String component) {
        String result;

        try {
            result = URLEncoder.encode(component, "UTF-8")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%7E", "~");

        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Exception caught.", e);
            result = component;
        }

        return result;
    }

    public static String concat(String ... values) {
        //
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            if (value != null) {
                builder.append(value);
            }
        }

        return builder.toString();
    }

    public static String format(String text, Object ... values) {
        //
        String format = text;
        if (values != null && values.length > 0) {
            for (Object value : values) {
                format = StringUtils.replaceOnce(format, "{}", value != null ? value.toString() : "null");
            }
        }

        return format;
    }

    public static String toUpperCase(String text) {
        //
        if (text != null && text.length() > 0) {
            return text.toUpperCase();
        }
        return text;
    }
    
    /**
     * 
     * @param value
     * @return
     */
    public static String applyComma(double value) {
        //
        DecimalFormat df = new DecimalFormat("#,###.#");
        return df.format(value);
    }

    public static String rightZeroPadding(String srcStr, int length) {
        return rightPadding(srcStr, '0', length);
    }
}
