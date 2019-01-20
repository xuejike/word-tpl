package com.github.xuejike.wordtpl.freemarker;

import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import freemarker.core.Environment;
import freemarker.template.*;

import java.util.Map;

public class FreemarkerWordEnvImpl extends WordTpEnvironment {
    protected Environment environment;


    public FreemarkerWordEnvImpl(Environment environment) {
        this.environment = environment;

    }

    @Override
    public Object getTplVar(String name) {
        try {
            TemplateModel variable = environment.getVariable(name);
            DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
            return builder.build().unwrap(variable);
        } catch (TemplateModelException e) {
            return null;
        }
    }

    @Override
    public void setTplVar(String name, Object val) {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        try {

            TemplateModel wrap = builder.build().wrap(val);

            environment.setVariable(name,wrap);
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
        //  map.put(name,val);
    }
}
