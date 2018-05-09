package mystem;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.*;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.github.stagirs.lingvo.morpho.*;
import com.github.stagirs.lingvo.morpho.model.Attr;
import com.github.stagirs.lingvo.morpho.model.Form;

public class Efficiency {

    private final static boolean DEBUG_LINGVO = true;
    private final static boolean DEBUG_MYSTEM = true;

    private static int countLex = 0;
    private static Map<String, Integer> countAll;
    private static Map<String, Integer> countCorrLingvoLex;
    private static Map<String, Integer> countCorrMystemLex;
    private static Map<String, Double> countCorrLingvoGr;
    private static Map<String, Double> countCorrMystemGr;
    private static AttrConverter attr = new AttrConverter();
    private static Process process;

    private static void LingvoCorrectPercent(String word, MorphoToken correct)
    {
        List<String> notAcc = new ArrayList<String>();
        String[] norms = MorphoAnalyst.predict(word).getNorms();
        Form[] forms = MorphoAnalyst.predict(word).getRaws();
        double cor = 0;
        int corL = 0;
        if (norms.length == 1){
            cor = 1;
            corL = 1;
        } else {
                //if (!correct.getGramems().contains("V") && !norms[i].toLowerCase().equals(correct.getLexema())) {
                //    continue;
                //}
                if (norms[0].toLowerCase().equals(correct.getLexema())) {
                    corL = 1;
                }
                List<Attr> attrs = forms[0].getAttrs();
                Set<String> gr = new HashSet<String>();

                for (Attr a : attrs) {
                    if (attr.lingvoToMystem(a.getDescription()) != null) {
                        gr.addAll(attr.lingvoToMystem(a.getDescription()));
                    }
                }
                //System.out.println(word + " " + gr.toString());

                if (correct.getGramems().equals(gr)) {
                    cor = 1;
                } else if (correct.getGramems().containsAll(gr)) {
                    double tmp = (correct.getGramems().size() - gr.size()) * 1. / correct.getGramems().size();
                    if (tmp > cor) {
                        cor = tmp;
                    }
                    if (DEBUG_LINGVO) {
                        notAcc.add("частично верно: " + correct.getLexema() + " " + correct.getGramems().toString() + " " + norms[0] + " " + gr.toString());
                    }
                } else if (DEBUG_LINGVO) {
                    notAcc.add("неверно: " + correct.getLexema() + " " + correct.getGramems().toString() + " " + norms[0] + " " + gr.toString());
                }

            if (DEBUG_LINGVO) {
                if (cor < 1) {
                    System.out.println("LINGVO:");
                    System.out.println(word + " " + correct.getGramems().toString());
                    for (String na : notAcc) {
                        System.out.println(na);
                    }
                    System.out.println();
                }
            }
        }
        countCorrLingvoGr.put(correct.getPart(), countCorrLingvoGr.get(correct.getPart()).doubleValue() + cor);
        countCorrLingvoLex.put(correct.getPart(), countCorrLingvoLex.get(correct.getPart()).intValue() + corL);
    }

    private static void MystemCorrectPercent(String word, MorphoToken correct)
    {
        List<String> notAcc = new ArrayList<String>();
        double cor = 0;
        int corL = 0;
        try {
            if(process == null){
                ProcessBuilder pb = new ProcessBuilder("./mystem","-n","-i","-d", "--format=xml"/*, "-e", "cp1251"*/);
                pb.directory(new File("/Users/ivan/Downloads/"));
                process = pb.start();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(process.getOutputStream(), "utf-8"));
            pw.println(word);
            pw.println(word);
            pw.flush();
            Thread.sleep(0, 500);
            String s = br.readLine();
            while(!s.contains("<w>")){
                s = br.readLine();
            }
            byte[] bytes = s.getBytes(Charset.forName("utf-8"));

            InputStream stringStream = new ByteArrayInputStream(bytes);

            XMLStreamReader xmlr = XMLInputFactory.newInstance().createXMLStreamReader(stringStream);

            while(xmlr.hasNext()) {
                if(xmlr.isStartElement() && xmlr.getName().toString() == "ana"){
                    if(xmlr.getAttributeValue(0).toLowerCase().equals(correct.getLexema())){
                        corL = 1;
                    } else if(!correct.getGramems().contains("V")){
                        xmlr.next();
                        continue;
                    }

                    Set<String> gr = new HashSet<String>();
                    StringTokenizer st = new StringTokenizer(xmlr.getAttributeValue(xmlr.getAttributeCount() - 1), ",=");
                    while(st.hasMoreTokens()){
                        String gram = st.nextToken();
                        if(attr.isMystemAttr(gram)){
                            gr.add(gram);

                        }
                    }

                    if(gr.contains("APRO") || gr.contains("ANUM")){
                        gr.add("A");
                        if(!gr.contains("сокр")){
                            gr.add("полн");
                        }
                    }

                    if(correct.getGramems().equals(gr)){
                        cor = 1;
                        break;
                    } else if(correct.getGramems().containsAll(gr)){
                        double tmp = (correct.getGramems().size() - gr.size()) * 1. / correct.getGramems().size();
                        if(tmp > cor){
                            cor = tmp;
                        }
                        if(DEBUG_MYSTEM){
                            notAcc.add("частично верно: " + correct.getLexema() + " " + correct.getGramems().toString() + " " + gr.toString());
                        }
                    } else if(DEBUG_MYSTEM) {
                        notAcc.add("неверно: " + correct.getLexema() + " " + correct.getGramems().toString() + " " + gr.toString());
                    }
                }
                xmlr.next();
            }
        } catch (XMLStreamException | FactoryConfigurationError | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if(DEBUG_MYSTEM) {
            if (cor < 1) {
                System.out.println("MYSTEM:");
                System.out.println(word + " " + correct.getGramems().toString());
                for(String na : notAcc){
                    System.out.println(na);
                }
                System.out.println();
            }
        }

        countCorrMystemGr.put(correct.getPart(), countCorrMystemGr.get(correct.getPart()).doubleValue() + cor);
        countCorrMystemLex.put(correct.getPart(), countCorrMystemLex.get(correct.getPart()).intValue() + corL);
    }
    private static void MystemCorrectPercentnew(String word, MorphoToken[] correct)
    {
        List<String> notAcc = new ArrayList<String>();
        System.out.println("НОВОЕ ПРЕДЛОЖЕНИЕ  " + word);
        for (int a=0;a<correct.length;a++){
            System.out.print(" " + correct[a].getLexema() + " " + correct[a].getPart());
        }
        System.out.println();
        System.out.println();
        double cor = 0;
        int corL = 0;
        int num = 0;
        int count = word.split(" ").length;
        try {
            if(process == null){
                ProcessBuilder pb = new ProcessBuilder("./mystem","-n","-i","-d", "--format=xml"/*, "-e", "cp1251"*/);
                pb.directory(new File("/Users/ivan/Downloads/"));
                process = pb.start();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(process.getOutputStream(), "utf-8"));
            pw.println(word);
            pw.println(word);
            pw.flush();
            Thread.sleep(200);
            for (int i = 0;i < count;i++) {
                String s = br.readLine();
                while (!s.contains("<w>")) {
                    s = br.readLine();
                }
                System.out.println(s);
                byte[] bytes = s.getBytes(Charset.forName("utf-8"));

                InputStream stringStream = new ByteArrayInputStream(bytes);

                XMLStreamReader xmlr = XMLInputFactory.newInstance().createXMLStreamReader(stringStream);
                /*while (xmlr.hasNext()){
                    if (xmlr.isStartElement() && xmlr.getName().toString() == "ana") {
                        System.out.println(xmlr.getAttributeValue(0));
                    }
                    xmlr.next();
                }*/

                while (xmlr.hasNext()) {
                    if (xmlr.isStartElement() && xmlr.getName().toString() == "ana") {
                        //System.out.println(xmlr.getAttributeValue(0).toLowerCase() + " " + i);
                        if (xmlr.getAttributeValue(0).toLowerCase().equals(correct[i].getLexema())) {
                            corL = 1;
                        } else if (!correct[i].getGramems().contains("V")) {
                            System.out.println(word);
                            System.out.println(xmlr.getAttributeValue(0).toLowerCase() + " " + correct[i].getLexema() + " error" );
                            xmlr.next();
                            continue;
                        }

                        Set<String> gr = new HashSet<String>();
                        StringTokenizer st = new StringTokenizer(xmlr.getAttributeValue(xmlr.getAttributeCount() - 1), ",=");
                        while (st.hasMoreTokens()) {
                            String gram = st.nextToken();
                            if (attr.isMystemAttr(gram)) {
                                gr.add(gram);

                            }
                        }

                        if (gr.contains("APRO") || gr.contains("ANUM")) {
                            gr.add("A");
                            if (!gr.contains("сокр")) {
                                gr.add("полн");
                            }
                        }

                        if (correct[i].getGramems().equals(gr)) {
                            cor = 1;
                            break;
                        } else if (correct[i].getGramems().containsAll(gr)) {
                            double tmp = (correct[i].getGramems().size() - gr.size()) * 1. / correct[i].getGramems().size();
                            if (tmp > cor) {
                                cor = tmp;
                            }
                            if (DEBUG_MYSTEM) {
                                notAcc.add("частично верно: " + correct[i].getLexema() + " " + correct[i].getGramems().toString() + " " + gr.toString());
                            }
                        } else if (DEBUG_MYSTEM) {
                            notAcc.add("неверно: " + correct[i].getLexema() + " " + correct[i].getGramems().toString() + " " + gr.toString());

                        }

                    }
                    xmlr.next();
                }
                if(DEBUG_MYSTEM) {
                    if (cor < 1) {
                        System.out.println("MYSTEM:");
                        //System.out.println(word + " " + correct.getGramems().toString());
                        for(String na : notAcc){
                            System.out.println(na);
                        }
                        System.out.println();
                    }
                }
                countCorrMystemGr.put(correct[i].getPart(), countCorrMystemGr.get(correct[i].getPart()).doubleValue() + cor);
                countCorrMystemLex.put(correct[i].getPart(), countCorrMystemLex.get(correct[i].getPart()).intValue() + corL);
            }
        } catch (XMLStreamException | FactoryConfigurationError | IOException | InterruptedException e) {
            e.printStackTrace();
        }/*
        if(DEBUG_MYSTEM) {
            if (cor < 1) {
                System.out.println("MYSTEM:");
                //System.out.println(word + " " + correct.getGramems().toString());
                for(String na : notAcc){
                    System.out.println(na);
                }
                System.out.println();
            }
        }*/


        //countCorrMystemGr.put(correct.getPart(), countCorrMystemGr.get(correct.getPart()).doubleValue() + cor);
        //countCorrMystemLex.put(correct.getPart(), countCorrMystemLex.get(correct.getPart()).intValue() + corL);
    }

    public static void main(String[] args) throws IOException {
        countAll = new HashMap<>();
        countCorrLingvoLex = new HashMap<>();
        countCorrMystemLex = new HashMap<>();
        countCorrLingvoGr = new HashMap<>();
        countCorrMystemGr = new HashMap<>();
        String str;
        String[] spl;
        int tri;
        Boolean sen = false;
        for(String i : attr.getParts()){
            countAll.put(i, 0);
            countCorrLingvoLex.put(i, 0);
            countCorrMystemLex.put(i, 0);
            countCorrLingvoGr.put(i, 0.);
            countCorrMystemGr.put(i, 0.);
        }
        FileReader fr = new FileReader("/Users/ivan/Desktop/sentencesformystem.txt");
        Scanner scan = new Scanner(fr);
        String word;
        int q =0;

        while (scan.hasNextLine()) {
            str = scan.nextLine();
            spl = str.split("   ");
            //System.out.println(str);
            word = spl[0];
            //System.out.println(spl.length);
            MorphoToken[] correct = new MorphoToken[spl.length-1];
            for(int i = 0;i<spl.length-1;i++){
                correct[i] = new MorphoToken();
            }

            for (int i = 1;i<spl.length;i++){
               //System.out.println(i);
                Set<String> gramems = new HashSet<String>();
                correct[i-1].setLexema(spl[i].split("  ")[0]);
                String[] tmp = spl[i].split("  ")[1].replace(",","").replace("[","").replace("]","").split(" ");
                gramems.clear();
                for(int k = 0;k<tmp.length;k++){
                    //System.out.println(tmp[k]);
                    gramems.add(tmp[k]);
                }
                for (String j : attr.getParts()) {
                    //System.out.println(i);
                    //if(stm.contains("прич")) System.out.println("kk");
                    if (gramems.contains(j)) {
                        correct[i-1].setPart(j);
                        //System.out.println("d " + i);
                    }
                }
                //correct[i-1].setGramems(gramems);
                //System.out.println();
                if (correct[i-1].getPart() == null) {
                    correct[i-1].setPart("Other");
                }
                //System.out.println(word + " " + correct[i-1].getLexema() + " " + correct[i-1].getPart() + " " + gramems);
                //correct[i-1].setGramems(gramems);
                countLex++;
                countAll.put(correct[i-1].getPart(), countAll.get(correct[i-1].getPart()).intValue() + 1);
                q = i+1;
                correct[i-1].setGramems(gramems);
                if(q == spl.length){
                    correct[i-1].setGramems(gramems);
                    for(int w = 0;w<correct.length;w++) {
                        //System.out.println(word + " " + correct[w].getLexema() + " " + correct[w].getPart() + " " + correct[w].getGramems());
                    }
                    //System.out.println(word);
                    MystemCorrectPercentnew(word, correct);
                }
            }
        }
        /*
        String source = "/Users/ivan/Downloads/annot.opcorpora.no_ambig_strict.xml";
        try {
            XMLStreamReader xmlr = XMLInputFactory.newInstance().createXMLStreamReader(source, new FileInputStream(source));
            while (xmlr.hasNext()) {
                xmlr.next();// - мой
                if (xmlr.getEventType() == 1) {
                    if (xmlr.getName().toString().equals("source")) {
                        str = xmlr.getElementText();
                        sen = false;
                    }
                }
                if(xmlr.isStartElement() && xmlr.getName().toString() == "tfr"){
                    if (!sen){
                        tri = 0;
                        sen = true;
                    }
                    String word = xmlr.getAttributeValue(1).replace('ё', 'е');
                    xmlr.next();
                    xmlr.next();
                    MorphoToken correct = new MorphoToken();
                    correct.setLexema(xmlr.getAttributeValue(1).toLowerCase().replace('ё', 'е'));
                    xmlr.next();
                    Set<String> gramems = new HashSet<String>();
                    while(xmlr.isStartElement() && xmlr.getName().toString() == "g"){
                        Set<String> stm = attr.sourceToMystem(xmlr.getAttributeValue(0));
                        if(stm != null){
                            gramems.addAll(stm);
                        }
                        xmlr.next(); xmlr.next();
                    }



                    if(gramems.isEmpty()){
                        continue;
                    }

                    for(String i : attr.getParts()){
                        //System.out.println(i);
                        //if(stm.contains("прич")) System.out.println("kk");
                        if(gramems.contains(i)){
                            correct.setPart(i);
                            //System.out.println("d " + i);
                        }
                    }
                    //System.out.println();
                    if(correct.getPart() == null){
                        correct.setPart("Other");
                    }

                    countLex++;
                    countAll.put(correct.getPart(), countAll.get(correct.getPart()).intValue() + 1);
                    correct.setGramems(gramems);
                    //System.out.print(word + " ");
                    //LingvoCorrectPercent(word, correct);
                    MystemCorrectPercent(word, correct);
                }
                //xmlr.next(); - ант
            }
        } catch (FileNotFoundException | XMLStreamException | FactoryConfigurationError e) {
            e.printStackTrace();
        }
        */

        System.out.println("Лемматизация");
        System.out.println("\tLingvo\tMystem");
        for(String i : attr.getParts()){
            System.out.println(i + "\t" + new BigDecimal(countCorrLingvoLex.get(i) * 100.0 / countAll.get(i)).setScale(2, RoundingMode.HALF_UP).doubleValue() + "%\t" + new BigDecimal(countCorrMystemLex.get(i) * 100.0 / countAll.get(i)).setScale(2, RoundingMode.HALF_UP).doubleValue() + "%");
        }
        System.out.println();
        System.out.println("\tLingvo\tMystem");
        for(String i : attr.getParts()){
            System.out.println(i + "\t" + countCorrLingvoLex.get(i)  + "\t" + countCorrMystemLex.get(i));
        }

        System.out.println();
        System.out.println("Разбор");
        System.out.println("\tLingvo\tMystem");
        for(String i : attr.getParts()){
            System.out.println(i + "\t" + new BigDecimal(countCorrLingvoGr.get(i) * 100.0 / countAll.get(i)).setScale(2, RoundingMode.HALF_UP).doubleValue() + "%\t" + new BigDecimal(countCorrMystemGr.get(i) * 100.0 / countAll.get(i)).setScale(2, RoundingMode.HALF_UP).doubleValue() + "%");
        }
        System.out.println();
        System.out.println("\tLingvo\tMystem");
        for(String i : attr.getParts()){
            System.out.println(i + "\t" +  new BigDecimal(countCorrLingvoGr.get(i)).setScale(2, RoundingMode.HALF_UP).doubleValue()  + "\t" + new BigDecimal(countCorrMystemGr.get(i)).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }

        System.out.println("Total words: " + countLex);
    }

}