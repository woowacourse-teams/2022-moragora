package com.woowacourse.moragora.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {

    PRESENT, TARDY;

    @JsonCreator
    public static Status fromEnum(String value) {
        for (Status status : Status.values()) {
            if (status.name().equals(value)) {
                return status;
            }
        }
        return null;
    }
}
