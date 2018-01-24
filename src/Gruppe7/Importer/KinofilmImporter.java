package Gruppe7.Importer;

import Gruppe7.Data.*;
import java.util.ArrayList;

public class KinofilmImporter extends Datei {

    private Datei importFileKinofilme;
    private String importString;
    private String importKinofilmTitel;
    private String importKinofilmRegisseur;

    private int importKinofilmFskInt;
    private Fsk importKinofilmFSK;

    private String importKinofilmGemresString;
    private ArrayList<Genre> importKinofilmGenres;

    private int importKinofilmMietpreis;
    private int importKinofilmBeliebtheit;
    private int importKinofilmLaufzeit;
    private String importKinofilmSprache;
    private String importKinofilmErscheinungsland;
    private int importKinofilmErscheinungsjahr;
    private boolean importThreeD;

    private int minBeliebtheit;


    /**
     * Konstruktor fuer Objekte der Klasse Datei
     * Legt einen String mit dem Namen der zu bearbeitenden Datei an.
     *
     * @param in_name (String): Dateiname der benutzt werden soll.
     */
    public KinofilmImporter(String in_name) {
        super(in_name);

        importFileKinofilme = new Datei(in_name);
        importFileKinofilme.openInFile_FS();

        //Schleife läuft bis zum Ende der Datei.
        while (true) {

            //Jede Zeile wird in importString eingelesen, es sei denn, in der letzten Zeile steht nichts drin.
            importString = importFileKinofilme.readLine_FS();

            if (importString == null) {break;}

            String[] arrayKinofilm;
//                System.out.println("Import String: " + importString + "\naus Importdatei " + in_name);

                //Zerlegt den Import String (Zeile der Datei) und erstellt ein Array.
                arrayKinofilm = importString.split(";");

                //Die einzelnen Array Positionen werden nachfolgend Variablen der Klasse zugewiesen.

                //Filmtitel
                importKinofilmTitel = String.valueOf(arrayKinofilm[0]);
                //Regieseuer
                importKinofilmRegisseur = String.valueOf(arrayKinofilm[1]);

                //FSK des aktuellen Films
                //Hier wird der int Wert der FSK ausgewertet und dafür eine Enumeration gesetzt.
                importKinofilmFskInt = Integer.valueOf(arrayKinofilm[2]);
                if (importKinofilmFskInt == 18) {
                    importKinofilmFSK = Fsk.FSK_18;
                }
                if (importKinofilmFskInt == 16) {
                    importKinofilmFSK = Fsk.FSK_16;
                }
                if (importKinofilmFskInt == 12) {
                    importKinofilmFSK = Fsk.FSK_12;
                }
                if (importKinofilmFskInt == 6) {
                    importKinofilmFSK = Fsk.FSK_6;
                }
                if (importKinofilmFskInt == 0) {
                    importKinofilmFSK = Fsk.FSK_0;
                }
                //Test ob FSK Zusweisung erfolgreich war.
//                System.out.println("FSK: " + importKinofilmFSK);


                //Genres Auslesen und Zuweisen
                //Für jeden Durchgang muss eine neue Liste erstellt werden.
                //Es wird eine ArrayList benötigt, da ein Film mehrere Genres haben kann.
                importKinofilmGenres = new ArrayList<>();

                //Erstellung eines String mit allen Genres
                importKinofilmGemresString = String.valueOf(arrayKinofilm[3]);

                //Ausgabe des Strings
//                System.out.println("String Genres:" + importKinofilmGemresString);
//                System.out.println("--------------------------");

                //Der String wird aufgeteilt
                String arrayGenre[] = importKinofilmGemresString.split(",");
                for (int i = 0; i < arrayGenre.length; i++) {
                    //Leerzeichen entfernen
                    arrayGenre[i] = arrayGenre[i].trim();
                }

                //Die ausgelesenen Genres werden gepfrüft und eindeutigen Enums zugewiesen.
                for (String inputGenre: arrayGenre)
                {
                    if (inputGenre.trim().equals("Action")) {
                        importKinofilmGenres.add(Genre.ACTION);
                    }
                    if (inputGenre.trim().equals("Dokumentation")) {
                        importKinofilmGenres.add(Genre.DOKUMENTATION);
                    }
                    if (inputGenre.trim().equals("Drama")) {
                        importKinofilmGenres.add(Genre.ACTION);
                    }
                    if (inputGenre.trim().equals("Horror")) {
                        importKinofilmGenres.add(Genre.HORROR);
                    }
                    if (inputGenre.trim().equals("Komödie")) {
                        importKinofilmGenres.add(Genre.KOMOEDIE);
                    }
                    if (inputGenre.trim().equals("Krimi")) {
                        importKinofilmGenres.add(Genre.KRIMI);
                    }
                    if (inputGenre.trim().equals("Science Fiction")) {
                        importKinofilmGenres.add(Genre.SCIENCE_FICTION);
                    }
                    if (inputGenre.trim().equals("Zeichentrick")) {
                        importKinofilmGenres.add(Genre.ZEICHENTRICK);
                    }
                    if (inputGenre.trim().equals("Thriller")) {
                        importKinofilmGenres.add(Genre.THRILLER);
                }

            }
            importKinofilmMietpreis = Integer.valueOf(arrayKinofilm[4]);
                //System.out.println("Mietpreis" + importKinofilmMietpreis);
            importKinofilmBeliebtheit = Integer.valueOf(arrayKinofilm[5]);
            importKinofilmLaufzeit = Integer.valueOf(arrayKinofilm[6]);
            importKinofilmSprache = String.valueOf(arrayKinofilm[7]);
            importKinofilmErscheinungsland=String.valueOf(arrayKinofilm[8]);
            importKinofilmErscheinungsjahr=Integer.valueOf(arrayKinofilm[9]);
            importThreeD=Boolean.valueOf(arrayKinofilm[10]);


            //Constructor

            //SaalVerwaltung.setSaele(new Saal(importPlaetzeLoge, importPlaetzeParkett, importThreeD, importSaalNr));

            Kinofilm tempKinofilm = new Kinofilm (importKinofilmTitel, importKinofilmLaufzeit, importThreeD, importKinofilmSprache, importKinofilmRegisseur, importKinofilmErscheinungsjahr, importKinofilmErscheinungsland, importKinofilmBeliebtheit, importKinofilmMietpreis, importKinofilmFSK, importKinofilmGenres);

            //Beliebtheitsfilter
            if (tempKinofilm.getBeliebtheit() >= 95){
                FilmVerwaltung.setFilme(tempKinofilm);
            }

        }
    }
}








