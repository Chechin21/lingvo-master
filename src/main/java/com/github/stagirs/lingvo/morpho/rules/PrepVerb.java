package com.github.stagirs.lingvo.morpho.rules;
/**
 Класс, проверяющий, есть ли согласованность предлога с глаголом(после предлога глагол не может стоять)
 @author ivan
 */
import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.model.Form;

public class PrepVerb {
    /**
     * Метод, проверяющий, является ли одной из форм омонима глагол, и проверяющий, не стоит ли этот омоним после предлога
     * @param spl - предложение
     * @param num - номер слова, которое имеет более чем 1 нормальную форму
     * @return int - номер корректной граммемы, если она определена, -1 в ином случае
     */
    public int getForm(String spl, int num) {
        String[] str = spl.split(" ");
        String word = str[num].toLowerCase();
        String check;
        Form[] raws;
        int count = MorphoAnalyst.predict(word).getNormsCount();
        Form[] forms = MorphoAnalyst.predict(word).getRaws();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                if (forms[i].getAttrs().get(0).toString().equals("VERB") ) {
                    if (num - 1 >= 0) {
                        check = str[num - 1];
                        raws = MorphoAnalyst.predict(check).getRaws();
                        if ((raws[0].getAttrs().get(0).toString().equals("PREP") || check.equals("к") || check.equals("в")) && !check.equals("надо")) {
                            if (i == 0) {
                               // System.out.println(check + " " + word + " " + forms[i + 1].getAttrs().get(0).toString());
                                return i+1;
                            } else {
                                //System.out.println(check + " " + word + " " + forms[i - 1].getAttrs().get(0).toString());
                                return i-1;
                            }
                        }
                    }
                }
            }
        }
        return -1;

    }
}
