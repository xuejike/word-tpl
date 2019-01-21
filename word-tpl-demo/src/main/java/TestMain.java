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
    public static void main(String[] args) throws IOException, TplBuildException {
        String tplFile = "E:\\project\\word-tpl\\word-tpl\\var-tpl.docx";
        String outFile = "E:\\project\\word-tpl\\word-tpl\\var-tpl-out.docx";

        HashMap<String, Object> map = new HashMap<>();
        map.put("xuejike","薛纪克");
        WordTplUtils wordTplUtils = new WordTplUtils(new FreemarkerWordTplFactory());
        wordTplUtils.buildWord(new File(tplFile),outFile,map);
    }
}
