package com.situ.jurisdiction.util;

import lombok.Data;

@Data
public class R {
    //状态码 10000-成功 10001-失败
    private Integer code;
    //返回的附件信息
    private String msg;
    //返回的数据
    private Object data;

    public static R ok() {
        R r = new R();
        r.setCode(10000);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.setCode(10000);
        r.setMsg(msg);
        return r;
    }

    public static R ok(Object data) {
        R r = new R();
        r.setCode(10000);
        r.setData(data);
        return r;
    }

    public static R ok(String msg, Object data) {
        R r = new R();
        r.setCode(10000);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static R error() {
        R r = new R();
        r.setCode(10001);
        return r;
    }

    public static R error(String msg) {
        R r = new R();
        r.setCode(10001);
        r.setMsg(msg);
        return r;
    }

    public static R error(String msg, Object data) {
        R r = new R();
        r.setCode(10001);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
