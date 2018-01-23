package Gruppe7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import Gruppe7.Data.*;
import Gruppe7.Logic.*;
import Gruppe7.Importer.*;
import sun.security.provider.ConfigFile;

public class Main {

    public static void main(String[] args) throws IOException {

        //Datenimport
        new SaalImporter("C:/import/saele.csv");
        new WerbefilmImporter("C:/import/werbespots.csv");
        new KinofilmImporter("C:/import/filme.csv");

        while (true){
            Planer planer = new Planer();
//            System.out.println(planer.getSpielplaneinnahmen());
//            System.out.println(planer.getSpielplanAusgaben());
        }
    }
}
