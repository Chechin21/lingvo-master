package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import  com.github.stagirs.lingvo.morpho.model.*;

public class AdvbVerb implements Rule{

    public int getForm(String spl,int num) {
        String[] str = spl.split(" ");
        String word = str[num].toLowerCase();
        String check;
        Form[] raws;
        int count = MorphoAnalyst.predict(word).getNormsCount();
        Form[] forms = MorphoAnalyst.predict(word).getRaws();
        if (count > 1 && !word.equals("к") && !word.equals("после")) {
            for(int i = 0;i < count;i++) {
                if (forms[i].getAttrs().get(0).toString().equals("ADVB")) {
                    if (num + 1 < str.length) {
                        check = str[num + 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("ADJS")) {
                            System.out.println(check + " " + word + " " + "ADVB");
                            //fw6.write(a + '\n');
                            //fw6.write(word + " " + check + " " + "ADVB");
                            //if(spl[tri].split(" ")[1].equals("ADVB")){
                            //    fw6.write(" good");
                            //    System.out.println("good");
                            //} else {
                            //    fw6.write(" wrong");
                            //    System.out.println("wrong");
                            // }
                            //fw6.write('\n');
                            //stop = false;
                            //down++;
                            return i;
                        }
                    }
                    if (num + 2 < str.length) {
                        check = str[num + 2];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN")) {
                            System.out.println(check + " " + word + " " + "ADVB");
                            return i;
                        }
                    }
                    if (num - 1 >= 0 ) {
                        check = str[num - 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("ADJS")) {
                            System.out.println(check + " " + word + " " + "ADVB");
                            return i;
                        }
                    }
                    if (num - 2 >= 0 ) {
                        check = str[num - 2];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN")) {
                            System.out.println(check + " " + word + " " + "ADVB");
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }
}
