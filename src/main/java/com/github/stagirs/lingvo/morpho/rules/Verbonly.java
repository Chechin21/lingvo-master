package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.model.Form;

public class Verbonly {
    public int getForm(String spl, int num) {
        String[] str = spl.split(" ");
        String word = str[num].toLowerCase();
        String check;
        Form[] raws;
        int count = MorphoAnalyst.predict(word).getNormsCount();
        Form[] forms = MorphoAnalyst.predict(word).getRaws();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                if ((forms[i].getAttrs().get(0).toString().equals("VERB") || forms[i].getAttrs().get(0).toString().equals("INFN")) && num !=0) {
                    boolean verb = true;
                    //System.out.println("here");
                    for (int k = 0; k < str.length; k++) {
                        check = str[k];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        //System.out.println(word + " " + raws[0].getAttrs().get(0).toString());
                        if (k != num && (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN"))) {
                            verb = false;
                        }
                    }
                    if (verb) {
                        //System.out.println(word + " " + forms[i].getAttrs().get(0).toString());
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}
