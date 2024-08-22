package com.example.springboot.common.utils;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class LocaleUtil {

    public static Locale getWithoutLocationLocale() {
        return Locale.of(LocaleContextHolder.getLocale().getLanguage());
    }

}
