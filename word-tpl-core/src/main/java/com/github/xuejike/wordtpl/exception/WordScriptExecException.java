package com.github.xuejike.wordtpl.exception;

/**
 * @author xuejike
 */
public class WordScriptExecException extends WordTplException {
    public WordScriptExecException() {
    }

    public WordScriptExecException(String message) {
        super(message);
    }

    public WordScriptExecException(String message, Throwable cause) {
        super(message, cause);
    }

    public WordScriptExecException(Throwable cause) {
        super(cause);
    }

    public WordScriptExecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
