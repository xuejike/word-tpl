import com.github.xuejike.wordtpl.WordTplUtils;
import com.github.xuejike.wordtpl.exception.TplBuildException;
import com.github.xuejike.wordtpl.freemarker.FreemarkerTokenParse;

import com.github.xuejike.wordtpl.freemarker.FreemarkerWordTplFactory;
import com.github.xuejike.wordtpl.word.WordBuild;
import com.github.xuejike.wordtpl.word.WordParse;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author xuejike
 */
public class TestMain {

    public static int status = 0;

    public static void main(String[] args) throws IOException, TplBuildException {
        String tplFile = "E:\\project\\word-tpl\\word-tpl\\var-tpl.docx";
        String outFile = "E:\\project\\word-tpl\\word-tpl\\var-tpl-out.docx";
//        FileInputStream fileInputStream = new FileInputStream(tplFile);
//
//        WordParse wordParse = new WordParse(new FreemarkerTokenParse());
//        StringBuilder tpl = wordParse.word2TplScript(fileInputStream);
//        System.out.println(tpl);
//        FreemarkerWordTplFactory tplFactory = new FreemarkerWordTplFactory();
//        tplFactory.addTpl("test",tpl.toString());
        HashMap<String, Object> map = new HashMap<>();
        map.put("xuejike","薛纪克");
////        String execTplName = tplFactory.execTplName("test", map);
//        WordBuild wordBuild = new WordBuild(tplFactory);
//        XWPFDocument xwpfDocument = wordBuild.execScript(tplFile, tpl.toString(), map);
//        xwpfDocument.write(new FileOutputStream(outFile));
//        System.out.println(execTplName);

//        officeToPDF("E:\\project\\word-tpl-script\\儿童报告_网络版_12-25.docx","E:\\project\\word-tpl-script\\word-table_out.pdf");
        WordTplUtils wordTplUtils = new WordTplUtils(new FreemarkerWordTplFactory());
//        String tplScript = wordTplUtils.buildTplScript(tplFile);
//        System.out.println(tplScript);
        wordTplUtils.buildWord(new File(tplFile),outFile,map);
    }


}
