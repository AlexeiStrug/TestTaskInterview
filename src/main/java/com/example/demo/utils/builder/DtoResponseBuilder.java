package com.example.demo.utils.builder;


import com.example.demo.utils.Messages;
import com.example.demo.utils.builder.enums.ResponseType;
import com.example.demo.utils.exception.TypeOfResponseTypeProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DtoResponseBuilder {

    private final Messages messages;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public CustomResponse buildResponse(ResponseType responseType, String message, Object object) {
        CustomResponse response = new CustomResponse();

        if (responseType.equals(ResponseType.WARNING) ||
                responseType.equals(ResponseType.SUCCESS) ||
                responseType.equals(ResponseType.ERROR)) {
            generateResponse(response, responseType, message, object);
        } else {
            throw new TypeOfResponseTypeProcessingException("Sorry! Next type of request -> " + responseType + " does not supported yet.");
        }
        return response;
    }

    private CustomResponse generateResponse(CustomResponse response, ResponseType responseType, String message, Object object) {
        response.setResponseType(responseType);
        if (message != null && !message.equals("")) response.setMessage(message);
        if (object != null && !object.equals("")) response.setResult(object);
        return response;
    }

}
