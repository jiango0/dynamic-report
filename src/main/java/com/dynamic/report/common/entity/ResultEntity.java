package com.dynamic.report.common.entity;

import java.io.Serializable;

public class ResultEntity<T> implements Serializable {

    private String code = "200";

    private String msg = "success";

    private String errorMsg;

    private T data;

    public ResultEntity() {}

    public ResultEntity(String code, String msg, String errorMsg, T data) {
        this.code = code;
        this.msg = msg;
        this.errorMsg = errorMsg;
        this.data = data;

    }

    public static <T> ResultEntity<T> returnSuccess(T t) {
        return new ResultEntity<>("200", "success", null, t);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
