package cn.vincent.taste.spicy.helper.error.i18n;

import cn.vincent.taste.spicy.helper.exception.ExceptionDetail;

/**
 * 信息转换器
 *
 * @author vincent
 * @version 1.0 2020/2/26 15:55
 */
public interface ErrorMessageTranslator {

    /**
     * 翻译
     *
     * @param detail 错误信息对象
     * @return 翻译后信息
     */
    ErrorCode translate(ExceptionDetail detail);
}
