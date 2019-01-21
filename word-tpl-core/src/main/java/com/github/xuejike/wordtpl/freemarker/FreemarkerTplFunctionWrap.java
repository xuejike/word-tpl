package com.github.xuejike.wordtpl.freemarker;

import com.github.xuejike.wordtpl.exception.WordScriptExecException;
import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.WordTplFunctionBody;
import freemarker.core.Environment;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 *
 * @author xuejike
 */
@Slf4j
public class FreemarkerTplFunctionWrap implements TemplateDirectiveModel {
    private WordTplFunction tplFunction;

    public FreemarkerTplFunctionWrap(WordTplFunction tplFunction) {
        this.tplFunction = tplFunction;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {

        TemplateModel variable = env.getVariable(WordTpEnvironment.WORD_ENV_TAG);
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        DefaultObjectWrapper wrapper = builder.build();
        WordTpEnvironment wordEnv = null;
        if (variable == null){
            wordEnv = new FreemarkerWordEnvImpl(env);
            env.setVariable(WordTpEnvironment.WORD_ENV_TAG,wrapper.wrap(wordEnv));
        }else{

            Object unwrap = wrapper.unwrap(variable);
            wordEnv = ((WordTpEnvironment) unwrap);
        }


        if (body == null){
            tplFunction.invoke(wordEnv, params,null);
        }else{
            WordTpEnvironment finalWordEnv = wordEnv;
            tplFunction.invoke(wordEnv, params, () -> {
                StringWriter writer = new StringWriter();
                try {
                    body.render(writer);
                } catch (TemplateException e) {
                    throw new WordScriptExecException(e.getMessage(),e);
                }
                finalWordEnv.getTplResultWriter().write(writer.toString());
                return writer.toString();
            });
        }


    }
}
