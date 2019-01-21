package com.github.xuejike.wordtpl.tpl.functions;

import com.github.xuejike.wordtpl.exception.WordScriptExecException;
import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFunction;
import com.github.xuejike.wordtpl.tpl.WordTplFunctionBody;
import com.github.xuejike.wordtpl.word.WordParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 脚本执行的开始和结束
 * @author xuejike
 */
@Slf4j
public class WordLoadFunction implements WordTplFunction {
    @Override
    public String getName() {
        return "wordLoad";
    }

    @Override
    public void invoke(WordTpEnvironment environment, Map param, WordTplFunctionBody body) throws WordScriptExecException {
        Object path = param.get("path");
        if (path ==null){
            throw new WordScriptExecException("模板文件不存在,请重新生成脚本");
        }
        try {
            environment.execScriptStart();
            FileInputStream inputStream = new FileInputStream(String.valueOf(path));
            XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
            List<Object> itemList = WordParse.getWordAllItemList(xwpfDocument);
            environment.setEnvVar(WordTpEnvironment.WORD_ITEM_LIST_KEY,itemList);
            body.exec();
            OutputStream wordOutput = environment.getWordOutput();
            xwpfDocument.write(wordOutput);
            wordOutput.flush();
            environment.execScriptFinish();

        } catch (FileNotFoundException e) {
            throw new WordScriptExecException("模板文件不存在,请重新生成脚本");
        } catch (IOException e) {
            throw new WordScriptExecException(e.getMessage(),e);
        }
    }
}
