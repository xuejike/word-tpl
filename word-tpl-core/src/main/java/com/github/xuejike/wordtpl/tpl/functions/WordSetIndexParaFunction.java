package com.github.xuejike.wordtpl.tpl.functions;

import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.WordTplFunctionBody;

import java.util.Map;

public class WordSetIndexParaFunction implements WordTplFunction {
    protected String name;
    protected String currentPTag;

    public WordSetIndexParaFunction(String currentPTag, String name) {
        this.currentPTag = currentPTag;
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void invoke(WordTpEnvironment environment, Map param, WordTplFunctionBody body) {
        Object index = param.get("index");
        if (index != null){
            environment.setTplVar(currentPTag,index);
        }

    }
}
