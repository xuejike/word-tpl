package com.github.xuejike.wordtpl.freemarker;

import com.github.xuejike.wordtpl.parse.TokenParse;
import com.github.xuejike.wordtpl.parse.TplToken;

/**
 * @author xuejike
 */
public class FreemarkerTokenParse extends TokenParse {
    public FreemarkerTokenParse() {
        super(new TplToken[]{
                new TplToken("${","}"),
                new TplToken("<@",">",true),
                new TplToken("</@",">",true),
                new TplToken("</#",">",true),
                new TplToken("<#",">",true)
        });
    }

    @Override
    public String buildFunction(String funName, String[][] params, String body) {
        StringBuilder builder = new StringBuilder().append("<@").append(funName);
        if (params != null){
            for (String[] param : params) {
                builder.append(" ").append(param[0]).append("=").append("\"").append(param[1]).append("\" ");
            }
        }

        builder.append(">");
        if (body != null){
            builder.append(body);
        }
        builder.append("</@").append(funName).append(">");
        return builder.toString();

    }

    @Override
    public int[] checkHaveBlockTagIndex(String runTxt) {
        if (runTxt == null){
            return null;
        }
        // TODO: 2019/1/20 存在优化调整空间
        int index= -1;
        if ( (index =runTxt.indexOf("<#")) >= 0 || (index =runTxt.indexOf("</#")) >=0
                || (index =runTxt.indexOf("<@"))>=0 || (index =runTxt.indexOf("</@"))>=0){
            return new int[]{index};
        }
        return null;
    }
}
