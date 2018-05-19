package com.github.stagirs.lingvo.morpho.model;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;

public class Parser {
    public static void parse(String str) throws Exception {
        String[] replacements = {"«","»",")","(",":","—","\"",",","?","!","...",";","…"};
        String[] spl;
        String word;
        int count;
        Form[] raws, forms;
        for(int i = 0;i < replacements.length;i++){
            str = str.replace(replacements[i],"");
        }
        str = str.replace("   "," ").replace("  "," ").trim();
        spl = str.split(" ");
        for (int j = 0; j < spl.length; j++) {
            word = spl[j].toLowerCase();
            count = MorphoAnalyst.predict(word).getNormsCount();
            forms = MorphoAnalyst.predict(word).getRaws();
            if (count > 1) {

            } else {
                System.out.println(word + );
            }
        }
    }
}
