package com.github.xuejike.wordtpl.exception;

/**
 * @author xuejike
 */
public class WordTplException extends Exception {
    public WordTplException() {
    }

    public WordTplException(String message) {
        super(message);
    }

    public WordTplException(String message, Throwable cause) {
        super(message, cause);
    }

    public WordTplException(Throwable cause) {
        super(cause);
    }

    public WordTplException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
