package com.github.xuejike.wordtpl.tpl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class WordTpEnvironment {
    public static final String WORD_ENV_TAG ="WORD_ENV_TAG";
    public static final String CURRENT_RUN_TAG = "CURRENT_RUN_TAG";
    public static final String CURRENT_P_TAG = "CURRENT_P_TAG";
    public static final String WORD_ITEM_LIST_KEY = "WORD_ITEM_LIST_KEY";
    public static final String CURRENT_TABLE_TAG ="CURRENT_TABLE_TAG";
    public static final String CURRENT_TABLE_ROW_TAG ="CURRENT_TABLE_ROW_TAG";
    public static final String CURRENT_TABLE_CELL_TAG ="CURRENT_TABLE_CELL_TAG";


    protected Map<String,Object> envVarMaps = new HashMap<>();
    protected HashMap<Integer,Object> execIndex = new HashMap<>();
    protected XWPFDocument xwpfDocument;



    public void setCurrentIndex(String tag, Object index){
        setEnvVar(tag,index);
        execIndex.put(Integer.parseInt(String.valueOf(index)),index);
    }
    public XWPFRun getCurrentRun(){
        Object index = getEnvVar(CURRENT_RUN_TAG);
        if (index == null){
            throw new RuntimeException("currentRun index is null");
        }
        List list = getWordItemList();
        Object run = list.get(Integer.parseInt(index.toString()));
        if (run instanceof XWPFRun){
            return (XWPFRun) run;
        }else{
            throw new RuntimeException("index "+index+" not is XWPFRun");
        }
    }

    public XWPFParagraph getCurrentParagraph(){
        Object index = getEnvVar(CURRENT_P_TAG);
        if (index == null){
            throw new RuntimeException("currentParagraph index is null");
        }
        List list = getWordItemList();
        Object run = list.get(Integer.parseInt(index.toString()));
        if (run instanceof XWPFParagraph){
            return (XWPFParagraph) run;
        }else{
            throw new RuntimeException("index "+index+" not is XWPFParagraph");
        }
    }

    public List getWordItemList(){
        Object tplVar = getEnvVar(WORD_ITEM_LIST_KEY);
        if (tplVar == null){
            throw new RuntimeException("word item list is null");
        }
        List list = (List) tplVar;
        return list;
    }

    public abstract Object getTplVar(String name);
    public abstract void setTplVar(String name, Object val);

    public void setEnvVar(String name,Object val){
        envVarMaps.put(name,val);
    }
    public Object getEnvVar(String name){
        return envVarMaps.get(name);
    }

    public HashMap<Integer, Object> getExecIndexMap() {
        return execIndex;
    }


    public abstract OutputStream getWordOutput();
    public abstract Writer getTplResultWriter();

    public void execScriptFinish(){

    }
    public void execScriptStart(){

    }

    public XWPFDocument getXwpfDocument() {
        return xwpfDocument;
    }

    public void setXwpfDocument(XWPFDocument xwpfDocument) {
        this.xwpfDocument = xwpfDocument;
    }
}
