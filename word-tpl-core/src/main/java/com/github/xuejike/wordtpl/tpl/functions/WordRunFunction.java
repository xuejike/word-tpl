package com.github.xuejike.wordtpl.tpl.functions;

import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.WordTplFunctionBody;
import com.github.xuejike.wordtpl.word.WordParse;
import org.apache.poi.xwpf.usermodel.XWPFRun;

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
            environment.setCurrentIndex(WordTpEnvironment.CURRENT_RUN_TAG,index);
        }
        try {
            if (body != null){
                String exec = body.exec();
                XWPFRun currentRun = environment.getCurrentRun();
               if (!WordParse.checkHaveTag(exec)){
                   currentRun.setText(exec,0);
               }
            }

        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
}
