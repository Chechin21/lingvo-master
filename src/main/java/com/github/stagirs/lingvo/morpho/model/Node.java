/*
 * Copyright 2017 Dmitriy Malakhov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.stagirs.lingvo.morpho.model;

import com.github.stagirs.lingvo.morpho.Diff;
import com.github.stagirs.lingvo.morpho.StringStore;
import static com.github.stagirs.lingvo.morpho.Utils.char2byte;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Node {
    public static Map<String, Prefix> name2prefix = new HashMap<String, Prefix>();
    
    String suf = "";
    Node[] nodes = new Node[34];
    Map<String, Rule> rules = new HashMap<String, Rule>();

    public Node() {
    }

    public Node(String suf) {
        this.suf = StringStore.get(suf);
    }
    

    public void add(String pref, String suf, Rule rule, String[] common){
        if(suf.isEmpty()){
            for (String string : common) {
                String prefix = pref + string;
                if(!name2prefix.containsKey(prefix)){
                    name2prefix.put(prefix, new Prefix(prefix, rule));
                }else{
                    name2prefix.get(prefix).addRule(rule);
                }
                rules.put(name2prefix.get(prefix).getPrefix(), rule);
            }
        }else{
            int charCode = char2byte(suf.charAt(suf.length() - 1));
            if(nodes[charCode] == null){
                nodes[charCode] = new Node(suf.charAt(suf.length() - 1) + this.suf);
            }
            nodes[charCode].add(pref, suf.substring(0, suf.length() - 1), rule, common);
        }
    }

    public Morpho find(String word){
        return find(word, word.length() - 1);
    }

    private Morpho find(String word, int finish){
        if(finish >= 0){
            int charCode = char2byte(word.charAt(finish));
            if(nodes[charCode] != null){
                Morpho norm = nodes[charCode].find(word, finish - 1);
                if(norm != null){
                    return norm;
                }
            }
        }
        Rule rule = rules.get(word.substring(0, finish + 1));
        return rule == null ? null : new Morpho(word, rule);
    }

    public PredictedMorpho predict(String word){
        return predict(word, word.length() - 1);
    }
    
    private PredictedMorpho predict(String word, int finish){
        PredictedMorpho morpho = null;
        if(finish >= 0){
            int charCode = char2byte(word.charAt(finish));
            if(nodes[charCode] != null){
                morpho = nodes[charCode].predict(word, finish - 1);
            }
        }
        
        String prefix = word.substring(0, finish + 1);
        Diff diff = new Diff(prefix);
        int minDistance = Integer.MAX_VALUE;
        Map.Entry<String, Rule> curEntry = null;
        for (Map.Entry<String, Rule> entry  : rules.entrySet()) {
            if(!prefix.startsWith(entry.getValue().getPre())){
                continue;
            }
            int distance = diff.getDistance(entry.getKey()) + Math.abs(prefix.length() - entry.getKey().length()) + StringUtils.getLevenshteinDistance(prefix, entry.getKey());
            if(distance < minDistance){
                minDistance = distance;
                curEntry = entry;
            }
        }
        if(curEntry == null || morpho != null && minDistance >= morpho.getDistance()){
            return morpho;
        }else{
            return new PredictedMorpho(word, curEntry.getValue(), curEntry.getKey() + curEntry.getValue().getSuf(), minDistance);
        }
    }
    
    public Node[] getNodes() {
        return nodes;
    }

    public Map<String, Rule> getRules() {
        return rules;
    }

    public String getSuf() {
        return suf;
    }
    
    
}
