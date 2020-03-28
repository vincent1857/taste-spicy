package cn.vincent.taste.spicy.helper.exception;

import cn.vincent.taste.spicy.helper.error.code.ErrorCodeEnum;
import cn.vincent.taste.spicy.helper.error.code.ErrorCodeEnumItem;
import cn.vincent.taste.spicy.helper.error.system.SystemErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * @author vincent
 * @version 1.0 2020/2/26 14:57
 */
@Setter
@ToString
@AllArgsConstructor
public class ExceptionDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private int reponseCode;
    private String code;
    private String message;
    private Serializable[] args;

    public ExceptionDetail() {
        this.code = SystemErrorCodes.SYSTEM_ERROR.detail().getCode();
        this.message = SystemErrorCodes.SYSTEM_ERROR.detail().getMessage();
        this.reponseCode = SystemErrorCodes.SYSTEM_ERROR.detail().getReponseCode();
    }

    public int getReponseCode() {
        return reponseCode;
    }

    public String getCode() {
        return code;
    }

    public Serializable[] getArgs() {
        return args;
    }

    public String getMessage() {
        if (StringUtils.isBlank(this.message)) {
            return null;
        } else if (this.args == null || this.args.length == 0) {
            return this.message;
        }
        return MessageFormat.format(this.message, Arrays.asList(this.args).toArray(new Object[this.args.length]));
    }

    public static ExceptionDetail create(ErrorCodeEnum error, Serializable... args) {
        return create(error.detail(), args);
    }

    public static ExceptionDetail create(ErrorCodeEnumItem errorItem, Serializable... args) {
        ExceptionDetail detail = new ExceptionDetail();
        detail.setReponseCode(errorItem.getReponseCode());
        detail.setCode(errorItem.getCode());
        detail.setMessage(errorItem.getMessage());
        detail.setArgs(args);
        return detail;
    }
}
