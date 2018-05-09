package com.github.stagirs.lingvo.morpho.model;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Analyze {
    public static void main(String[] args) throws Exception {
        String a = "Мы стали прислушиваться";
        FileReader fr = new FileReader("/Users/ivan/Desktop/test.txt");
        Scanner scan = new Scanner(fr);
        String word, check;
        String[] spl;
        Form[] raws, forms;
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
    }
}
