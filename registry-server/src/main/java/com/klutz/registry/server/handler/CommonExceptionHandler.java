package com.klutz.registry.server.handler;

import com.klutz.registry.server.exception.ParamCheckException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * created on 2021/12/7
 * @author klutz
 */
@ControllerAdvice
@SuppressWarnings("rawtypes")
public class CommonExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ParamCheckException.class)
    public ResponseEntity<?> handleParamCheckException(ParamCheckException paramCheckException){
        Map<String,String> result = new HashMap<>();
        result.put("msg",paramCheckException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(result);
    }


}
