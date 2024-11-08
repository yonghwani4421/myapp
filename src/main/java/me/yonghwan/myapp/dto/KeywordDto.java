package me.yonghwan.myapp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class KeywordDto {
    private Long id;
    private String content;

    @JsonCreator
    @Builder
    public KeywordDto(@JsonProperty("id") Long id,@JsonProperty("content") String content) {
        this.id = id;
        this.content = content;
    }

}
