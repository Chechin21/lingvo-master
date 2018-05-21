package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.model.Form;
/**
 Класс, проверяющий, является ли слово вводным
 @author ivan
 */
public class Conj {
    /**
     * Метод, проверяющий, является ли одной из форм омонима вводное слово, и определяющий, является ли оно первым в предложении
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
                if (forms[i].getAttrs().get(0).toString().equals("CONJ") && num == 0) {
                    //System.out.println(word + " " + "CONJ");
                    return i;
                }
            }
        }
        return -1;
    }
}
