package cn.vincent.taste.spicy.helper.error.i18n;

import cn.vincent.taste.spicy.helper.data.Result;
import cn.vincent.taste.spicy.helper.data.ResultHelper;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author vincent
 * @version 1.0 2020/2/26 15:41
 */
@Getter
@ToString
public class ErrorCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 响应码 */
    private int reponseCode;
    /** 状态码 */
    private String code;
    /** 错误信息 */
    private String message;

    public ErrorCode(int reponseCode, String code, String message) {
        this.reponseCode = reponseCode;
        this.code = code;
        this.message = message;
    }

    public static Result<?> output(ErrorCode errorCode) {
        return ResultHelper.fail(errorCode.code, errorCode.message);
    }
}
