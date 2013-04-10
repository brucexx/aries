package com.brucexx.aries.exception;

public class AriesException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = 1L;

    private String            code;

    public AriesException(String desc) {
        super(desc);
    }

    public AriesException(String code, String desc) {
        this.code = code;

    }

    public AriesException() {
        super();
    }

    public AriesException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * ����һ��<code>FinInfoException</code>����
     * 
     * @param message 
     */
    public AriesException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public String getSystemErrorCode() {
        return code;
    }

}
