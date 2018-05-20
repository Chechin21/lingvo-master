package com.github.stagirs.lingvo.morpho.rules;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.github.stagirs.lingvo.morpho.model.Form;

import java.io.FileReader;
import java.util.Scanner;

public class Stats {
    public int getForm(String spl,int num) throws Exception{
        String[] str = spl.split(" ");
        String word = str[num].toLowerCase();
        String check;
        Form[] raws,rawn;
        boolean stop = true;
        int count = MorphoAnalyst.predict(word).getNormsCount();
        Form[] forms = MorphoAnalyst.predict(word).getRaws();
        if (count > 1) {
            if(forms[0].getAttrs().get(0).equals(forms[1].getAttrs().get(0))){
                stop = false;
                String[] helper;
                String casee;
                int[] a1=new int[count];
                for (int i = 0; i < count; i++) {
                    a1[i]=0;
                }if (num-1 >= 0){
                    System.out.println("j-1>0");
                    check = str[num - 1];
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
                int max = a1[0];
                for (int i = 0; i < count; i++) {
                    System.out.println(a1[i]);
                    if (a1[i]>max){
                        max = a1[i];
                        num = i;
                    }
                }
            }
            if(num-1 >=0 && num+1 < str.length) {
                stop = false;
                check = str[num + 1];
                rawn = MorphoAnalyst.predict(check).getRaws();
                check = str[num - 1];
                raws = MorphoAnalyst.predict(check).getRaws();
                int[] a1 = new int[count];
                int[] a2 = new int[count];
                for (int i = 0; i < count; i++) {
                    String[] helper;
                    FileReader fr1 = new FileReader("/Users/ivan/Desktop/sentencesforstat.txt");
                    Scanner scanstat = new Scanner(fr1);
                    a1[i] = 0;
                    a2[i] = 0;
                    while (scanstat.hasNextLine()) {
                        helper = scanstat.nextLine().split(" ");
                        if (helper[0].equals(word)) {
                            if (helper[1].equals(forms[i].getAttrs().get(0).toString())) {
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
                            if (helper[1].equals(raws[0].getAttrs().get(0).toString())) {
                                if (scanstat.hasNextLine()) {
                                    helper = scanstat.nextLine().split(" ");
                                    if (!helper[0].isEmpty()) {
                                        if (helper[1].equals(forms[i].getAttrs().get(0).toString())) {
                                            if (scanstat.hasNextLine()) {
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
                    if (i == count - 1) {
                        fr1.close();
                        int max1, max2, num1 = 0, num2 = 0, prevmax2 = 0, numprev2 = 0;
                        max1 = a1[0];
                        max2 = a2[0];
                        for (int w = 1; w < count; w++) {
                            if (a1[w] > max1) {
                                max1 = a1[w];
                                num1 = w;
                            }
                            if (a2[w] > max2) {
                                prevmax2 = max2;
                                max2 = a2[w];
                                num2 = w;
                            }
                        }
                        if (num1 == num2) {
                            return num1;
                        } else if (max2 - prevmax2 > 5) {
                            return num2;
                        } else {
                            return num1;
                        }
                    }
                }
            }
            if(num-1 >=0 && num-2 >= 0) {
                stop = false;
                check = str[num - 1];
                rawn = MorphoAnalyst.predict(check).getRaws();
                check = str[num - 2];
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
                    if (i == count-1){
                        fr1.close();
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
                            return num1;
                        } else if(max2 - prevmax2 > 5){
                            return num2;
                        } else {
                            return num1;
                        }
                    }
                }
            }
            if(num+2 < str.length && num+1 < str.length) {
                stop = false;
                check = str[num + 2];
                rawn = MorphoAnalyst.predict(check).getRaws();
                check = str[num + 1];
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
                    if (i == count-1){
                        fr1.close();
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
                            return num1;
                        } else if(max2 - prevmax2 > 5){
                            return num2;
                        } else {
                            return num1;
                        }
                    }
                }
            }
        }
        return -1;
    }
}
