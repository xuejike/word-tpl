package com.github.xuejike.wordtpl.tpl;

import com.github.xuejike.wordtpl.exception.TplBuildException;
import com.github.xuejike.wordtpl.freemarker.FreemarkerTplFunctionWrap;
import com.github.xuejike.wordtpl.tpl.functions.*;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author xuejike
 */
public class WordTplFactory {

    protected StringTemplateLoader tplLoader;
    protected Configuration cfg;

    public WordTplFactory() {
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        tplLoader = new StringTemplateLoader();
        cfg.setTemplateLoader(tplLoader);
        WordParaFunction wordParaFunction = new WordParaFunction();
        WordRunFunction wordRunFunction = new WordRunFunction();
        registerFunction(wordParaFunction);
        registerFunction(wordRunFunction);
        registerFunction(new WordTableCellFunction());
        registerFunction(new WordTableFunction());
        registerFunction(new WordTableRowFunction());
        registerFunction(new WordScriptFinishFunction());
    }

    public void registerFunction(WordTplFunction wordTplFunction){
        cfg.setSharedVariable(wordTplFunction.getName(),new FreemarkerTplFunctionWrap(wordTplFunction));

    }
    public String buildTpl(String tplName,Map<String,Object> data) throws IOException, TplBuildException {
        Template template = cfg.getTemplate(tplName);
        StringWriter writer = new StringWriter();
        try {
            template.process(data,writer);
            return writer.toString();
        } catch (TemplateException e) {
            throw new TplBuildException("模板构建失败",e);
        }
    }
    public void addTpl(String name,String tpl){
        tplLoader.putTemplate(name, tpl);
    }
}
