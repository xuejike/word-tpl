package com.github.xuejike.wordtpl.freemarker;

import com.github.xuejike.wordtpl.exception.TplBuildException;
import com.github.xuejike.wordtpl.parse.AbstractTokenParse;
import com.github.xuejike.wordtpl.tpl.AbstractWordTplFactory;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.functions.*;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

/**
 * @author xuejike
 */
public class FreemarkerWordTplFactory extends AbstractWordTplFactory {

    protected StringTemplateLoader tplLoader;
    protected Configuration cfg;

    @Override
    protected void initTpl() {
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        tplLoader = new StringTemplateLoader();
        cfg.setTemplateLoader(tplLoader);

    }

    @Override
    public void registerFunction(WordTplFunction wordTplFunction){
        cfg.setSharedVariable(wordTplFunction.getName(),new FreemarkerTplFunctionWrap(wordTplFunction));

    }
    @Override
    public void execTplName(String tplName, Map<String,Object> data, OutputStream outFile) throws IOException, TplBuildException {
        Template template = cfg.getTemplate(tplName);
        StringWriter writer = new StringWriter();
        try {
            data.put(FreemarkerWordEnvImpl.OUTPUT_FILE,outFile);
            template.process(data,writer);
        } catch (TemplateException e) {
            throw new TplBuildException("模板构建失败",e);
        }
    }

    @Override
    public void execTplScript(String script, Map<String, Object> data, OutputStream outFile) throws IOException, TplBuildException {
        String tplName = UUID.randomUUID().toString();
        tplLoader.putTemplate(tplName,script);
        execTplName(tplName, data,outFile);
        tplLoader.removeTemplate(tplName);
        cfg.removeTemplateFromCache(tplName);

    }

    @Override
    public void addTpl(String name, String tpl){
        tplLoader.putTemplate(name, tpl);
    }
    protected AbstractTokenParse tokenParse = new FreemarkerTokenParse();
    @Override
    public AbstractTokenParse getTokenParse() {
        return tokenParse;
    }
}
