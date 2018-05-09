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

import com.github.stagirs.lingvo.morpho.Mapping;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Plain2Mapping {
    public static void main(String[] args) throws IOException {
        List<String> lines = FileUtils.readLines(new File("forms_dictionary.txt"), "utf-8");
        Set<String> suffixes = new HashSet();
        Map<String, Set<String>> form2norms = new HashMap();
        for (String line : lines) {
            String[] parts = line.split(" ");
            for (int i = 0; i < parts.length; i+=2) {
                if(!form2norms.containsKey(parts[i])){
                    form2norms.put(parts[i], new HashSet());
                }
                form2norms.get(parts[i]).add(parts[0] + "/" + parts[1] + "/" + parts[i + 1]);
                int[] com = getCommon(parts[0], parts[i]);
                suffixes.add(parts[i].substring(com[1]));
            }
        }
        HashMap<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
        Map<String, Map<String, Set<String>>> resultWithCommon = new HashMap();
        for (Map.Entry<String, Set<String>> line : form2norms.entrySet()) {
            String form = line.getKey();
            int[] index = new int[]{0, form.length()};
            for (String l : line.getValue()) {
                String[] norm = l.split("/");
                int[] com = getCommon(norm[0], form);
                index[0] = Math.max(com[0], index[0]);
                index[1] = Math.min(com[1], index[1]);
            }
            String common = index[0] < index[1] ? form.substring(index[0], index[1]) : "";
            while(!common.isEmpty() && suffixes.contains(form.substring(form.indexOf(common) + common.length() - 1))){
                common = common.substring(0, common.length() - 1);
            }
            String pre = form.substring(0, form.indexOf(common));
            String suf = form.substring(form.indexOf(common) + common.length());
            List<String> rules = new ArrayList();
            for (String l : line.getValue()) {
                String[] mainWord = l.split("/");
                String mainPre = mainWord[0].substring(0, mainWord[0].indexOf(common));
                String mainSuf = mainWord[0].substring(mainWord[0].indexOf(common) + common.length());
                rules.add(mainPre + "/" + mainSuf + "/" + mainWord[1] + "/" + mainWord[2]);
            }
            Collections.sort(rules);
            String rulesStr = StringUtils.join(rules, "/");
            if(!result.containsKey(pre +"/" + suf)){
                result.put(pre +"/" + suf, new HashMap());
                resultWithCommon.put(pre +"/" + suf, new HashMap());
            }
            if(result.get(pre +"/" + suf).containsKey(rulesStr)){
                result.get(pre +"/" + suf).put(rulesStr, result.get(pre +"/" + suf).get(rulesStr) + 1);
            }else{
                result.get(pre +"/" + suf).put(rulesStr, 1);
            }
            if(!resultWithCommon.get(pre +"/" + suf).containsKey(rulesStr)){
                resultWithCommon.get(pre +"/" + suf).put(rulesStr, new HashSet());
            }
            resultWithCommon.get(pre +"/" + suf).get(rulesStr).add(common);
        }
        saveMappingCommon(new File("src/main/resources/mapping"), resultWithCommon);
    }
    
    public static int[] getCommon(String norm, String form){
        for (int i = 0; i < norm.length() - 2; i++) {
            String common = norm.substring(0, norm.length() - i);
            if(form.contains(common)){
                int index = form.indexOf(common);
                return new int[]{index, index + common.length()};
            }
        }
        return new int[]{form.length(), 0};
    }
    
    
    public static void saveMappingCommon(File file, Map<String, Map<String, Set<String>>> result) throws IOException{
        List<Mapping> list = new ArrayList();
        for (Map.Entry<String, Map<String, Set<String>>> entry : result.entrySet()) {
            Mapping mapping = Mapping.get(entry.getKey());
            for (Map.Entry<String, Set<String>> e : entry.getValue().entrySet()) {
                mapping.add(e.getKey(), e.getValue().toArray(new String[e.getValue().size()]));
            }
            list.add(mapping);
        }
        Collections.sort(list);
        FileUtils.writeLines(file, "utf-8", list);
    }
    
}
