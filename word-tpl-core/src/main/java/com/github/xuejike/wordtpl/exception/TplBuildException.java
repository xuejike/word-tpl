package com.github.xuejike.wordtpl.exception;

/**
 * 模板构建异常
 * @author xuejike
 */
public class TplBuildException extends WordTplException {
    public TplBuildException() {
    }

    public TplBuildException(String message) {
        super(message);
    }

    public TplBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplBuildException(Throwable cause) {
        super(cause);
    }

    public TplBuildException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
