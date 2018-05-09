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
package com.github.stagirs.lingvo.morpho.build;

import com.github.stagirs.lingvo.morpho.model.Attr;
import static com.github.stagirs.lingvo.morpho.model.Attr.*;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 1. нужно выявить какая морфология вызывает неоднозначность, построить распределение
 * 2. нужно определить насколько подходит обучающая выборка опенкорпоры под снятие омонимии, какие случаи неоднозначности остались не у дел
 *     можно считать, что неоднозначность покрыта, если для нее в выборке есть по N и более примеров
 *     
 *     можно выделить 2 типа неоднозначности, когда нормальная форма одна и та же или когда она разная (более важный случай для нашей задачи)
 * 
 * 3. для не покрытых случаев неоднозначности найти в википедии примеры и разметить их
 * 4. построить для пар морфологии + служ. части речи распрееление по частотам встречаемости
 * 5. построить классификатор
 * 6. прогнать на обучающей выборке и на википедии, выявить предложения где омонимия не снята, те оценка правдоподобия для разных вариантов очень похожа
 * @author Dmitriy Malakhov
 */
public class Prediction {
    private boolean contains(ArrayList<String[]> array, String ... el){
        for(String[] str : array){
            if(str[0].equals(el[0]) && str[1].equals(el[1]) && str[2].equals(el[2])){
                return true;
            }
        }
        return false;
    }
    
    static Map<Attr, Attr> attr2attr = new HashMap<Attr, Attr>(){
        {
            put(gen1, gent);
            put(gen2, gent);
            put(acc2, accs);
            put(loc1, loct);
            put(loc2, loct);
        }
    };
    static Set<Attr> significant = new HashSet<Attr>(Arrays.asList(
            NOUN, PREP, CONJ,INFN,VERB,ADJF,ADJS,ADVB,COMP,PRTF,PRTS,GRND,NUMR,NPRO,PRED,PRCL,INTJ,
            masc,femn,neut,
            sing,plur,
            nomn,gent,datv,accs,ablt,loct,voct,
            N1per,N2per,N3per,
            pres,past,futr,
            indc,impr));
    
    public void test1() throws IOException{
        Map<String, ArrayList<String[]>> form2norm = new HashMap();
        for (String line : FileUtils.readLines(new File("forms_dictionary.txt"), "utf-8")) {
            String[] parts = line.split(" ");
            
            for (int i = 0; i < parts.length; i+=2) {
                if(!form2norm.containsKey(parts[i])){
                    form2norm.put(parts[i], new ArrayList());
                }
                if(contains(form2norm.get(parts[i]), parts[0], parts[1], parts[i + 1])){
                    continue;
                }
                form2norm.get(parts[i]).add(new String[]{parts[0], parts[1], parts[i + 1]});
            }
        }  
        TObjectIntHashMap<String> counterAnnot = new TObjectIntHashMap<String>();
        for (String line : FileUtils.readLines(new File("annot-opcorpora"), "utf-8")) {
            String[] parts = line.split("\t");
            for (int i = 1; i < parts.length; i++) {
                String word = parts[i].split(" ")[0];
                ArrayList<String[]> norms = form2norm.get(word);
                if(norms == null){
                    continue;
                }
                if(norms.size() < 2){
                    continue;
                }
                List<String> m = new ArrayList<String>();
                for (String[] string : norms) {
                    m.add(filterAttrs(string[2]));
                }
                Collections.sort(m);
                counterAnnot.adjustOrPutValue(StringUtils.join(m, " "), 1, 1);
            }
        }
        TObjectIntHashMap<String> counter = new TObjectIntHashMap<String>();
        for (Map.Entry<String, ArrayList<String[]>> entry : form2norm.entrySet()) {
            if(entry.getValue().size() < 2){
                continue;
            }
            List<String> m = new ArrayList<String>();
            for (String[] string : entry.getValue()) {
                m.add(filterAttrs(string[2]));
            }
            Collections.sort(m);
            counter.adjustOrPutValue(StringUtils.join(m, " "), 1, 1);
        }
        List<String> result = new ArrayList<String>();
        for(String m : counter.keySet()){
            result.add(String.format("%06d", counter.get(m)) + "\t" + counterAnnot.get(m) + "\t" + m);
        }
        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        FileUtils.writeLines(new File("morpho-distribution.txt"), "utf-8", result);
    }
    
    
    public void test2() throws IOException{
        Map<String, ArrayList<String[]>> form2norm = new HashMap();
        for (String line : FileUtils.readLines(new File("forms_dictionary.txt"), "utf-8")) {
            String[] parts = line.split(" ");
            
            for (int i = 0; i < parts.length; i+=2) {
                if(!form2norm.containsKey(parts[i])){
                    form2norm.put(parts[i], new ArrayList());
                }
                if(contains(form2norm.get(parts[i]), parts[0], parts[1], parts[i + 1])){
                    continue;
                }
                form2norm.get(parts[i]).add(new String[]{parts[0], parts[1], parts[i + 1]});
            }
        }  
        TObjectIntHashMap<String> attr2count = new TObjectIntHashMap<String>();
        for (String line : FileUtils.readLines(new File("annot-opcorpora"), "utf-8")) {
            String[] parts = line.split("\t");
            for (int i = 1; i < parts.length; i++) {
                attr2count.adjustOrPutValue(filterAttrs(parts[i].split(" ")[1]), 1, 1);
            }
        }
        TObjectIntHashMap<String> counter = new TObjectIntHashMap<String>();
        for (Map.Entry<String, ArrayList<String[]>> entry : form2norm.entrySet()) {
            if(entry.getValue().size() < 2){
                continue;
            }
            for (String[] string : entry.getValue()) {
                counter.adjustOrPutValue(filterAttrs(string[2]), 1 ,1);
            }
        }
        List<String> result = new ArrayList<String>();
        for(String m : counter.keySet()){
            result.add(String.format("%06d", counter.get(m)) + "\t" + attr2count.get(m) + "\t" + m);
        }
        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        FileUtils.writeLines(new File("attrs-distribution.txt"), "utf-8", result);
    }
    
    String filterAttrs(String attrs){
        List<Attr> res = new ArrayList<Attr>();
        for(String attr : attrs.split(",")){
            Attr a = Attr.valueOf(attr);
            if(attr2attr.containsKey(a)){
                a = attr2attr.get(a);
            }
            if(!significant.contains(a)){
                continue;
            }
            res.add(a);
        }
        return StringUtils.join(res, ",");
    }
}
