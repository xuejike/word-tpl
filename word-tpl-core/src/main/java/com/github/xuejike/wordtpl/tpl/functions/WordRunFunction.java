package com.github.xuejike.wordtpl.tpl.functions;

import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.WordTplFunctionBody;

import java.util.Map;

public class WordRunFunction implements WordTplFunction {
    @Override
    public String getName() {
        return "wordRun";
    }

    @Override
    public void invoke(WordTpEnvironment environment, Map param, WordTplFunctionBody body) {
        Object index = param.get("index");
        if (index != null){
            environment.setTplVar(WordTpEnvironment.CURRENT_RUN_TAG,index);
        }
        try {
            if (body != null){
                body.exec();
            }

        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
}
