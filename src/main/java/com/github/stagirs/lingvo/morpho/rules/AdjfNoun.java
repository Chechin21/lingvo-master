package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.model.Form;
/**
 Класс, проверяющий, есть ли согласованность существительного с прилагательным
 @author ivan
 */
public class AdjfNoun {
    /**
     * Метод, проверяющий, является ли одной из форм омонима существительное, и определяющий, находится ли в окрестности этого  существительного прилагательное, к которому оно относится(совпадают род число падеж)
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
                if (forms[i].getAttrs().get(0).toString().equals("NOUN") ) {
                    if (num + 1 < str.length ) {
                        check = str[num + 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("ADJF")) {
                            //System.out.println(word + " " + check + " " + "NOUN");
                            return i;
                        }
                    }
                    if (num - 1 >= 0 ) {
                        check = str[num - 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("ADJF")) {
                            //System.out.println(check + " " + word + " " + "NOUN");
                            return i;
                        }
                    }
                    if (num + 2 < str.length ) {
                        check = str[num + 2];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("ADJF")) {
                            //System.out.println(word + " " + check + " " + "NOUN");
                            return i;
                        }
                    }
                    if (num - 2 >= 0 ) {
                        check = str[num - 2];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("ADJF")) {
                            //System.out.println(check + " " + word + " " + "NOUN");
                            return i;
                        }
                    }
                }


            }
        }
        return -1;
    }
}
