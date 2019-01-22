package com.github.xuejike.wordtpl.freemarker;

import com.github.xuejike.wordtpl.parse.AbstractTokenParse;
import com.github.xuejike.wordtpl.parse.TplToken;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Map;

/**
 * @author xuejike
 */
public class FreemarkerTokenParse extends AbstractTokenParse {
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
    public String buildFunction(String funName, Map<String,Object> params, String body) {
        StringBuilder builder = new StringBuilder().append("<@").append(funName);
        if (params != null){
            for (Map.Entry<String,Object> param : params.entrySet()) {
                String val = String.valueOf(param.getValue());
                builder.append(" ").append(param.getKey())
                        .append("=").append("\"")
                        .append(val)
                        .append("\" ");
            }
        }

        builder.append(">");
        if (body != null){
            builder.append(body);
        }
        builder.append("</@").append(funName).append(">");
        return builder.toString();
    }


}
