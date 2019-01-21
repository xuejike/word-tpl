package com.github.xuejike.wordtpl.tpl;

import com.github.xuejike.wordtpl.exception.WordScriptExecException;
import freemarker.template.TemplateException;

import java.io.IOException;

public interface WordTplFunctionBody {
    String exec() throws WordScriptExecException, IOException;
}
