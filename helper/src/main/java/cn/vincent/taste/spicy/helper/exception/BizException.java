package cn.vincent.taste.spicy.helper.exception;

/**
 * 统一异常接口
 *
 * @author vincent
 * @version 1.0 2020/2/26 14:57
 */
public interface BizException {

    /**
     * 获取异常详情
     *
     * @return 详情
     */
    ExceptionDetail getDetail();
}
