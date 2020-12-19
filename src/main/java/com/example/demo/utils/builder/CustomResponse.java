package com.example.demo.utils.builder;


import com.example.demo.utils.builder.enums.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomResponse {

    private ResponseType responseType;
    private String message;
    private Object result;

}
