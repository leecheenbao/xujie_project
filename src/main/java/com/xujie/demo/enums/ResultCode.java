package com.xujie.demo.enums;

public enum ResultCode {

    SUCCESS(1, "SUCCESS"),
    FAIL(400, "無法進行訪問");


    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

