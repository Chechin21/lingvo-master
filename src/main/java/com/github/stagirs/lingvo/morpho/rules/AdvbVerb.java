package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import  com.github.stagirs.lingvo.morpho.model.*;
/**
Класс, проверяющий, есть ли согласованность наречия с глаголом
 @author ivan
*/
public class AdvbVerb implements Rule{
    /**
     * Метод, проверяющий, является ли одной из форм омонима наречие, и определяющий, находится ли в окрестности этого наречия глагол, к которому оно относится
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
        if (count > 1 && !word.equals("к") && !word.equals("после")) {
            for(int i = 0;i < count;i++) {
                if (forms[i].getAttrs().get(0).toString().equals("ADVB")) {
                    if (num + 1 < str.length) {
                        check = str[num + 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("ADJS")) {
                            //System.out.println(check + " " + word + " " + "ADVB");
                            return i;
                        }
                    }
                    if (num + 2 < str.length) {
                        check = str[num + 2];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN")) {
                            //System.out.println(check + " " + word + " " + "ADVB");
                            return i;
                        }
                    }
                    if (num - 1 >= 0 ) {
                        check = str[num - 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("ADJS")) {
                            //System.out.println(check + " " + word + " " + "ADVB");
                            return i;
                        }
                    }
                    if (num - 2 >= 0 ) {
                        check = str[num - 2];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN")) {
                            //System.out.println(check + " " + word + " " + "ADVB");
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }
}
