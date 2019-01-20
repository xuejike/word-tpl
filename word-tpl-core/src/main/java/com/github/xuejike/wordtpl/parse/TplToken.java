package com.github.xuejike.wordtpl.parse;

/**
 * 模板文件关键字标识
 * @author xuejike
 */
public class TplToken {
    protected String begin;
    protected String end;

    protected boolean block;

    public TplToken(String begin, String end) {
        this.begin = begin;
        this.end = end;
    }

    public TplToken(String begin, String end, boolean block) {
        this.begin = begin;
        this.end = end;
        this.block = block;
    }

    public String getBegin() {
        return begin;
    }


    public String getEnd() {
        return end;
    }


    public boolean isBlock() {
        return block;
    }
}
