package com.github.stagirs.lingvo.morpho.model;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.rules.*;

public class Parser {
    public static void parse(String str) throws Exception {
        String[] replacements = {"«","»",")","(",":","—","\"",",","?","!","...",";","…"};
        String[] spl,norms;
        String word;
        int count,num;
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
            norms = MorphoAnalyst.predict(word).getNorms();
            if (word.equals("к") || word.equals("в") || word.equals("с")){
                System.out.println(word + " " + norms[0] + " " + "[PREP]" );
                continue;
            }
            if (count > 1) {
                AdvbVerb av = new AdvbVerb();
                NounAdjf na = new NounAdjf();
                Conj c = new Conj();
                InfVerb iv = new InfVerb();
                AdjfNoun an = new AdjfNoun();
                Prep p = new Prep();
                PrepVerb pv = new PrepVerb();
                Verbonly vo = new Verbonly();
                Stats st = new Stats();

                num = na.getForm(str,j);
                if (num != -1){
                    System.out.println(word + " " + norms[num] + " " + forms[num].getAttrs().toString());
                    continue;
                }
                num = av.getForm(str,j);
                if (num != -1){
                    System.out.println(word + " " + norms[num] + " " + forms[num].getAttrs().toString());
                    continue;
                }
                num = c.getForm(str,j);
                if (num != -1){
                    System.out.println(word + " " + norms[num] + " " + forms[num].getAttrs().toString());
                    continue;
                }
                num = iv.getForm(str,j);
                if (num != -1){
                    System.out.println(word + " " + norms[num] + " " + forms[num].getAttrs().toString());
                    continue;
                }
                num = an.getForm(str,j);

                if (num != -1){
                    System.out.println(word + " " + norms[num] + " " + forms[num].getAttrs().toString());
                    continue;
                }
                num = p.getForm(str,j);
                if (num == -2){
                    System.out.println(word + " " + norms[num] + " " + "[PREP]");
                    continue;
                }
                num = pv.getForm(str,j);
                if (num != -1){
                    System.out.println(word + " " + norms[num] + " " + forms[num].getAttrs().toString());
                    continue;
                }
                num = vo.getForm(str,j);
                if (num != -1){
                    System.out.println(word + " " + norms[num] + " " + forms[num].getAttrs().toString());
                    continue;
                }
                num = st.getForm(str,j);
                if (num != -1){
                    System.out.println(word + " " + norms[num] + " " + forms[num].getAttrs().toString());
                    continue;
                }
            } else {
                System.out.println(word + " " + norms[0] + " " + forms[0].getAttrs().toString() );
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Parser a = new Parser();
        a.parse("в нашей стране");
    }
}
