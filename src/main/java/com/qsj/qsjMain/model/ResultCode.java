package com.qsj.qsjMain.model;

/**
 * API 接口返回状态码
 *
 */
public enum ResultCode {

    SUCCESS(200, "成功"),

    /** 300-399是客户端需要做些事 */
    LOGIN_REQUIRED(302, "需要重新登录"),

    /** 400-499是客户端错误 */
    BAD_REQUEST(400, "非法请求"), //
    UNAUTHORIZED(401, "未授权，请联系管理员"), //
    FORBIDDEN(403, "该页面禁止访问"), //
    NOT_FOUND(404, "页面未找到"), //
    LOGIN_FAIL(410, "登录失败"), //
    PARAM_REQUIRED(411, "缺少必须参数"), //
    PARAM_VALIDATION_FAILURE(412, "参数校验失败"), //
    OPERATION_EXPIRED(413, "超时提交"), //
    OPERATION_DUPLICATED(414, "重复提交"), //
    OPERATION_UNSUPPORTED(415, "不支持的操作"), //

    /** 500-599是服务端错误 */
    SERVICE_BUSY(500, "内部系统繁忙"), //
    SERVICE_RATE_EXCEEDED(520, "请求超限"),

    /** 600-699 外部系统异常 */
    RELY_SERVICE_BUSY(600, "外部系统繁忙"),
    ;

    private final int code;
    private final String message;

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
