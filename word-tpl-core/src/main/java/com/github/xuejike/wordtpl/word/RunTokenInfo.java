package com.github.xuejike.wordtpl.word;

import com.github.xuejike.wordtpl.parse.TplToken;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class RunTokenInfo {
    protected TplToken tplToken;
    protected int beginIndex;
    protected int endIndex;
    protected XWPFRun xwpfRun;


    public XWPFRun getXwpfRun() {
        return xwpfRun;
    }

    public void setXwpfRun(XWPFRun xwpfRun) {
        this.xwpfRun = xwpfRun;
    }

    public TplToken getTplToken() {
        return tplToken;
    }

    public void setTplToken(TplToken tplToken) {
        this.tplToken = tplToken;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
