package me.yonghwan.myapp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

public enum Role {
    USER,ADMIN;

    @JsonCreator
    public static Role form(String val){
        for (Role value : Role.values()) {
            if (value.name().equals(val)){
                return value;
            }
        }
        return null;
    }
}
