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

import com.github.stagirs.lingvo.morpho.Mapping.Item;
import com.github.stagirs.lingvo.morpho.model.Morpho;
import com.github.stagirs.lingvo.morpho.model.Rule;
import com.github.stagirs.lingvo.morpho.model.Node;
import com.github.stagirs.lingvo.morpho.model.PredictedMorpho;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class MorphoAnalyst {
    static Node node = new Node();
    static {
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            URL url = cl.getResource("mapping");
            InputStream in = url.openStream();
            try{
                for (String line : IOUtils.readLines(in, "utf-8")){
                   Mapping mapping = new Mapping(line);
                    for (Item item : mapping.getItems()) {
                        node.add(mapping.pref, mapping.suf, new Rule(mapping.pref, mapping.suf, item.rule, item.commons.length), item.commons);
                    } 
                }
            }finally{
                in.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static List<String> normalize(List<String> words){
        List<String> result = new ArrayList<String>();
        for (String word : words) {
            Morpho morpho = find(word);
            if(morpho == null){
                result.add(word);
            }else{
                result.add(morpho.getNorm());
            }
        }
        return result;
    }
    
    public static Morpho find(String word){
        return node.find(word);
    }
    
    public static PredictedMorpho predict(String word){
        return node.predict(word);
    }
}
