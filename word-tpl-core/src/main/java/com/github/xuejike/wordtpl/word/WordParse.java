package com.github.xuejike.wordtpl.word;

import com.github.xuejike.wordtpl.parse.TokenParse;

import com.github.xuejike.wordtpl.parse.TplToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author xuejike
 */
@Slf4j
public  class WordParse {

    public static final String PIC_TAG = "@PIC";
    public static final String  C_DEL_TAG = "@DEL";

    protected TokenParse tokenParse;

    public WordParse(TokenParse tokenParse) {
        this.tokenParse = tokenParse;
    }

    public StringBuilder word2Tpl(String filePath) throws IOException {
        return word2Tpl(new File(filePath));
    }
    public StringBuilder word2Tpl(File file) throws IOException {
       return word2Tpl(new FileInputStream(file));
    }
    public StringBuilder word2Tpl(FileInputStream fileInputStream) throws IOException {
        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);

        StringBuilder tpl = new StringBuilder();

        ArrayList<XWPFRun> xwpfRuns = getWordRuns(xwpfDocument);
        //生成初级版本模板字符串
        xwpfRuns.forEach(r-> appendRunTpl(tpl, r));
        //解析合并run字符串

        HashMap<XWPFRun, LinkedList<RunTokenInfo>> runTokenMap = new HashMap<>();
        tokenParse.parseFormat(tpl, (begin, end, token) -> {
            RunTokenInfo runTokenInfo = marginRun(begin, end, xwpfRuns, token);
            LinkedList<RunTokenInfo> list = runTokenMap.computeIfAbsent(runTokenInfo.getXwpfRun(), k -> new LinkedList<>());
            list.add(runTokenInfo);
        });

        tpl.setLength(0);

        //生成初级版本模板字符串
        xwpfRuns.forEach(r-> appendRunTpl(tpl, r));
        System.out.println(tpl);
        tpl.setLength(0);
        List<Object> wordItemList = getWordAllItemList(xwpfDocument);
        parseWordItemScript(tpl,wordItemList,runTokenMap);


        return tpl;

    }

    private void parseWordItemScript(StringBuilder tpl, List<Object> wordItemList, HashMap<XWPFRun, LinkedList<RunTokenInfo>> runTokenMap) {
        String[][] params = new String[1][];
        params[0] = new String[]{"index", String.valueOf(wordItemList.size())};
        for (int i = 0; i < wordItemList.size(); i++) {
            Object o = wordItemList.get(i);
            params[0][1] = String.valueOf(i);
            if (o instanceof IBodyElement){
                switch (((IBodyElement) o).getElementType()){
                    case PARAGRAPH:{
                        tpl.append(tokenParse.buildFunction("wordP", params, null))
                        .append("\n");
                        break;
                    }
                    case TABLE:
                    {
                        tpl.append(tokenParse.buildFunction("wordTable", params, null))
                                .append("\n");
                        break;
                    }
                }
            }else if (o instanceof XWPFRun){
                XWPFRun xwpfRun = (XWPFRun) o;
                String runText = getRunText(xwpfRun);
                LinkedList<RunTokenInfo> runTokenInfos = runTokenMap.get(xwpfRun);
                if (runTokenInfos == null){
                    tpl.append(tokenParse.buildFunction("wordRun",params,runText))
                            .append("\n");
                }else{
                    String[][] runSP = new String[2][];
                    runSP[0] = params[0];
                    runSP[1] = new String[]{"limit","0"};
                    int begin = 0;
                    int limit = 0;

                    for (RunTokenInfo info : runTokenInfos) {
                        if (info.getTplToken().isBlock()){
                            if (info.beginIndex == 0){
                                begin = info.endIndex+1;
                                String tokenKey = runText.substring(info.beginIndex, info.endIndex+1);
                                tpl.append(tokenKey).append("\n");
                            }else{
                                String substring = runText.substring(begin, info.beginIndex);
                                runSP[1][1] = String.valueOf(limit);
                                tpl.append(tokenParse.buildFunction("wordRun",runSP,substring))
                                        .append("\n");
                                String tokenKey = runText.substring(info.beginIndex, info.endIndex+1);
                                tpl.append(tokenKey).append("\n");
                                limit++;
                                begin = info.endIndex+1;
                            }
                        }
                    }
                    if (begin < runText.length()){
                        String substring = runText.substring(begin);
                        runSP[1][1] = String.valueOf(limit);
                        tpl.append(tokenParse.buildFunction("wordRun",runSP,substring))
                                .append("\n");
                    }

                }

            }else if ( o instanceof XWPFTableRow){
                tpl.append(tokenParse.buildFunction("wordTableRow", params, null))
                        .append("\n");
            }else if (o instanceof XWPFTableCell){
                tpl.append(tokenParse.buildFunction("wordTableCell", params, null))
                        .append("\n");
            }
        }
        tpl.append(tokenParse.buildFunction("wordScriptFinish",null,null));

    }

    public static List<Object> getWordAllItemList(XWPFDocument xwpfDocument){
        ArrayList<Object> list = new ArrayList<>();
        for (IBodyElement element : xwpfDocument.getBodyElements()) {
            switch (element.getElementType()){
                case PARAGRAPH:
                {
                    getWordParagraphAllItem(list, (XWPFParagraph) element);
                    break;
                }
                case TABLE:
                {
                    getWordTableAllItem(list,((XWPFTable) element));
                    break;
                }
            }
        }
        return list;
    }

    private static void getWordTableAllItem(ArrayList<Object> list, XWPFTable table) {
        list.add(table);
        for (XWPFTableRow row : table.getRows()) {
            list.add(row);
            for (XWPFTableCell cell : row.getTableCells()) {
                list.add(cell);
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    getWordParagraphAllItem(list,paragraph);
                }
            }
        }

    }

    private static void getWordParagraphAllItem(ArrayList<Object> list, XWPFParagraph paragraph) {
        list.add(paragraph);
        list.addAll(paragraph.getRuns());
    }


    public String getRunText(XWPFRun xwpfRun){
        String text = xwpfRun.getText(0);
        if (text == null){
            if (xwpfRun.getEmbeddedPictures().size() > 0){
                return PIC_TAG;
            }else{
                return null;
            }
        }else{
            return text;
        }
    }


    public static ArrayList<XWPFRun> getWordRuns(XWPFDocument xwpfDocument) {
        ArrayList<XWPFRun> xwpfRuns = new ArrayList<>();
        for (IBodyElement bodyElement : xwpfDocument.getBodyElements()) {
            switch (bodyElement.getElementType()){
                case PARAGRAPH:
                {
                    xwpfRuns.addAll(((XWPFParagraph) bodyElement).getRuns());

                    break;
                }
                case TABLE:
                {
                    // TODO: 2019/1/17 暂未实现
                    XWPFTable table = (XWPFTable) bodyElement;
                    List<XWPFTableRow> rows = table.getRows();
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                                xwpfRuns.addAll(paragraph.getRuns());
                            }

                        }

                    }

                    break;
                }
                default:{

                }
            }
        }
        return xwpfRuns;
    }

    private StringBuilder appendRunTpl(StringBuilder tpl, XWPFRun run) {
        int picSize = run.getEmbeddedPictures().size();
        if (picSize > 0){
            tpl.append(PIC_TAG);
        }else{
            tpl.append(run.getText(0));
        }
        tpl.append("\n");
        return tpl;
    }

    private Collection<? extends XWPFRun> parseParagraph(XWPFParagraph bodyElement) {
        return bodyElement.getRuns();
    }
    private RunTokenInfo marginRun(int[] beginRowNum, int[] endRowNum, List<XWPFRun> runList, TplToken token) {
        RunTokenInfo runTokenInfo = new RunTokenInfo();
        runTokenInfo.setTplToken(token);
        if (log.isDebugEnabled()){
            log.debug("#########Token##########");
            log.debug("# key > {} {}",token.getBegin(),token.getEnd());
            log.debug("# begin => {} {}",beginRowNum[0],beginRowNum[1]);
            log.debug("# end => {} {}",endRowNum[0],endRowNum[1]);
            log.debug("#--------Before---------");
            for (int i = beginRowNum[0]; i <= endRowNum[0]; i++) {
                log.debug("{}:{}",i,runList.get(i).getText(0));
            }
        }
        XWPFRun begin = runList.get(beginRowNum[0]);
        runTokenInfo.setXwpfRun(begin);
        if (beginRowNum[0] < endRowNum[0]){
            runTokenInfo.setBeginIndex(beginRowNum[1]);

            StringBuilder marginTxt = new StringBuilder(begin.getText(0));
            for (int i = beginRowNum[0]+1; i < endRowNum[0]; i++) {
                marginTxt.append(runList.get(i).getText(0));
                runList.get(i).setText(C_DEL_TAG,0);
            }
            XWPFRun lastRun = runList.get(endRowNum[0]);
            String lastText = lastRun.getText(0);
            String substring = lastText.substring(0, endRowNum[1]+1);
            marginTxt.append(substring);
            if (lastText.length() >(endRowNum[1]+1)){
                lastRun.setText(lastText.substring(endRowNum[1]+1),0);
            }else{
                lastRun.setText(C_DEL_TAG,0);
            }

            runTokenInfo.setEndIndex(marginTxt.length()-1);
            begin.setText(marginTxt.toString(),0);
        }else{
            runTokenInfo.setBeginIndex(beginRowNum[1]);
            runTokenInfo.setEndIndex(endRowNum[1]);
        }
        if (log.isDebugEnabled()){
            log.debug("#--------After---------");
            log.debug("{}",runTokenInfo.getXwpfRun().getText(0));
            log.debug("# begin>{},end>{}",runTokenInfo.beginIndex,runTokenInfo.endIndex);
        }

        return runTokenInfo;
    }

    public static boolean checkHaveTag(String txt){
        if (PIC_TAG.equals(txt)){
            return true;
        }
        return false;
    }
}
