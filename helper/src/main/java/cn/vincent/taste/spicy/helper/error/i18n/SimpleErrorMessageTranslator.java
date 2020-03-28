package cn.vincent.taste.spicy.helper.error.i18n;

import cn.vincent.taste.spicy.helper.exception.ExceptionDetail;

/**
 * 简单信息翻译实现
 *
 * @author vincent
 * @version 1.0 2020/2/26 15:56
 */
public class SimpleErrorMessageTranslator implements ErrorMessageTranslator {

    @Override
    public ErrorCode translate(ExceptionDetail detail) {
        String code = detail.getCode();
        int reponseCode = detail.getReponseCode();
        String message = detail.getMessage();
        return new ErrorCode(reponseCode, code, message);
    }
}
