package net.wanji.business.exception;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/3 14:38
 * @Descriptoin: 业务异常
 */

public class BusinessException extends Exception {

    public BusinessException(String message) {
        super(message);
    }
}
