package com.example.reback;

public enum ResultCode {
    SUCCESS(2000, "操作成功"),
    FAIL(2001, "操作失败"),
    UNAUTHORIZED(401, "用户不存在"),
    NOT_FOUND(404, "资源不存在"),
    SERVER_ERROR(500, "服务器错误"),
    UNEXPECTED_ERROR(333,"未知错误"),
    PARAMETER_ERROR(501,"参数错误"),
    REQUEST_FAIL(503,"请求失败"),
    RESOURCE_EXPIRED(505,"资源失效");

    private final int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public ResultCode setMessage(String message){
        this.message = this.message + "//" +message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
