package social.roo.enums;

import lombok.Getter;

/**
 * @author biezhi
 * @date 2017/10/13
 */
public enum ErrorCode {

    OPT_TOO_FAST(10000, "操作频率太快");

    @Getter
    private int code;
    @Getter
    private String msg;

    ErrorCode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
