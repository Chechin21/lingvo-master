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
public class PredictedMorpho extends Morpho{
    private String dictionaryWord;
    private String dictionaryCommon;
    private int distance;
    
    public PredictedMorpho(String word, Rule rule, String dictionaryWord, int distance) {
        super(word, rule);
        this.distance = distance;
        this.dictionaryWord = dictionaryWord;
        this.dictionaryCommon = word.substring(rule.pre.length(), word.length() - rule.suf.length());
    }
    public String getDictionaryNorm() {
        return rule.items[0][0].getPre() + dictionaryCommon + rule.items[0][0].getSuf();
    }
    
    public String[] getDictionaryNorms() {
        String[] norms = new String[rule.items.length];
        for (int i = 0; i < norms.length; i++) {
            norms[i] = rule.items[i][0].getPre() + dictionaryCommon + rule.items[i][0].getSuf();
        }
        return norms;
    }

    public String getDictionaryWord() {
        return dictionaryWord;
    }

    public int getDistance() {
        return distance;
    }
}
