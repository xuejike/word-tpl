package com.github.xuejike.wordtpl.word;

import com.github.xuejike.wordtpl.exception.TplBuildException;
import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.WordTplFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuejike
 */
@Slf4j
public class WordBuild {
     WordTplFactory wordTplFactory;

    public WordBuild(WordTplFactory wordTplFactory) {
        this.wordTplFactory = wordTplFactory;
    }
    public XWPFDocument execScript(String tplFile, String tplScript, Map<String,Object> data) throws IOException, TplBuildException {

        InputStream fileInputStream =new FileInputStream(new File(tplFile));
        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
        List<Object> itemList = WordParse.getWordAllItemList(xwpfDocument);
        data.put(WordTpEnvironment.WORD_ITEM_LIST_KEY,itemList);

        wordTplFactory.addTpl(tplFile,tplScript);
        String tpl = wordTplFactory.buildTpl(tplFile, data);
        //剔除未执行的run

        return xwpfDocument;
    }


    //    public void buildDoc(String runsTxt,String tplWordFile,String outFile) throws IOException {
//        InputStream fileInputStream =new FileInputStream(new File(tplWordFile));
//        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
//        ArrayList<XWPFRun> list = WordParse.getWordRuns(xwpfDocument);
//        HashMap<Integer, XWPFRun> map = new HashMap<>();
//        parseRunsText(runsTxt,(index,txt)->{
//            boolean tag = WordParse.checkHaveTag(txt);
//            map.put(index,list.get(index));
//            if (!tag){
//                list.get(index).setText(txt,0);
//            }
//        });
//        for (int i = 0; i < list.size(); i++) {
//            XWPFRun xwpfRun = list.get(i);
//            if (map.get(i) == null){
//                XWPFParagraph parent = (XWPFParagraph) xwpfRun.getParent();
//                if (parent.getRuns().size() > 1){
//                    int runIndex = parent.getRuns().indexOf(xwpfRun);
//                    parent.removeRun(runIndex);
//                }else{
//                    int index = parent.getDocument().getBodyElements().indexOf(parent);
//                    parent.getDocument().removeBodyElement(index);
//                }
//            }
//        }
//        FileOutputStream stream = new FileOutputStream(new File(outFile));
//        xwpfDocument.write(stream);
//
//    }
//
//    private void parseRunsText(String runsTxt,ParseRunsTextCallback parseRunsTextCallback) {
//        String[] rows = runsTxt.split("\n");
//        for (String row : rows) {
//            int sp = row.indexOf(':');
//            if (sp >0 ){
//                String indexStr = row.substring(0, sp);
//                try {
//                    int runIndex = Integer.parseInt(indexStr);
//                    String txt = row.substring(sp + 1);
//                    parseRunsTextCallback.run(runIndex,txt);
//                }catch (Exception ex){
//                    System.out.println("解析->"+row);
//                }
//
//            }
//        }
//
//    }
//
//

    public static void copyRunInfo(XWPFRun sourceRun,XWPFRun destRun){
        destRun.setBold(sourceRun.isBold());
        destRun.setCapitalized(sourceRun.isCapitalized());
        destRun.setCharacterSpacing(sourceRun.getCharacterSpacing());
        destRun.setColor(sourceRun.getColor());
    }
    public static void copyParagraph(XWPFParagraph sourceParagraph,XWPFParagraph destParagraph){

    }
    interface ParseRunsTextCallback{
        void run(int index, String txt);
    }
}
