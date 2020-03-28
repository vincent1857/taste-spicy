package cn.vincent.taste.spicy.helper.error.system;

import cn.vincent.taste.spicy.helper.error.code.ErrorCodeEnumItem;
import cn.vincent.taste.spicy.helper.error.code.SystemErrorCodeEnum;

/**
 * 全局系统异常
 *
 * @author vincent
 * @version 1.0 2020/2/26 15:06
 */
public enum SystemErrorCodes implements SystemErrorCodeEnum {

    // 成功
    SUCCESS("0", "成功", 200),
    SYSTEM_ERROR("100001", "系统异常"),

    UPLOAD_FILE_TOO_LARGE("100010", "文件上传过大"),
    INVALID_PARAM_FIELD_ERROR("100011", "参数{0}不正确"),
    INVALID_PARAM_NOT_NULL("100012", "{0}不能为空"),
    INVALID_PARAM_ERROR("100013", "参数错误"),

    INVALID_LOGIN_STATUS("100020", "检测到您的登录状态发生变化，请重新登录"),
    INVALID_REDIRECT_URL("100021", "无效的重定向链接"),

    RPC_INNER_SERVER_EXCEPTION("100030", "内部服务异常"),
    RPC_INNER_SERVER_FAIL("100031", "内部服务连接失败"),
    RPC_THIRD_SERVER_EXCEPTION("100032", "外部服务异常"),
    RPC_THIRD_SERVER_FAIL("100033", "外部服务连接失败"),
    ;

    private final ErrorCodeEnumItem item;

    SystemErrorCodes(final String code, final String message) {
        this.item = new ErrorCodeEnumItem(200, code, message);
    }

    SystemErrorCodes(final String code, final String message, final int reponseCode) {
        this.item = new ErrorCodeEnumItem(reponseCode, code, message);
    }

    @Override
    public ErrorCodeEnumItem detail() {
        return this.item;
    }
}
