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
package com.github.stagirs.lingvo.morpho;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Mapping implements Comparable<Mapping>{

    public static class Item{
        String rule;
        String[] commons;
        
        public Item(String rule, String[] commons) {
            this.rule = rule;
            this.commons = commons;
        }
    }
    
    String pref;
    String suf;
    
    List<Item> items = new ArrayList<Item>();

    private Mapping() {}
    
    public Mapping(String line) {
        String[] parts = line.split("\t");
        String[] main = parts[0].split("/", -1);
        this.pref = main[0];
        this.suf = main[1];
        for (int i = 1; i < parts.length; i++) {
            String[] p = parts[i].split(" ", -1);
            items.add(new Item(p[0], p[1].split("/", -1)));
        }
    }
    
    public void add(String rule, String[] commons){
        items.add(new Item(rule, commons));
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pref).append("/").append(suf);
        for (Item item : items) {
            sb.append("\t").append(item.rule).append(" ");
            for (String common : item.commons) {
                sb.append(common).append("/");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); 
    }
    
    
    @Override
    public int compareTo(Mapping o) {
        return o.items.size() - items.size();
    }
    
    public static Mapping get(String key){
        Mapping mapping = new Mapping();
        String[] main = key.split("/", -1);
        mapping.pref = main[0];
        mapping.suf = main[1];
        return mapping;
    }
}
