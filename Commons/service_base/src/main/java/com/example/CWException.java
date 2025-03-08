package com.example;


import com.example.reback.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CWException extends RuntimeException{
    private ResultCode result;
    public CWException setMessage(String meg){
        result.setMessage(meg);
        return this;
    }
}
