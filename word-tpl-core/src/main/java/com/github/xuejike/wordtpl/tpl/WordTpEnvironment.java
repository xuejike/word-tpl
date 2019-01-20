package com.github.xuejike.wordtpl.tpl;

import org.apache.poi.xwpf.usermodel.XWPFRun;

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


    public abstract Object getTplVar(String name);
    public abstract void setTplVar(String name, Object val);

    public void setEnvVar(String name,Object val){
        envVarMaps.put(name,val);
    }
    public Object getEnvVar(String name){
        return envVarMaps.get(name);
    }

}
