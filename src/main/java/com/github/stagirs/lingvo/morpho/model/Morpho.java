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

/**
 *
 * @author Dmitriy Malakhov
 */
public class Morpho {
    String word;
    String common;
    Rule rule;

    public Morpho(String word, Rule rule) {
        this.word = word;
        this.common = word.substring(rule.pre.length(), word.length() - rule.suf.length());
        this.rule = rule;
    }

    public String getNorm() {
        return rule.items[0][0].getPre() + common + rule.items[0][0].getSuf();
    }
    
    public String[] getNorms() {
        String[] norms = new String[rule.items.length];
        for (int i = 0; i < norms.length; i++) {
            norms[i] = rule.items[i][0].getPre() + common + rule.items[i][0].getSuf();
        }
        return norms;
    }
    public Form[] getRaws() {
        Form[] raws = new Form[rule.items.length];
        for (int i = 0; i < rule.items.length; i++) {
            raws[i] = rule.items[i][0].getRaw();
        }
        return raws;
    }
    public Form[] getRawsfull() {
        Form[] raws = new Form[rule.normCount];
        for (int i = 0; i < rule.items.length; i++) {
            for (int j = 0;j < rule.items[i].length; j++){
                raws[i] = rule.items[i][j].getRaw();
            }

        }
        return raws;
    }
    
    public int getNormCount(){
        return rule.normCount;
    }
    public int getNormsCount() {return rule.items.length;}
    public String getWord() {
        return word;
    }
    
    
}
