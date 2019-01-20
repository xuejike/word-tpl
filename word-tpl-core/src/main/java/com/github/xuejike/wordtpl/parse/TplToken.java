package com.github.xuejike.wordtpl.parse;

/**
 * 模板文件关键字标识
 * @author xuejike
 */
public class TplToken {
    protected String begin;
    protected String end;

    public TplToken(String begin, String end) {
        this.begin = begin;
        this.end = end;
    }

    public String getBegin() {
        return begin;
    }


    public String getEnd() {
        return end;
    }


}
