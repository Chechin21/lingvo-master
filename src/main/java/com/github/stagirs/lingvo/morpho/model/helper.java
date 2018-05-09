package com.github.stagirs.lingvo.morpho.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class helper {
    public static void main(String[] args) throws Exception {
        FileReader fr = new FileReader("/Users/ivan/Desktop/sentences.txt");
        Scanner scan = new Scanner(fr);
        String str;
        String[] spl;
        FileWriter fw  = new FileWriter("/Users/ivan/Desktop/test.txt");
        int i  = 0;
        while (scan.hasNextLine()) {
            str = scan.nextLine();
            spl = str.split("   ");
            if (spl.length>1) {
                fw.write(str + '\n');
            }
        }
        fr.close();
        fw.close();
    }
}
