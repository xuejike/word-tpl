package com.github.xuejike.wordtpl.tpl.functions;

import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.WordTplFunctionBody;

import java.util.Map;

public class WordParaFunction extends WordSetIndexParaFunction  {

    public WordParaFunction() {
        super(WordTpEnvironment.CURRENT_P_TAG, "wordP");
    }


}
