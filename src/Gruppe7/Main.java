package Gruppe7;

import java.io.IOException;

import Gruppe7.Logic.*;
import Gruppe7.Importer.*;

public class Main {


    public static void main(String[] args) throws IOException {
        //Datenimport
        new SaalImporter("C:/import/saele.csv");
        new WerbefilmImporter("C:/import/werbespots.csv");
        new KinofilmImporter("C:/import/filme.csv");

        //Performance Wrapper
        long startTime = System.currentTimeMillis();

        Planer planer = new Planer();
        int durchläufe = 10000;
            for (int i = 0; i < 10000; i++) {
                Planer tempPlaner = new Planer();

                if (tempPlaner.getSpielplanGewinn() > planer.getSpielplanGewinn()){
                    planer = tempPlaner;
                    System.out.println("Einnahmen: " + planer.getSpielplanEinnahmen() +"\n"+
                                        "Ausgaben: " + planer.getSpielplanAusgaben() +"\n"+
                                        "Gewinn:" + planer.getSpielplanGewinn() +"\n"+
                                        "--------------------------------");
                }
            }

        // Performance Measure
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000 + " Sekunden für " + durchläufe + " Durchläufe");
    }
}
