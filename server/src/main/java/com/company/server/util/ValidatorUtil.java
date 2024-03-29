package com.company.server.util;

import com.company.server.exception.ValidatorException;
import org.springframework.util.StringUtils;

public class ValidatorUtil {

    /**
     * 空校验（null or ""）
     */
    public static void require(Object str, String fieldName) {
        if (StringUtils.isEmpty(str)) {
            throw new ValidatorException(fieldName + "不能为空");
        }
    }

    /**
     * 长度校验
     */
    public static void length(String str, String fieldName, int min, int max) {
        if (StringUtils.isEmpty(str)) {
            return;
        }
        int length = str.length();
        if (length < min || length > max) {
            throw new ValidatorException(fieldName + "长度" + min + "~" + max + "位");
        }
    }
}
