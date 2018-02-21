package com.vezixtor.ontormi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vezixtor.ontormi.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ExceptionResolverUtils {

    public static void restResponse(ServletResponse response, String message, HttpStatus httpStatus) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus( httpStatus.value() );
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(toJson(new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message)));
        } catch (IOException e) {
            restResponse(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static String toJson(Object object) throws IOException {
        if (object == null) {
            return null;
        }
        return new ObjectMapper().writeValueAsString(object);
    }
}
