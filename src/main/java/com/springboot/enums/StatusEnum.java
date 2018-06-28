package com.springboot.enums;

@SuppressWarnings("all")
public enum StatusEnum {

    SUCCESS_200(200, "请求成功"),
    FAILURE_201(201, "新资源已经被创建"),
    FAILURE_204(204, "资源删除成功"),
    FAILURE_304(304, "没有变化,客户端可以使用缓存数据"),
    FAILURE_400(400, "Bad Request调用不合法"),
    FAILURE_401(401, "未认证,调用需要用户通过认证"),
    FAILURE_403(403, "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
    FAILURE_404(404, "未找到,指定的资源不存在"),
    FAILURE_422(422, "不可指定的请求体,只有服务器不能处理实体时使用,比如图像不能被格式化,或者重要字段丢失。"),
    ERROR_500(500,"Internal Server Error标准服务端错误，API开发人员应该尽量避开这种错误"),
    SUCCESS_666(666, "业务成功"),
    FAILURE_999(999, "业务失败");

    private StatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    private final Integer code;
    private final String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
