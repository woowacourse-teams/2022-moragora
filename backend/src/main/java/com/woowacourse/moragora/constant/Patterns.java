package com.woowacourse.moragora.constant;

public class Patterns {

    public static final String EMAIL = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$";
    public static final String NICKNAME = "[a-zA-Z0-9가-힣]{1,15}";
    public static final String DATE = "yyyy-MM-dd";
    public static final String TIME = "'T'HH:mm";
}
