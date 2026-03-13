package com.example.demo.common; // 包名必须和你创建的一致

// 通用统一返回结果类，T表示返回数据的泛型（支持任意数据类型：String/List/Student等）
public class Result<T> {
    // 状态码：200=成功，400=参数错误，500=业务失败
    private Integer code;
    // 提示信息（前端展示用）
    private String message;
    // 返回的数据（比如新增成功的提示、分页查询的列表等）
    private T data;

    // 私有构造器：禁止外部直接new，只能用静态方法创建
    private Result() {}

    // ========== 静态工具方法：快速创建返回结果 ==========
    // 1. 成功（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 2. 成功（不带数据，比如删除成功）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 3. 失败（自定义提示信息）
    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    // 4. 失败（自定义状态码+提示信息，比如参数校验失败用400）
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    // ========== getter/setter：必须完整（SpringJSON序列化需要） ==========
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}