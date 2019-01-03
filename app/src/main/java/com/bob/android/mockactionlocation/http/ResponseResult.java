package com.bob.android.mockactionlocation.http;

/**
 * @package com.bob.android.mockactionlocation.http
 * @fileName ResponseResult
 * @Author Bob on 2019/1/3 9:44.
 * @Describe TODO
 */
public class ResponseResult {

    //请求成功
    public static String SUCCEED_CODE = "100";
    //用户未登录或者登录信息已过期
    public static String IDENTITY_ERROR_CODE = "101";
    //用户登录异常
    public static String PASSWORD_ERROR_CODE = "104";
    //业务异常
    public static String SERVER_ERROR_CODE = "105";
    //非法请求
    public static String PARAMS_ERROR_CODE = "106";

    public static String VERIFY_ERROR_CODE = "3001";

    public static String DATA_EMPTY_CODE = "4001";
    public static String LOAD_NO_MORE = "6001";


    private String responseCode;
    private String message;
    private String content;
    private String checkCode;

    public ResponseResult() {
    }

    public ResponseResult(String responseCode, String message, String content) {
        this.responseCode = responseCode;
        this.message = message;
        this.content = content;
    }

    public ResponseResult(String responseCode, String content) {
        this.responseCode = responseCode;
        this.content = content;
    }

    public String getCheckCode() {
        return checkCode == null ? "" : checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
