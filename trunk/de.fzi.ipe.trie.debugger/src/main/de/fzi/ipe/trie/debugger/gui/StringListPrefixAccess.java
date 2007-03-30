package de.fzi.ipe.trie.debugger.gui;

import java.util.ArrayList;
import java.util.Collections;


public class StringListPrefixAccess {
    
    private ArrayList<String> list;
    
    private int beginIndex, endIndex;
    
    private String previousPrefix = "";
    
    public StringListPrefixAccess (String[] list) {
        this.list = new ArrayList<String>(list.length);
        for (int i=0;i<list.length;i++) this.list.add(list[i]);
        Collections.sort(this.list);
    }
    
    public void setPrefix(String prefix) {
        if (!prefix.startsWith(previousPrefix)) { 
            beginIndex=0;
            endIndex = list.size();
        }
        while (beginIndex < endIndex) {
            String current = (String) list.get(beginIndex);
            if (current.startsWith(prefix)) break;
            beginIndex++;
        }
        int temp = beginIndex;
        while (temp < endIndex) {
            String current = (String) list.get(temp);
            if (current.startsWith(prefix)) temp++;
            else break;
        }
        endIndex = temp;
    }    

}
