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
import com.github.stagirs.lingvo.morpho.model.Form;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Xml2PlainTest {
    public static void main(String[] args) throws Exception {
        String dictionary = extract(new File("dict.opcorpora.xml.zip"));
        Pattern normPattern = Pattern.compile("<l.*?t=\"(.*?)\">(.*?)</l>", Pattern.MULTILINE|Pattern.DOTALL);
        Pattern rawPattern = Pattern.compile("<f.*?t=\"(.*?)\">(.*?)</f>", Pattern.MULTILINE|Pattern.DOTALL);
        Pattern gPattern = Pattern.compile("<g.*?v=\"(.*?)\".*?/>", Pattern.MULTILINE|Pattern.DOTALL);
        Matcher m = Pattern.compile("<lemma id=\"(.*?)\".*?/lemma>", Pattern.MULTILINE|Pattern.DOTALL).matcher(dictionary);
        List<String> result = new ArrayList<String>();
        while(m.find()){
            String lemma = m.group(0);
            Matcher normMatcher = normPattern.matcher(lemma);
            if(normMatcher.find()){
                List<Attr> mainAttr = fillAttributes(gPattern.matcher(normMatcher.group(2)));
                if(mainAttr.contains(Attr.Init)){
                    continue;
                }
                List<String> forms = new ArrayList<String>();
                Matcher rawMatcher = rawPattern.matcher(lemma);
                while(rawMatcher.find()){
                    String word = rawMatcher.group(1).replace('ё', 'е').replace("’", "");
                    Form form = new Form(mainAttr, fillAttributes(gPattern.matcher(rawMatcher.group(2))));
                    forms.add(word + " " + StringUtils.join(form.getAttrs(), ","));
                }
                result.add(StringUtils.join(forms, " "));
            }
        }
        Collections.sort(result);
        FileUtils.writeLines(new File("forms_dictionary.txt"), "utf-8", result);
    }
    
    
    
    private static List<Attr> fillAttributes(Matcher attrMatcher){
        List<Attr> list = new ArrayList<Attr>();
        while(attrMatcher.find()){
            String attr = attrMatcher.group(1).replace("-", "_");
            if(Character.isDigit(attr.charAt(0))){
                attr = "N" + attr;
            }
            list.add(Attr.valueOf(attr));
        }
        Collections.sort(list);
        List<Attr> result = new ArrayList<Attr>();
        for (Attr val : list) {
            result.add(val);
        }
        return result;
    }
    
    private static String extract(File file) throws IOException{
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        try {
            ZipEntry ze = zis.getNextEntry();
            if(ze == null){
                throw new RuntimeException("can't unzip file");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(zis, baos);
            return new String(baos.toByteArray(), "utf-8");
        } finally {
            zis.close();
        }
    }
}
