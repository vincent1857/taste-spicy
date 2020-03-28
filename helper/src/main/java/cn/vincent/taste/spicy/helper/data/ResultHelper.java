package cn.vincent.taste.spicy.helper.data;

import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;

/**
 * @author vincent
 * @version 1.0 2020/2/26 15:45
 */
public class ResultHelper {

    private ResultHelper() { }

    public static boolean isSuccess(Result<?> result) {
        if (result == null) {
            return false;
        }

        return SystemErrorCodes.SUCCESS.detail().getCode().equals(result.getCode());
    }

    public static Result<?> fail(String code, String message) {
        Result<?> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
