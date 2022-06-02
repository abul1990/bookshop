package com.tw.bootcamp.bookshop.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Error {
    private String name;
    private String message;
    private String type;
    public Error(String message){
        this(null,message,null);
    }
    public Error(String name,String message){
        this(name,message,null);
    }
}
