package com.github.xuejike.wordtpl.parse;

import java.util.Map;

/**
 * @author xuejike
 */
public interface TokenParse {
    /**
     * 解析并对初始word模板进行格式化
     * @param txt 文本
     * @param tokenCallback 发现关键字回调
     */
    void parseFormatTpl(StringBuilder txt, TokenCallback tokenCallback);

    /**
     * 生成自定义函数
     * @param funName 函数名称
     * @param params 参数 name value 数组
     * @param body
     * @return
     */
    String buildFunction(String funName, Map<String,Object> params, String body);

    interface TokenCallback{
        /**
         * 发现Token关键字
         * @param begin 开始行 列
         * @param end 结束 行 列
         * @param token token
         */
        void findToken(int[] begin, int[] end, TplToken token);
    }
}
