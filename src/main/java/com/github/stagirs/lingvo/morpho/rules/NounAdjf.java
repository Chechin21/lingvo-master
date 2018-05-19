package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.model.Form;

public class NounAdjf implements Rule {

    public int getForm(String spl,int num) {
        String[] str = spl.split(" ");
        String word = str[num].toLowerCase();
        String check;
        Form[] raws;
        int count = MorphoAnalyst.predict(word).getNormsCount();
        Form[] forms = MorphoAnalyst.predict(word).getRaws();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                if (forms[i].getAttrs().get(0).toString().equals("ADJF") ) {
                    if (num + 1 < str.length ) {
                        check = str[num + 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("NOUN")) {
                            System.out.println(word + " " + check + " " + "ADFJ");
                            return i;
                        }
                    }
                    if (num - 1 >= 0 ) {
                        check = str[num - 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("NOUN")) {
                            System.out.println(check + " " + word + " " + "ADFJ");
                            return i;
                        }
                    }
                    if (num + 2 < str.length ) {
                        check = str[num + 2];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("NOUN")) {
                            System.out.println(word + " " + check + " " + "ADFJ");
                            return i;
                        }
                    }
                    if (num - 2 >= 0 ) {
                        check = str[num - 2];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("NOUN")) {
                            System.out.println(check + " " + word + " " + "ADFJ");
                            return i;
                        }
                    }
                }
            }
        }
        return -1;

    }
}
