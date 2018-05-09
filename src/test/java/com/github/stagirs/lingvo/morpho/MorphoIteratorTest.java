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

import com.github.stagirs.lingvo.morpho.model.Morpho;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.lang.math.NumberUtils.toInt;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Dmitriy Malakhov
 */
public class MorphoIteratorTest {
    @Test
    public void test() throws IOException{
        Map<String, Integer> word2size = new HashMap<String, Integer>();
        for(String line : FileUtils.readLines(new File("words"), "utf-8")){
            String[] parts = line.split("\t");
            word2size.put(parts[0], toInt(parts[1]));
        }
        MorphoIterator iterator = new MorphoIterator();
        long time = System.currentTimeMillis();
        int i = 0;
        while(iterator.hasNext()){
            iterator.next();
            i++;
        }
        System.out.println(i + " " + (System.currentTimeMillis() - time));
        Assert.assertEquals(word2size.size(), i);
        
        Set<String> words = new HashSet<String>();
        iterator = new MorphoIterator();
        
        while(iterator.hasNext()){
            Morpho m = iterator.next();
            if(!word2size.containsKey(m.getWord())){
                throw new RuntimeException(m.getWord());
            }
            if(word2size.get(m.getWord()) != m.getNormCount()){
                throw new RuntimeException(m.getWord() + " " + m.getNormCount());
            }
            words.add(m.getWord());
        }
        for (Map.Entry<String, Integer> entry : word2size.entrySet()) {
            if(!words.contains(entry.getKey())){
                throw new RuntimeException(entry.getKey());
            }
        }
    }
}
