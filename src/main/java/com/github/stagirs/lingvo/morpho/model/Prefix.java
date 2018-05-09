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
public class Prefix {
    String prefix;
    Rule[] rules;

    public Prefix(String prefix, Rule rule) {
        this.prefix = prefix;
        this.rules = new Rule[]{rule};
    }

    public String getPrefix() {
        return prefix;
    }

    public Rule[] getRules() {
        return rules;
    }

    public void addRule(Rule rule){
        Rule[] rules = new Rule[this.rules.length + 1];
        System.arraycopy(this.rules, 0, rules, 0, this.rules.length);
        rules[this.rules.length] = rule;
        this.rules = rules;
    }
    
}
