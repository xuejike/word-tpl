package com.github.xuejike.wordtpl.tpl.functions;

import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.WordTplFunctionBody;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordScriptFinishFunction implements WordTplFunction {
    @Override
    public String getName() {
        return "wordScriptFinish";
    }

    @Override
    public void invoke(WordTpEnvironment environment, Map param, WordTplFunctionBody body) {
        List list = environment.getWordItemList();
        HashMap<Integer, Object> execIndexMap = environment.getExecIndexMap();
        for (int i = 0; i < list.size(); i++) {
            Object o = execIndexMap.get(i);
            if (o == null){
                removeItem(list.get(i));
            }
        }
    }

    private void removeItem(Object item) {
        if (item instanceof XWPFRun){
            removeRunItem(((XWPFRun) item));
        }
    }

    private void removeRunItem(XWPFRun item) {

    }
}
