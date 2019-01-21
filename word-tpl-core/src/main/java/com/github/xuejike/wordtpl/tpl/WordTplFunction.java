package com.github.xuejike.wordtpl.tpl;

import com.github.xuejike.wordtpl.exception.WordScriptExecException;

import java.util.Map;

public interface WordTplFunction {

    String getName();
    void invoke(WordTpEnvironment environment,Map param,WordTplFunctionBody body) throws WordScriptExecException;
}
