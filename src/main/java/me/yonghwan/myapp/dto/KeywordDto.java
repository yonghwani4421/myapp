package me.yonghwan.myapp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class KeywordDto {
    private String content;

    @JsonCreator
    @Builder
    public KeywordDto(@JsonProperty("content") String content) {
        this.content = content;
    }

}
