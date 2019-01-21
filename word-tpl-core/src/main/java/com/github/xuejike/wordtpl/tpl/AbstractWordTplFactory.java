package com.github.xuejike.wordtpl.tpl;

import com.github.xuejike.wordtpl.exception.TplBuildException;
import com.github.xuejike.wordtpl.parse.AbstractTokenParse;
import com.github.xuejike.wordtpl.parse.TokenParse;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author xuejike
 */
@Slf4j
public abstract class AbstractWordTplFactory {
    public AbstractWordTplFactory() {
        initTpl();
        loadFunction();
    }

    private void loadFunction() {
        ServiceLoader<WordTplFunction> serviceLoader = ServiceLoader.load(WordTplFunction.class);
        log.info("###### SPI Load Function #####");
        for (WordTplFunction tplFunction : serviceLoader) {
            registerFunction(tplFunction);
            log.info("# {}",tplFunction.getName());
        }
        log.info("##############################");
    }

    protected abstract void initTpl();

    /**
     * 注册自定义函数
     * @param wordTplFunction
     */
    public abstract void registerFunction(WordTplFunction wordTplFunction);

    /**
     * 通过模板名称执行模板
     * @param tplName 模板名称
     * @param data 数据内容
     * @throws IOException
     * @throws TplBuildException
     */
    public abstract void execTplName(String tplName, Map<String, Object> data, OutputStream outFile) throws IOException, TplBuildException;
    public void execTplName(String tplName,Map<String,Object> data,String outFilePath) throws IOException {
        execTplName(tplName, data, new FileOutputStream(outFilePath));
    }
    public void execTplName(String tplName, Map<String,Object> data, File outFile) throws IOException {
        execTplName(tplName, data, new FileOutputStream(outFile));
    }
    public abstract void execTplScript(String script,Map<String,Object> data,OutputStream outFile) throws IOException, TplBuildException;
    public void execTplScript(String script,Map<String,Object> data,String outFilePath) throws IOException {
        execTplScript(script, data, new FileOutputStream(outFilePath));
    }
    public void execTplScript(String script,Map<String,Object> data,File outFile) throws IOException {
        execTplScript(script, data, new FileOutputStream(outFile));
    }
    /**
     * 添加模板
     * @param name 模板名称
     * @param tpl 模板内容
     */
    public abstract void addTpl(String name, String tpl);


    /**
     * 获取Token解析器
     * @return
     */
    public abstract TokenParse getTokenParse();
}
