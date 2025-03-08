package com.example.reback;

import java.util.HashMap;
import java.util.Map;

/*
    value：属性的简短描述。
    name：属性的名称。
    dataType：属性的数据类型。
    required：指定属性是否为必填项。默认为 false。
    example：为属性提供示例值。
    hidden：指定是否在文档中隐藏属性。默认为 false。
    allowEmptyValue：指定是否允许空值。默认为 false。
    notes：详细说明属性的额外信息。

@ApiModel 注解的常用参
    value：模型类的简短描述。
    description：模型类的详细描述。
*/

public class Reback {

    private Integer code;

    private String message;

    private Map<String , Object> data=new HashMap<String,Object>();

    Reback(){}
    public Integer getCode(){
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    protected Reback result(ResultCode result){
        this.message = result.getMessage();
        this.code = result.getCode();
        return this;
    }

    public Reback message(String message){
        this.message = this.message+"/"+message;
        return this;
    }

    public Reback data(String key,Object value){
        this.data.put(key, value);
        return  this;
    }

}

