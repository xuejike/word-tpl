package com.github.xuejike.wordtpl.tpl;

import java.util.Map;

public interface WordTplFunction {

    String getName();
    void invoke(WordTpEnvironment environment,Map param,WordTplFunctionBody body);
}
