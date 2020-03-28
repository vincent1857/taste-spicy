package cn.vincent.taste.spicy.helper.error.code;

/**
 * 业务异常编码
 *
 * @author vincent
 * @version 1.0 2020/2/26 15:35
 */
public interface BusinessErrorCodeEnum extends ErrorCodeEnum {

    /**
     * 原始异常信息
     *
     * @return 原始异常信息
     */
    ErrorCodeEnumItem origin();

    /**
     * 项目编码
     *
     * @return 项目编码
     */
    String subjectCode();

    /**
     * 获取详情
     *
     * @return 获取详情
     */
    @Override
    default ErrorCodeEnumItem detail() {
        return new ErrorCodeEnumItem(this.origin().getReponseCode(), this.subjectCode() + this.origin().getCode(), this.origin().getMessage());
    }
}
