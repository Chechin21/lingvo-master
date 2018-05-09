package com.github.stagirs.lingvo.morpho.model;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import mystem.AttrConverter;
import mystem.MorphoToken;
import mystem.Efficiency;
import com.github.stagirs.lingvo.morpho.*;
import com.github.stagirs.lingvo.morpho.model.Attr;
import com.github.stagirs.lingvo.morpho.model.Form;

public class preparemystem {
    public static void main(String[] args) throws Exception {
        int count;
        AttrConverter attr = new AttrConverter();
        String word = "были";
        count = MorphoAnalyst.predict(word).getNormCount();
        System.out.println(word + " " + count);
        Form[] k = MorphoAnalyst.predict(word).getRaws();
        String[] s = MorphoAnalyst.predict(word).getNorms();
        for (int i = 0; i < k.length; i++) {
            System.out.println(s[i] + " " + k[i].getAttrs().toString());
        }
        word = "к";
        Form[] k1 = MorphoAnalyst.predict(word).getRaws();
        String[] s1 = MorphoAnalyst.predict(word).getNorms();
        for (int i = 0; i < k1.length; i++) {
            System.out.println(s1[i] + " " + k1[i].getAttrs().toString());
        }
        //System.out.println(k1[0].intersect(k[0]));
        //System.out.println(k1[0].intersect(k[1]));
        String[] replacements = {"«","»",".",")","(",":","—","\"",",","?","!","...",";","…"};
        String[] check = {"NOUN","PREP","CONJ","INFN","VERB","ADJF","ADJS","ADVB","COMP","PRTF","PRTS","GRND","NUMR","NPRO","PRED","PRCL","INTJ"};
        String str= " ";
        String[] spl;
        String help;
        boolean ok = true,tmp = false;
        if ('w' < 'а' || 'w' > 'я'){
            System.out.println("good");
        }
        String source = "/Users/ivan/Downloads/annot.opcorpora.no_ambig_strict.xml";
        FileWriter fw = new FileWriter("/Users/ivan/Desktop/sentencesformystem.txt");
        try {
            XMLStreamReader xmlr = XMLInputFactory.newInstance().createXMLStreamReader(source, new FileInputStream(source));
            while (xmlr.hasNext()) {
                xmlr.next();
                if (xmlr.getEventType() == 1) {
                    if (xmlr.getName().toString().equals("source")) {
                        str = xmlr.getElementText();
                        for(int i = 0;i < replacements.length;i++){
                            str = str.replace(replacements[i],"");
                        }
                        str = str.replace("   "," ").replace("  "," ").trim();
                        spl = str.split(" ");
                        ok = true;
                        if (spl.length < 3){
                            ok = false;
                        }
                        if(spl.length >= 3) {
                            for (int i = 0; i < spl.length; i++) {
                                if (spl[i].toLowerCase().charAt(0) < 'а' || spl[i].toLowerCase().charAt(0) > 'я') {
                                    ok = false;
                                }
                            }
                            if (ok) {
                                //System.out.println();
                                fw.write("\n");
                                fw.write(str);
                                //System.out.print(str);
                            }
                        }
                        //System.out.println(str.replace("  "," "));

                        //System.out.println(xmlr.getAttributeValue(1));
                    }
                }
                if (ok) {
                    if (xmlr.isStartElement() && xmlr.getName().toString() == "tfr") {
                        word = xmlr.getAttributeValue(1).replace('ё', 'е');
                        xmlr.next();
                        xmlr.next();
                        MorphoToken correct = new MorphoToken();
                        correct.setLexema(xmlr.getAttributeValue(1).toLowerCase().replace('ё', 'е'));
                        xmlr.next();
                        Set<String> gramems = new HashSet<String>();
                        while (xmlr.isStartElement() && xmlr.getName().toString() == "g") {
                            Set<String> stm = attr.sourceToMystem(xmlr.getAttributeValue(0));
                            System.out.println(stm);
                            if (stm != null) {
                                gramems.addAll(stm);
                            }
                            xmlr.next();
                            xmlr.next();
                        }

                        if (gramems.isEmpty() /*|| gramems.contains("V")*/) {
                            continue;
                        }

                        for (String i : attr.getParts()) {
                            //System.out.println(i);
                            //if(stm.contains("прич")) System.out.println("kk");
                            if (gramems.contains(i)) {
                                correct.setPart(i);
                                //System.out.println("d " + i);
                            }
                        }
                        //System.out.println();
                        if (correct.getPart() == null) {
                            correct.setPart("Other");
                        }
                        System.out.println(gramems);
                        correct.setGramems(gramems);
                        fw.write("   " + correct.getLexema() + "  " + correct.getGramems());
                    }
                }
            }
        }   catch (XMLStreamException | FactoryConfigurationError | IOException  e) {
            e.printStackTrace();
        }
        fw.close();
    }
}
