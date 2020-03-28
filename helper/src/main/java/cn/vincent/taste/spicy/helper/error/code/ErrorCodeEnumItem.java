package cn.vincent.taste.spicy.helper.error.code;

import cn.vincent.taste.spicy.helper.exception.ExceptionDetail;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常信息
 *
 * @author vincent
 * @version 1.0 2020/2/26 14:40
 */
@Getter
@Setter
public class ErrorCodeEnumItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int reponseCode;
    private final String code;
    private final String message;

    public ErrorCodeEnumItem(final int reponseCode, final String code, final String message){
        this.reponseCode = reponseCode;
        this.code = code;
        this.message = message;
    }

    public ExceptionDetail toErrorCode(Serializable... args) {
        return ExceptionDetail.create(this, args);
    }

    public boolean eq(String code) {
        return this.getCode().equals(code);
    }
}