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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Rule implements Comparable<Rule>{

    RuleItem[][] items;
    String pre;
    String suf;
    int freq;
    int normCount;

    public Rule(String pre, String suf, String line, int freq) {
        this.pre = pre;
        this.suf = suf;
        this.freq = freq;
        String[] rules = line.split("/", -1);
        normCount = rules.length / 4;
        Map<String, List<RuleItem>> ruleItems = new HashMap();
        for (int i = 0; i < rules.length; i+=4) {
            String key = rules[i] + "/" + rules[i + 1];
            if(!ruleItems.containsKey(key)){
                ruleItems.put(key, new ArrayList<RuleItem>());
            }
            ruleItems.get(key).add(new RuleItem(rules[i], rules[i + 1], new Form(rules[i + 2]), new Form(rules[i + 3])));
        }
        items = new RuleItem[ruleItems.size()][];
        int i = 0;
        for (Map.Entry<String, List<RuleItem>> entry : ruleItems.entrySet()) {
            items[i++] = entry.getValue().toArray(new RuleItem[entry.getValue().size()]);
        }
    }

    public int getNormCount() {
        return normCount;
    }

    public String getPre() {
        return pre;
    }

    public String getSuf() {
        return suf;
    }
    
    
    public int getFreq() {
        return freq;
    }

    public RuleItem[][] getItems() {
        return items;
    }

    @Override
    public int compareTo(Rule o) {
        return o.freq - freq;
    }
}
