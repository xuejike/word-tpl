package com.github.xuejike.wordtpl.tpl.functions;

import com.github.xuejike.wordtpl.exception.WordScriptExecException;
import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.WordTplFunctionBody;
import com.github.xuejike.wordtpl.word.WordParse;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        HashMap<Object, Integer> delMap = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Object o = execIndexMap.get(i);
            Object item = list.get(i);
            if (o == null){
                // 不存在 执行列表
                if (item instanceof XWPFRun) {
                    //父节点 未被删除，讲该节点删除
                    delRunInfo(delMap,item);
                }else{
                    delMap.put(item,-1);
                }
            }else
            {
                //在执行列表中删除 被标记的run，并对父节点进行计数
                if (item instanceof XWPFRun){
                    String text = ((XWPFRun) item).getText(0);
                    if (WordParse.C_DEL_TAG.equals(text)){
                        delRunInfo(delMap, item);
                    }
                }
            }
        }
        //执行删除
        for (Map.Entry<Object, Integer> entry : delMap.entrySet()) {
            Object key = entry.getKey();
            if (entry.getValue() == -1){
                //必须移除的

                removeItemInWord(key,delMap);
            }else{
                //进行计数器判断
                if (key instanceof XWPFParagraph){
                    int size = ((XWPFParagraph) key).getRuns().size();
                    if (size <= entry.getValue()){
                        entry.setValue(-1);
                        removeItemInWord(key, delMap);
                    }
                }
            }
        }





        OutputStream wordOutput = environment.getWordOutput();
        XWPFDocument xwpfDocument = environment.getXwpfDocument();
        try {
            xwpfDocument.write(wordOutput);
        } catch (IOException e) {
            throw new WordScriptExecException(e.getMessage(),e);
        }
        try {
            wordOutput.close();
        } catch (IOException e) {
            throw new WordScriptExecException(e);
        }
        environment.execScriptFinish();
    }

    private void removeItemInWord(Object key, HashMap<Object, Integer> delMap) {
        if (key instanceof XWPFParagraph){
            IBody body = ((XWPFParagraph) key).getBody();
            if (body instanceof XWPFTableCell){
                int indexOf = body.getParagraphs().indexOf(key);
                ((XWPFTableCell) body).removeParagraph(indexOf);
            }else if (body instanceof XWPFDocument){
                int indexOf = body.getBodyElements().indexOf(key);
                ((XWPFDocument) body).removeBodyElement(indexOf);
            }else{
                int index = ((XWPFParagraph) key).getDocument().getParagraphs().indexOf(key);
                ((XWPFParagraph) key).getDocument().removeBodyElement(index);
            }

        }else if (key instanceof XWPFRun){
            XWPFParagraph parent = (XWPFParagraph) ((XWPFRun) key).getParent();
            Integer delCount = delMap.get(parent);
            if (delCount == null || delCount > -1){
                int index = parent.getRuns().indexOf(key);
                parent.removeRun(index);
            }

        }
    }

    private void delRunInfo(HashMap<Object, Integer> delMap, Object item) {
        IRunBody parent = ((XWPFRun) item).getParent();
        Integer delCount = delMap.get(parent);
        if (delCount == null) {
            delMap.put(parent, 1);
            delMap.put(item,-1);
        }else if (delCount != -1) {
            delCount++;
            delMap.put(parent,delCount);
            delMap.put(item,-1);
        }  //

    }


}
