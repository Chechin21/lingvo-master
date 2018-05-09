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

 * @author Dmitriy Malakhov
 */
public class AnnotTest {
    public static void main(String[] args) throws Exception {
        String annot = extract(new File("annot.opcorpora.no_ambig.nonmod.xml.zip"));
        Pattern tokenPattern = Pattern.compile("<token.*?text=\"(.*?)\">(.*?)</token>", Pattern.MULTILINE|Pattern.DOTALL);
        Pattern gPattern = Pattern.compile("<g.*?v=\"(.*?)\"", Pattern.MULTILINE|Pattern.DOTALL);
        Matcher mainMatcher = Pattern.compile("<source>(.*?)</source>.*?<tokens>(.*?)</tokens>", Pattern.MULTILINE|Pattern.DOTALL).matcher(annot);
        List<String> result = new ArrayList<String>();
        while(mainMatcher.find()){
            Matcher tokenMatcher = tokenPattern.matcher(mainMatcher.group(2));
            List<String> sent = new ArrayList<String>();
            while(tokenMatcher.find()){
                String word = tokenMatcher.group(1).replace('ё', 'е').replace("’", "").toLowerCase();
                List<Attr> attrs = new ArrayList<Attr>();
                Matcher gMatcher = gPattern.matcher(tokenMatcher.group(2));
                while(gMatcher.find()){
                    try{
                        attrs.add(Attr.valueOf(gMatcher.group(1)));
                    }catch(Exception e){}
                }
                if(!attrs.isEmpty()){
                    Collections.sort(attrs);
                    sent.add(word + " " + StringUtils.join(attrs, ","));
                }
            }
            if(!sent.isEmpty()){
                result.add(mainMatcher.group(1).replace("•", "").replace("\t", "") + "\t" + StringUtils.join(sent, "\t"));
            }
        }
        FileUtils.writeLines(new File("annot-opcorpora"), "utf-8", result);
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
