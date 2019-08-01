package com.hd.message.util;

import org.springframework.cglib.beans.BeanCopier;

/**
 * cglib工具类
 */
public class CglibUtil {

    public static void copyObject(Object source, Object target) {
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }
}
