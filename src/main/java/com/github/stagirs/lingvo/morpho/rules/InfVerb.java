package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.model.Form;
/**
 Класс, проверяющий, есть ли согласованность инфнинитива с глаголом
 @author ivan
 */
public class InfVerb {
    /**
     * Метод, проверяющий, является ли одной из форм омонима инфинитив(глагол), и определяющий, находится ли в окрестности этого наречия глагол(инфинитив)
     * @param spl - предложение
     * @param num - номер слова, которое имеет более чем 1 нормальную форму
     * @return int - номер корректной граммемы, если она определена, -1 в ином случае
     */
    public int getForm(String spl,int num) {
        String[] str = spl.split(" ");
        String word = str[num].toLowerCase();
        String check;
        Form[] raws;
        int count = MorphoAnalyst.predict(word).getNormsCount();
        Form[] forms = MorphoAnalyst.predict(word).getRaws();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                if (forms[i].getAttrs().get(0).toString().equals("INFN")  ) {
                    if ( num + 1 < str.length  ) {
                        check = str[ num + 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("AD numS")) {
                            //System.out.println(check + " " + word + " " + "INFN");
                            return i;
                        }
                    }
                    if ( num - 1 >= 0  ) {
                        check = str[ num - 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("AD numS")) {
                            //System.out.println(check + " " + word + " " + "INFN");
                            return i;
                        }
                    }
                }
                if (forms[i].getAttrs().get(0).toString().equals("VERB")  ) {
                    if ( num + 1 < str.length  ) {
                        check = str[ num + 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("INFN")) {
                           // System.out.println(check + " " + word + " " + "VERB");
                            return i;
                        }
                    }
                    if ( num - 1 >= 0  ) {
                        check = str[ num - 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("INFN")) {
                            //System.out.println(check + " " + word + " " + "VERB");
                            return i;
                        }
                    }
                }

            }
        }
        return -1;
    }
}
