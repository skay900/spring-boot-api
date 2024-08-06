package com.example.springboot.service;

import java.util.Arrays;

public enum TestEnum {
    FIRST("AAA"),
    SECOND("BBB"),
    THIRD("없음");

    private final String name;

    TestEnum(String name) {
        this.name = name;
    }

    public static TestEnum getName(String name) {
        return Arrays.stream(values())
                .filter(item -> item.name.equals(name))
                .findFirst()
                .orElse(TestEnum.THIRD);
    }

    public static void main(String[] args) {
        System.out.println(TestEnum.getName("BBB"));
    }

}
