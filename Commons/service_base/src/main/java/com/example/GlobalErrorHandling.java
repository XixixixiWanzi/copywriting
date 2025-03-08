package com.example;


import com.example.reback.R;
import com.example.reback.Reback;
import com.example.reback.ResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalErrorHandling {
    @ExceptionHandler(CWException.class)
    @ResponseBody
    public Reback error(CWException cwe){
        cwe.printStackTrace();
        return R.FAIL.setCode(cwe.getResult());
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public Reback error(BadCredentialsException ex){
        ex.printStackTrace();
        return R.FAIL.message(ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Reback handleUnknownException(Exception ex) {
        ex.printStackTrace();
        return R.FAIL.setCode(ResultCode.UNEXPECTED_ERROR);
    }
}
