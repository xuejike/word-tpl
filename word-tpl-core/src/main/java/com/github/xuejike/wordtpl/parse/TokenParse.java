package com.github.xuejike.wordtpl.parse;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 解析模板文件关键字 并进行合并
 * @author xuejike
 */
@Slf4j
public abstract class TokenParse {
    protected char[][] startToken;
    protected char[][] endToken;
    protected TplToken[] tokens;


    public TokenParse(TplToken[] tokens) {
        this.tokens = tokens;
        initToken();
    }
    protected void initToken(){
        startToken = new char[tokens.length][];
        endToken = new char[tokens.length][];


        for (int i = 0; i < tokens.length; i++) {
            startToken[i] = tokens[i].begin.toCharArray();
            endToken[i] = tokens[i].end.toCharArray();
        }
    }
    public void parseFormat(StringBuilder txt,TokenCallback tokenCallback){
        Stack<Character> stack = new Stack<>();
        KeyModel model = KeyModel.text;
        int rowNum = 0;
        int colNum = 0;
        int[] beginRow = {-1,-1};
        int[] endRow = {-1,-1};
        ArrayList<Integer> matchToken = new ArrayList<Integer>(tokens.length);
        ArrayList<Integer> successToken = new ArrayList<Integer>(tokens.length);

        int matchTokenIndex = -1;
        for (int i = 0; i < txt.length(); i++) {
            char c = txt.charAt(i);
            if (i == txt.length()-1
                    && model != KeyModel.text&&c =='\n'){
                c=' ';
            }
            if (c == '\n'){
                rowNum ++;
                colNum = 0;
            }else if (model == KeyModel.text){

                if (stack.isEmpty()){
                    if (matchStartToken(c,0,matchToken,successToken) == MatchStatus.haveMatch) {
                        stack.push(c);
                        beginRow[0] = rowNum;
                        beginRow[1] = colNum;
                        if (log.isDebugEnabled()){
                            log.debug("###匹配到第一个关键字###");
                            log.debug("");
                        }
                    }
                    //未匹配到，暂不处理

                }else{
                    switch (matchStartToken(c,stack.size(),matchToken,successToken)){
                        case noMatch:{
                            //未匹配到抛弃之前的所有回复原状
                            if (log.isDebugEnabled()){
                                log.debug("###未匹配成功抛弃###");
                                log.debug("# {}",printStack(stack));
                                log.debug("###################");
                                log.debug("");
                            }
                            printStack(stack);
                            beginRow[0] = -1;
                            beginRow[1] = -1;
                            stack.clear();
                            break;
                        }
                        case haveMatch:{
                            //成功匹配关键字，继续匹配
                            stack.push(c);
                            if (log.isDebugEnabled()){
                                log.debug("###匹配到关键字###");
                                log.debug("# {}",printStack(stack));
                                log.debug("###################");
                                log.debug("");
                            }
                            break;
                        }
                        case matchSuccess:{
                            //匹配完成，进入关键字模式
                            matchTokenIndex = successToken.get(0);
                            if (log.isDebugEnabled()){
                                log.debug("#####匹配成功#####");
                                log.debug("# {}",printStack(stack));
                                log.debug("###################");
                                log.debug("");
                                log.debug("----------------------------------");
                                log.debug("#######进入关键字模式#######");
                                log.debug("");
                            }
                            model = KeyModel.keyWord;
                            stack.clear();
                            break;
                        }
                        default:{
                            beginRow[0] = -1;
                            beginRow[1] = -1;
                            stack.clear();
                        }
                    }
                }

            } else if (model == KeyModel.keyWord){
                //关键字模式

                if (c =='"'){
                    model = KeyModel.stringModel;
                    stack.push(c);
                    if (log.isDebugEnabled()){
                        log.debug("####进入字符串模式####");
                        log.debug("");
                    }
                }else{

                    MatchStatus matchStatus = matchEndToken(c, stack.size(), matchTokenIndex);
                    switch (matchStatus){
                        case noMatch:{
                            stack.clear();
                            break;
                        }
                        case haveMatch:{
                            stack.push(c);
                            endRow[0]= rowNum;
                            endRow[1] = colNum;

                            break;
                        }
                        case matchSuccess:{
                            //匹配完成 执行回调
                            stack.clear();
                            tokenCallback.findToken(beginRow,endRow,tokens[matchTokenIndex]);
                            model = KeyModel.text;
                            beginRow[0] = -1;
                            beginRow[1] = -1;
                            endRow[0]= -1;
                            endRow[1] = -1;
                            //退一步
                            i --;
                            if (log.isDebugEnabled()){
                                log.debug("#######退出关键字模式#######");
                                log.debug("------------------------------------");
                                log.debug("");
                            }
                            break;
                        }
                        default:{

                        }
                    }


                }
            }else if (model ==KeyModel.stringModel){
                if (stack.peek() =='\\'){
                    stack.pop();
                }else if (stack.peek() =='"'){
                    switch (c){
                        case '\\':{
                            stack.push(c);
                            break;
                        }
                        case '"':{
                            stack.pop();
                            model = KeyModel.keyWord;
                            if (log.isDebugEnabled()){
                                log.debug("####退出字符串模式####");
                                log.debug("");
                            }
                            break;
                        }
                        default:{

                        }
                    }
                }else{
                    throw new RuntimeException("stringModel error");
                }


            }



            colNum ++;
        }
    }

    private MatchStatus matchEndToken(char c, int matchLen, int matchTokenIndex) {
        char[] chars = endToken[matchTokenIndex];
        if (matchLen == chars.length){
            return MatchStatus.matchSuccess;
        }else if (matchLen > chars.length){
            throw new RuntimeException("matchLen > endToken");
        }else if (chars[matchLen] == c){
            return MatchStatus.haveMatch;
        }else{
            return MatchStatus.noMatch;
        }
    }

    protected MatchStatus matchStartToken(char c, int index, List<Integer> matchToken,List<Integer> successToken){

        if (index <= 0 ){
            matchToken.clear();
            for (int i = 0; i < startToken.length; i++) {
                if (startToken[i][0] == c){
                    matchToken.add(i);
                }
            }
            if (matchToken.size() > 0){
                return MatchStatus.haveMatch;
            }else{
                return MatchStatus.noMatch;
            }
        }else{
            if (matchToken.size() > 0){
                Integer[] matchTk =new Integer[matchToken.size()];
                matchTk =matchToken.toArray(matchTk);
                successToken.clear();
                for (Integer tkIndex : matchTk) {
                    char[] token = startToken[tkIndex];
                    if (token.length == index){
                        successToken.add(tkIndex);
                        matchToken.remove(tkIndex);
                    }else if (token.length < index){
                        matchToken.remove(tkIndex);
                    }else if (token[index] != c){
                        matchToken.remove(tkIndex);
                    }
                }
                if (matchToken.size() > 0){
                    return MatchStatus.haveMatch;
                }else if (successToken.size() == 1){
                    return MatchStatus.matchSuccess;
                }else{
                    //最后没有匹配成功
                    return MatchStatus.noMatch;
                }

            }else{
                throw new RuntimeException("matchTokenSize = 0");
            }
        }
    }

    protected String printStack(Stack<Character> stack){
        StringBuilder sb = new StringBuilder();
        for (Character character : stack) {
            sb.append(character);
        }
        return sb.toString();


    }

    public abstract String buildFunction(String funName, String[][] params, String body);

    public boolean checkHaveBlockTag(String runTxt) {

        return false;
    }

    public interface TokenCallback{
        void findToken(int[] begin, int[] end, TplToken token);
    }

    enum MatchStatus{
        noMatch,haveMatch,matchSuccess
    }

    enum KeyModel{
        text, keyWord,stringModel
    }
}
