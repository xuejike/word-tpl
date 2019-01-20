package com.github.xuejike.wordtpl.word;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuejike
 */
public class WordCache {
    HashMap<String,StringBuilder> tplMap = new HashMap<>();
    Map<XWPFDocument, List<XWPFRun>> wordRunMap = new HashMap<>();



}
