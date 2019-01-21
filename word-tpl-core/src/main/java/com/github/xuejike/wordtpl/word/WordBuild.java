package com.github.xuejike.wordtpl.word;

import com.github.xuejike.wordtpl.exception.TplBuildException;
import com.github.xuejike.wordtpl.tpl.WordTpEnvironment;
import com.github.xuejike.wordtpl.tpl.AbstractWordTplFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author xuejike
 */
@Slf4j
public class WordBuild {
    AbstractWordTplFactory wordTplFactory;



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
