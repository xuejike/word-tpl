package com.github.xuejike.wordtpl;

import com.github.xuejike.wordtpl.tpl.AbstractWordTplFactory;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.word.WordParse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * word 模板操作工具类
 * @author xuejike
 */
@Slf4j
public class WordTplUtils {

    private AbstractWordTplFactory wordTplFactory;
    protected WordParse wordParse;

    public WordTplUtils(AbstractWordTplFactory wordTplFactory) {
        this.wordTplFactory = wordTplFactory;
        wordParse = new WordParse(wordTplFactory.getTokenParse());

    }

    public String buildTplScript(String wordPath) throws IOException {
        return wordParse.word2TplScript(wordPath).toString();
    }
    public void addTplScript(String name,String tplScript){
        wordTplFactory.addTpl(name,tplScript);
    }
    public void buildWord(File tplWordFile, String outWordPath, Map<String,Object> data) throws IOException {
        String script = wordParse.word2TplScript(tplWordFile);
        wordTplFactory.execTplScript(script,data,outWordPath);
    }
    public void buildWord(String scriptName,String outWordPath, Map<String,Object> data) throws IOException {
        wordTplFactory.execTplName(scriptName,data,outWordPath);
    }

}
