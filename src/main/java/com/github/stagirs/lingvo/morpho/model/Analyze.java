package com.github.stagirs.lingvo.morpho.model;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Analyze {
    public static void main(String[] args) throws Exception {
        String a = "Мы стали прислушиваться";
        FileReader fr = new FileReader("/Users/ivan/Desktop/sentences.txt");
        Scanner scan = new Scanner(fr);
        String word, check,statcheck;
        String[] spl;
        Form[] raws, forms,rawn;
        int down=0,tri = 0;
        boolean stop = true;
        boolean verb = true;
        String[] str;
        FileWriter fw1 = new FileWriter("/Users/ivan/Desktop/1verb.txt");
        FileWriter fw2 = new FileWriter("/Users/ivan/Desktop/NounAdjf.txt");
        FileWriter fw3 = new FileWriter("/Users/ivan/Desktop/AdjfNoun.txt");
        FileWriter fw4 = new FileWriter("/Users/ivan/Desktop/Prep.txt");
        FileWriter fw5 = new FileWriter("/Users/ivan/Desktop/PrepVerb.txt");
        FileWriter fw6 = new FileWriter("/Users/ivan/Desktop/AdvbVerb.txt");
        FileWriter fw7 = new FileWriter("/Users/ivan/Desktop/Conj.txt");
        FileWriter fw8 = new FileWriter("/Users/ivan/Desktop/InfnVerb.txt");
        FileWriter fw9 = new FileWriter("/Users/ivan/Desktop/Stats.txt");
        FileWriter fw10 = new FileWriter("/Users/ivan/Desktop/Samespeech.txt");
        /*
        Map<String,String> comp = new LinkedHashMap<String,String>();
        FileReader fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
        Scanner scanstat = new Scanner(fr1);
        while(scanstat.hasNextLine()){
            statcheck = scanstat.nextLine();
            spl = statcheck.split(" ");
            //System.out.println(spl[0]);
            if (!statcheck.isEmpty()) {
                System.out.println(spl[0]+ " " + spl[1]);
                comp.put(spl[0], spl[1]);
            } else{
                comp.put(" "," ");
            }
        }
        for (Map.Entry<String, String> entry : comp.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }*/

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
                stop = true;
                if (count > 1) {
                    System.out.println(word);
                    tri++;
                    //prep
                    for (int i = 0; i < count; i++) {
                        if ((forms[i].getAttrs().get(0).toString().equals("PREP") || word.equals("к") || word.equals("в")) && stop) {
                            System.out.println(word + " " + "PREP");
                            fw4.write(a + '\n' + word + " " + "PREP" );
                            if(spl[tri].split(" ")[1].equals("PREP")){
                                fw4.write(" good");
                                System.out.println("good");
                            } else {
                                fw4.write(" wrong");
                                System.out.println("wrong");
                            }
                            fw4.write('\n');
                            stop = false;
                            down++;
                        }
                    }
                    for (int i = 0; i < count; i++) {
                        if (!stop) {
                            break;
                        }
                        //System.out.println(forms[i].getAttrs().get(0));
                        //conj
                        if (forms[i].getAttrs().get(0).toString().equals("CONJ") && j == 0) {
                            fw7.write(a + '\n' + word + " " + "CONJ");
                            if(spl[tri].split(" ")[1].equals("CONJ")){
                                fw7.write(" good");
                                System.out.println("good");
                            } else {
                                fw7.write(" wrong");
                                System.out.println("wrong");
                            }
                            fw7.write('\n');
                            System.out.println(word + " " + "CONJ");
                            stop = false;
                            down++;
                        }
                        // nounadjf
                        if ((forms[i].getAttrs().get(0).toString().equals("ADJF") && stop)) {
                            if (j + 1 < str.length && stop) {
                                check = str[j + 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("NOUN")) {
                                    System.out.println(word + " " + check + " " + "ADFJ");
                                    fw2.write(a + '\n');
                                    fw2.write(word + " " + check + " " + "ADFJ");
                                    if(spl[tri].split(" ")[1].equals("ADJF")){
                                        fw2.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw2.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw2.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j - 1 >= 0 && stop) {
                                check = str[j - 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("NOUN")) {
                                    System.out.println(check + " " + word + " " + "ADFJ");
                                    fw2.write(a + '\n');
                                    fw2.write(check + " " + word + " " + "ADFJ");
                                    if(spl[tri].split(" ")[1].equals("ADJF")){
                                        fw2.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw2.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw2.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j + 2 < str.length && stop) {
                                check = str[j + 2];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("NOUN")) {
                                    System.out.println(word + " " + check + " " + "ADFJ");
                                    fw2.write(a + '\n');
                                    fw2.write(word + " " + check + " " + "ADFJ");
                                    if(spl[tri].split(" ")[1].equals("ADJF")){
                                        fw2.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw2.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw2.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j - 2 >= 0 && stop) {
                                check = str[j - 2];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("NOUN")) {
                                    System.out.println(check + " " + word + " " + "ADFJ");
                                    fw2.write(a + '\n');
                                    fw2.write(check + " " + word + " " + "ADFJ");
                                    if(spl[tri].split(" ")[1].equals("ADJF")){
                                        fw2.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw2.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw2.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                        }
                        //adjfnoun
                        if ((forms[i].getAttrs().get(0).toString().equals("NOUN") && stop)) {
                            if (j + 1 < str.length && stop) {
                                check = str[j + 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("ADJF")) {
                                    System.out.println(word + " " + check + " " + "NOUN");
                                    fw3.write(a + '\n');
                                    fw3.write(word + " " + check + " " + "NOUN");
                                    if(spl[tri].split(" ")[1].equals("NOUN")){
                                        fw3.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw3.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw3.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j - 1 >= 0 && stop) {
                                check = str[j - 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("ADJF")) {
                                    System.out.println(check + " " + word + " " + "NOUN");
                                    fw3.write(a + '\n');
                                    fw3.write(check + " " + word + " " + "NOUN");
                                    if(spl[tri].split(" ")[1].equals("NOUN")){
                                        fw3.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw3.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw3.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j + 2 < str.length && stop) {
                                check = str[j + 2];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("ADJF")) {
                                    System.out.println(word + " " + check + " " + "NOUN");
                                    fw3.write(a + '\n');
                                    fw3.write(word + " " + check + " " + "NOUN");
                                    if(spl[tri].split(" ")[1].equals("NOUN")){
                                        fw3.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw3.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw3.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j - 2 >= 0 && stop) {
                                check = str[j - 2];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (forms[i].intersect(raws[0]) >= 3 && raws[0].getAttrs().get(0).toString().equals("ADJF")) {
                                    System.out.println(check + " " + word + " " + "NOUN");
                                    fw3.write(a + '\n');
                                    fw3.write(check + " " + word + " " + "NOUN");
                                    if(spl[tri].split(" ")[1].equals("NOUN")){
                                        fw3.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw3.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw3.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                        }
                        //prep verb
                        if ((forms[i].getAttrs().get(0).toString().equals("VERB") && stop)) {
                            if (j - 1 >= 0) {
                                check = str[j - 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if ((raws[0].getAttrs().get(0).toString().equals("PREP") || check.equals("к") || check.equals("в")) && !check.equals("надо")) {
                                    if (i == 0) {
                                        System.out.println(check + " " + word + " " + forms[i + 1].getAttrs().get(0).toString());
                                        fw5.write(a + '\n');
                                        fw5.write(check + " " + word + " " + forms[i + 1].getAttrs().get(0).toString());
                                        if(spl[tri].split(" ")[1].equals(forms[i + 1].getAttrs().get(0).toString())){
                                            fw5.write(" good");
                                            System.out.println("good");
                                        } else {
                                            fw5.write(" wrong");
                                            System.out.println("wrong");
                                        }
                                        fw5.write('\n');
                                        stop = false;
                                        down++;
                                    } else {
                                        System.out.println(check + " " + word + " " + forms[i - 1].getAttrs().get(0).toString());
                                        fw5.write(a + '\n');
                                        fw5.write(check + " " + word + " " + forms[i - 1].getAttrs().get(0).toString());
                                        if(spl[tri].split(" ")[1].equals(forms[i - 1].getAttrs().get(0).toString())){
                                            fw5.write(" good");
                                            System.out.println("good");
                                        } else {
                                            fw5.write(" wrong");
                                            System.out.println("wrong");
                                        }
                                        fw5.write('\n');
                                        stop = false;
                                        down++;
                                    }
                                }
                            }
                        }
                        //verb1
                        if ((forms[i].getAttrs().get(0).toString().equals("VERB") || forms[i].getAttrs().get(0).toString().equals("INFN")) && stop) {
                            verb = true;
                            //System.out.println("here");
                            for (int k = 0; k < str.length; k++) {
                                check = str[k];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                //System.out.println(word + " " + raws[0].getAttrs().get(0).toString());
                                if (k != j && (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN"))) {
                                    verb = false;
                                }
                            }
                            if (verb) {
                                System.out.println(word + " " + forms[i].getAttrs().get(0).toString());
                                fw1.write(a + '\n');
                                fw1.write( word + " " + forms[i].getAttrs().get(0).toString());
                                if(spl[tri].split(" ")[1].equals(forms[i].getAttrs().get(0).toString())){
                                    fw1.write(" good");
                                    System.out.println("good");
                                } else {
                                    fw1.write(" wrong");
                                    System.out.println("wrong");
                                }
                                fw1.write('\n');
                                stop = false;
                                down++;
                            }
                        }
                        //advb verb
                        if (forms[i].getAttrs().get(0).toString().equals("ADVB") && stop) {
                            //System.out.println("here");
                            if (j + 1 < str.length && stop) {
                                check = str[j + 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("ADJS")) {
                                    System.out.println(check + " " + word + " " + "ADVB");
                                    fw6.write(a + '\n');
                                    fw6.write(word + " " + check + " " + "ADVB");
                                    if(spl[tri].split(" ")[1].equals("ADVB")){
                                        fw6.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw6.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw6.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j + 2 < str.length && stop) {
                                check = str[j + 2];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN")) {
                                    System.out.println(check + " " + word + " " + "ADVB");
                                    fw6.write(a + '\n');
                                    fw6.write(word + " " + check + " " + "ADVB");
                                    if(spl[tri].split(" ")[1].equals("ADVB")){
                                        fw6.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw6.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw6.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j - 1 >= 0 && stop) {
                                check = str[j - 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("ADJS")) {
                                    System.out.println(check + " " + word + " " + "ADVB");
                                    fw6.write(a + '\n');
                                    fw6.write(check + " " + word + " " + "ADVB");
                                    if(spl[tri].split(" ")[1].equals("ADVB")){
                                        fw6.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw6.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw6.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j - 2 >= 0 && stop) {
                                check = str[j - 2];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN")) {
                                    System.out.println(check + " " + word + " " + "ADVB");
                                    fw6.write(a + '\n');
                                    fw6.write(check + " " + word + " " + "ADVB");
                                    if(spl[tri].split(" ")[1].equals("ADVB")){
                                        fw6.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw6.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw6.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                        }
                        //infnverb
                        if (forms[i].getAttrs().get(0).toString().equals("INFN") && stop) {
                            if (j + 1 < str.length && stop) {
                                check = str[j + 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("ADJS")) {
                                    System.out.println(check + " " + word + " " + "INFN");
                                    fw8.write(a + '\n');
                                    fw8.write(word + " " + check + " " + "INFN");
                                    if(spl[tri].split(" ")[1].equals("INFN")){
                                        fw8.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw8.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw8.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j - 1 >= 0 && stop) {
                                check = str[j - 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (raws[0].getAttrs().get(0).toString().equals("VERB") || raws[0].getAttrs().get(0).toString().equals("INFN") || raws[0].getAttrs().get(0).toString().equals("ADJS")) {
                                    System.out.println(check + " " + word + " " + "INFN");
                                    fw8.write(a + '\n');
                                    fw8.write(check + " " + word + " " + "INFN");
                                    if(spl[tri].split(" ")[1].equals("INFN")){
                                        fw8.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw8.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw8.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                        }
                        if (forms[i].getAttrs().get(0).toString().equals("VERB") && stop) {
                            if (j + 1 < str.length && stop) {
                                check = str[j + 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (raws[0].getAttrs().get(0).toString().equals("INFN")) {
                                    System.out.println(check + " " + word + " " + "VERB");
                                    fw8.write(a + '\n');
                                    fw8.write(word + " " + check + " " + "VERB");
                                    if(spl[tri].split(" ")[1].equals("VERB")){
                                        fw8.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw8.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw8.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                            if (j - 1 >= 0 && stop) {
                                check = str[j - 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                if (raws[0].getAttrs().get(0).toString().equals("INFN")) {
                                    System.out.println(check + " " + word + " " + "VERB");
                                    fw8.write(a + '\n');
                                    fw8.write(check + " " + word + " " + "INFN");
                                    if(spl[tri].split(" ")[1].equals("VERB")){
                                        fw8.write(" good");
                                        System.out.println("good");
                                    } else {
                                        fw8.write(" wrong");
                                        System.out.println("wrong");
                                    }
                                    fw8.write('\n');
                                    stop = false;
                                    down++;
                                }
                            }
                        }
                    }/*
                    if (stop){
                        if(j-1 >=0 && j+1 < str.length) {
                            for (int i = 0; i < count; i++) {
                                FileReader fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
                                Scanner scanstat = new Scanner(fr1);
                                Scanner scanprev = new Scanner(fr1);
                                Scanner scanpost = new Scanner(fr1);
                                String stat, prev,next;
                                String[] helper;
                                Integer a1=0,a2=0,a3=0;
                                check = str[j + 1];
                                raws = MorphoAnalyst.predict(check).getRaws();
                                scanstat.next();
                                while (scanstat.hasNextLine()) {
                                    helper = scanprev.nextLine().split(" ");
                                    if (!helper[0].isEmpty()){
                                        prev = helper[1];
                                    } else {
                                        prev = "";
                                    }
                                    helper = scanstat.nextLine().split(" ");
                                    stat = helper[0];
                                    if (!stat.isEmpty()) {
                                        if (word.equals(stat) && helper[1].equals(forms[i].getAttrs().get(0).toString())) {
                                            next = scanstat.nextLine();
                                            if (!next.isEmpty()){
                                                if (next.split(" ")[1].equals(raws[0].getAttrs().get(0).toString())){
                                                    a1++;
                                                }
                                            }
                                            if (!prev.isEmpty()){
                                                check = str[j - 1];
                                                raws = MorphoAnalyst.predict(check).getRaws();
                                                if (prev.equals(raws[0].getAttrs().get(0).toString())){
                                                    a1++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }*/
                    if(forms[0].getAttrs().get(0).equals(forms[1].getAttrs().get(0))){
                        stop = false;
                        String[] helper;
                        String casee;
                        int[] a1=new int[count];
                        for (int i = 0; i < count; i++) {
                            a1[i]=0;
                        }
                        fw10.write(a + "\n" + word + " " + forms[0].getAttrs() + " " +  MorphoAnalyst.predict(word).getNorms()[0] + "\n" + word + " " + forms[1].getAttrs() + " " + MorphoAnalyst.predict(word).getNorms()[1] + "\n");
                        if (j-1 >= 0){
                            System.out.println("j-1>0");
                            check = str[j - 1];
                            raws = MorphoAnalyst.predict(check).getRaws();
                            if (raws[0].getAttrs().get(0).toString().equals("PREP") || check.equals("к") || check.equals("в") || check.equals("с")){
                                System.out.println("here");
                                FileReader fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstatfull.txt");
                                Scanner scanstat = new Scanner(fr1);
                                while (scanstat.hasNextLine()) {
                                    if (scanstat.nextLine().split(" ")[0].equals(check)){
                                        if(scanstat.hasNextLine()) {
                                            helper = scanstat.nextLine().split(",");
                                            casee = helper[helper.length-1];
                                            System.out.println(casee);
                                            for (int i = 0; i < count; i++) {
                                                if(forms[i].getAttrs().toString().contains(casee)){
                                                    a1[i]++;
                                                }
                                            }
                                        }
                                    }
                                }
                                fr1.close();
                            }
                        }
                        int max = a1[0],num=0;
                        for (int i = 0; i < count; i++) {
                            System.out.println(a1[i]);
                            if (a1[i]>max){
                                max = a1[i];
                                num = i;
                            }
                        }
                        fw10.write(forms[num].getAttrs() + " " + MorphoAnalyst.predict(word).getNorms()[0] + "\n");
                    }
                    if (stop){
                        if(j-1 >=0 && j+1 < str.length) {
                            stop = false;
                            fw9.write(a + '\n');
                            check = str[j + 1];
                            rawn = MorphoAnalyst.predict(check).getRaws();
                            check = str[j - 1];
                            raws = MorphoAnalyst.predict(check).getRaws();
                            int[] a1=new int[count];
                            int[] a2=new int[count];
                            for (int i = 0; i < count; i++) {
                                String[] helper;
                                FileReader fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
                                Scanner scanstat = new Scanner(fr1);
                                a1[i]=0;
                                a2[i]=0;
                                while (scanstat.hasNextLine()) {
                                    helper = scanstat.nextLine().split(" ");
                                    if(helper[0].equals(word)){
                                        if(helper[1].equals(forms[i].getAttrs().get(0).toString())){
                                            a2[i]++;
                                        }
                                    }
                                }
                                fr1.close();
                                fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
                                scanstat = new Scanner(fr1);
                                while (scanstat.hasNextLine()) {
                                    helper = scanstat.nextLine().split(" ");
                                    if (!helper[0].isEmpty()) {
                                        if(helper[1].equals(raws[0].getAttrs().get(0).toString())){
                                            if(scanstat.hasNextLine()) {
                                                helper = scanstat.nextLine().split(" ");
                                                if (!helper[0].isEmpty()) {
                                                    if (helper[1].equals(forms[i].getAttrs().get(0).toString())) {
                                                        if(scanstat.hasNextLine()) {
                                                            helper = scanstat.nextLine().split(" ");
                                                            if (!helper[0].isEmpty()) {
                                                                if (helper[1].equals(rawn[0].getAttrs().get(0).toString())) {
                                                                    a1[i]++;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                fw9.write(word + " " + forms[i].getAttrs().get(0).toString() + " " + a2[i] + " " + a1[i] + "   ");
                                if (i == count-1){
                                    int max1,max2,num1=0,num2=0,prevmax2=0,numprev2=0;
                                    max1 = a1[0];
                                    max2 = a2[0];
                                    for(int w=1;w<count;w++){
                                        if (a1[w]>max1){
                                            max1=a1[w];
                                            num1 = w;
                                        }
                                        if (a2[w]>max2){
                                            prevmax2 = max2;
                                            max2=a2[w];
                                            num2 = w;
                                        }
                                    }
                                    if (num1 == num2){
                                        if(spl[tri].split(" ")[1].equals(forms[num1].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + forms[num1].getAttrs().get(0).toString() + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + forms[num1].getAttrs().get(0).toString() + " wrong" + "\n");
                                            System.out.println("wrong");
                                        }
                                    } else if(max2 - prevmax2 > 5){
                                        if(spl[tri].split(" ")[1].equals(forms[num2].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + forms[num2].getAttrs().get(0).toString() + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + forms[num2].getAttrs().get(0).toString() + " wrong" + "\n");
                                            System.out.println("wrong");
                                        }
                                    } else {
                                        if(spl[tri].split(" ")[1].equals(forms[num1].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + forms[num1].getAttrs().get(0).toString() + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + forms[num1].getAttrs().get(0).toString() + " wrong" + "\n");
                                            System.out.println("wrong");
                                        }
                                    }
                                }
                                fr1.close();
                            }
                        }
                    }
                    if (stop){
                        if(j-1 >=0 && j-2 >= 0) {
                            stop = false;
                            fw9.write(a + '\n');
                            check = str[j - 1];
                            rawn = MorphoAnalyst.predict(check).getRaws();
                            check = str[j - 2];
                            raws = MorphoAnalyst.predict(check).getRaws();
                            int[] a1=new int[count];
                            int[] a2=new int[count];
                            for (int i = 0; i < count; i++) {
                                String[] helper;
                                FileReader fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
                                Scanner scanstat = new Scanner(fr1);
                                a1[i]=0;
                                a2[i]=0;
                                while (scanstat.hasNextLine()) {
                                    helper = scanstat.nextLine().split(" ");
                                    if(helper[0].equals(word)){
                                        if(helper[1].equals(forms[i].getAttrs().get(0).toString())){
                                            a2[i]++;
                                        }
                                    }
                                }
                                fr1.close();
                                fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
                                scanstat = new Scanner(fr1);
                                while (scanstat.hasNextLine()) {
                                    helper = scanstat.nextLine().split(" ");
                                    if (!helper[0].isEmpty()) {
                                        if(helper[1].equals(raws[0].getAttrs().get(0).toString())){
                                            if(scanstat.hasNextLine()) {
                                                helper = scanstat.nextLine().split(" ");
                                                if (!helper[0].isEmpty()) {
                                                    if (helper[1].equals(rawn[0].getAttrs().get(0).toString())) {
                                                        if(scanstat.hasNextLine()) {
                                                            helper = scanstat.nextLine().split(" ");
                                                            if (!helper[0].isEmpty()) {
                                                                if (helper[1].equals(forms[i].getAttrs().get(0).toString())) {
                                                                    a1[i]++;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                fw9.write(word + " " + forms[i].getAttrs().get(0).toString() + " " + a2[i] + " " + a1[i] + "   ");
                                if (i == count-1){
                                    int max1,max2,num1=0,num2=0,prevmax2=0,numprev2=0;
                                    max1 = a1[0];
                                    max2 = a2[0];
                                    for(int w=1;w<count;w++){
                                        if (a1[w]>max1){
                                            max1=a1[w];
                                            num1 = w;
                                        }
                                        if (a2[w]>max2){
                                            prevmax2 = max2;
                                            max2=a2[w];
                                            num2 = w;
                                        }
                                    }
                                    if (num1 == num2){
                                        if(spl[tri].split(" ")[1].equals(forms[num1].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("wrong");
                                        }
                                    } else if(max2 - prevmax2 > 5){
                                        if(spl[tri].split(" ")[1].equals(forms[num2].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("wrong");
                                        }
                                    } else {
                                        if(spl[tri].split(" ")[1].equals(forms[num1].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("wrong");
                                        }
                                    }
                                }
                                fr1.close();
                            }
                        }
                    }
                    if (stop){
                        if(j+2 < str.length && j+1 < str.length) {
                            stop = false;
                            fw9.write(a + '\n');
                            check = str[j + 2];
                            rawn = MorphoAnalyst.predict(check).getRaws();
                            check = str[j + 1];
                            raws = MorphoAnalyst.predict(check).getRaws();
                            int[] a1=new int[count];
                            int[] a2=new int[count];
                            for (int i = 0; i < count; i++) {
                                String[] helper;
                                FileReader fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
                                Scanner scanstat = new Scanner(fr1);
                                a1[i]=0;
                                a2[i]=0;
                                while (scanstat.hasNextLine()) {
                                    helper = scanstat.nextLine().split(" ");
                                    if(helper[0].equals(word)){
                                        if(helper[1].equals(forms[i].getAttrs().get(0).toString())){
                                            a2[i]++;
                                        }
                                    }
                                }
                                fr1.close();
                                fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
                                scanstat = new Scanner(fr1);
                                while (scanstat.hasNextLine()) {
                                    helper = scanstat.nextLine().split(" ");
                                    if (!helper[0].isEmpty()) {
                                        if(helper[1].equals(forms[i].getAttrs().get(0).toString())){
                                            if(scanstat.hasNextLine()) {
                                                helper = scanstat.nextLine().split(" ");
                                                if (!helper[0].isEmpty()) {
                                                    if (helper[1].equals(raws[0].getAttrs().get(0).toString())) {
                                                        if(scanstat.hasNextLine()) {
                                                            helper = scanstat.nextLine().split(" ");
                                                            if (!helper[0].isEmpty()) {
                                                                if (helper[1].equals(rawn[0].getAttrs().get(0).toString())) {
                                                                    a1[i]++;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                fw9.write(word + " " + forms[i].getAttrs().get(0).toString() + " " + a2[i] + " " + a1[i] + "   ");
                                if (i == count-1){
                                    int max1,max2,num1=0,num2=0,prevmax2=0,numprev2=0;
                                    max1 = a1[0];
                                    max2 = a2[0];
                                    for(int w=1;w<count;w++){
                                        if (a1[w]>max1){
                                            max1=a1[w];
                                            num1 = w;
                                        }
                                        if (a2[w]>max2){
                                            prevmax2 = max2;
                                            max2=a2[w];
                                            num2 = w;
                                        }
                                    }
                                    if (num1 == num2){
                                        if(spl[tri].split(" ")[1].equals(forms[num1].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("wrong");
                                        }
                                    } else if(max2 - prevmax2 > 5){
                                        if(spl[tri].split(" ")[1].equals(forms[num2].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("wrong");
                                        }
                                    } else {
                                        if(spl[tri].split(" ")[1].equals(forms[num1].getAttrs().get(0).toString())){
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("good");
                                        } else {
                                            fw9.write( "\n" + " good" + "\n");
                                            System.out.println("wrong");
                                        }
                                    }
                                }
                                fr1.close();
                            }
                        }
                    }
                }
            }
        }
        System.out.println(down);
        fr.close();
        fw1.close();
        fw2.close();
        fw3.close();
        fw4.close();
        fw5.close();
        fw6.close();
        fw7.close();
        fw8.close();
        fw9.close();
        fw10.close();
    }
}
