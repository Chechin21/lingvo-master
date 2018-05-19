package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.model.Form;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Checker {
    public int checkit(Rule rule) throws Exception {
        FileReader fr = new FileReader("/Users/ivan/Desktop/test.txt");
        Scanner scan = new Scanner(fr);
        String word, check;
        String[] spl;
        Form[] raws, forms;
        String a;
        int num;
        int down=0,tri = 0;
        String[] str;
        FileWriter fw1 = new FileWriter("/Users/ivan/Desktop/checker.txt");
        while (scan.hasNextLine()) {
            tri = 0;
            spl = scan.nextLine().split("   ");
            a = spl[0];
            System.out.println(a);
            //System.out.println(a);
            str = a.split(" ");
            int count;
            for (int j = 0; j < str.length; j++) {
                word = str[j].toLowerCase();
                count = MorphoAnalyst.predict(word).getNormsCount();
                //System.out.print(word + " " + count);
                forms = MorphoAnalyst.predict(word).getRaws();
                if (count > 1) {
                    //System.out.println(word);
                    tri++;
                    num = rule.getForm(a,j);
                    if(num != -1 && num!= -2) {
                        fw1.write(a + '\n');
                        fw1.write(word + " " + forms[num].getAttrs().toString());
                        if (spl[tri].split(" ")[1].equals(forms[num].getAttrs().get(0).toString())) {
                            fw1.write(" good");
                            //System.out.println("good");
                        } else {
                            fw1.write(" wrong");
                            //System.out.println("wrong");
                        }
                        fw1.write('\n');
                    }
                    if (num == -2){
                        fw1.write(a + '\n');
                        fw1.write(word + " " + "PREP");
                        if (spl[tri].split(" ")[1].equals("PREP")) {
                            fw1.write(" good");
                            //System.out.println("good");
                        } else {
                            fw1.write(" wrong");
                            //System.out.println("wrong");
                        }
                        fw1.write('\n');
                    }
                }
            }
        }
        return 0;
    }
    public static void main(String[] args) throws Exception {
        Checker a = new Checker();
        AdvbVerb av = new AdvbVerb();
        NounAdjf na = new NounAdjf();
        System.out.println(a.checkit(na));
    }
}
