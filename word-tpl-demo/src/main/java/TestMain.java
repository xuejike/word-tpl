import com.github.xuejike.wordtpl.WordTplUtils;
import com.github.xuejike.wordtpl.exception.TplBuildException;
import com.github.xuejike.wordtpl.freemarker.FreemarkerTokenParse;

import com.github.xuejike.wordtpl.freemarker.FreemarkerWordTplFactory;
import com.github.xuejike.wordtpl.tpl.functions.WordRunFunction;
import com.github.xuejike.wordtpl.word.WordBuild;
import com.github.xuejike.wordtpl.word.WordParse;
import org.apache.commons.io.FileUtils;
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
        String tplFile = "E:\\project\\word-tpl\\word-tpl\\抗凝药物基因-终极报告.docx";
        String outFile = "E:\\project\\word-tpl\\word-tpl\\var-tpl-out.docx";

        HashMap<String, Object> map = new HashMap<>();
        map.put("xuejike","薛纪克");
        FreemarkerWordTplFactory tplFactory = new FreemarkerWordTplFactory();
        tplFactory.registerFunction(new WordRunFunction());
        WordTplUtils wordTplUtils = new WordTplUtils(tplFactory);

        // 每次构建都会重新生成word-tpl脚本
//        wordTplUtils.buildWord(new File(tplFile),outFile,map);
        // 提前构建脚本，并添加到模板中，以后使用模板名称进行生成word即可，可以避免每次重新构建word脚本
        long begin = System.currentTimeMillis();
        String script = wordTplUtils.buildTplScript(tplFile);
        FileUtils.writeStringToFile(new File("./test.ftl"),script,"UTF-8");
        System.out.println(System.currentTimeMillis()- begin);
        wordTplUtils.addTplScript("test",script);
        wordTplUtils.buildWord("test",outFile,map);
    }
}
