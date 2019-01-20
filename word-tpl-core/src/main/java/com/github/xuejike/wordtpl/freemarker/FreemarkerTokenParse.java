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
                new TplToken("<@",">"),
                new TplToken("</@",">"),
                new TplToken("</#",">"),
                new TplToken("<#",">")
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
    public boolean checkHaveBlockTag(String runTxt) {
        if (runTxt == null){
            return false;
        }
        // TODO: 2019/1/20 存在优化调整空间
        if (runTxt.indexOf("<#") > 0 | runTxt.indexOf("<@")  > 0
                | runTxt.indexOf("</#") > 0 | runTxt.indexOf("</@") >0 ){
            return true;
        }
        return false;
    }
}
