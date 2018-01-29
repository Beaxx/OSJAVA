package Gruppe7.Importer;

import Gruppe7.Data.*;

public class WerbefilmImporter extends Datei {

    /**
     * Konstruktor fuer Objekte der Klasse Datei
     * Legt einen String mit dem Namen der zu bearbeitenden Datei an.
     *
     * @param in_name (String): Dateiname der benutzt werden soll.
     */
    public WerbefilmImporter(String in_name) {
        super(in_name);

        Datei importFileWerbespots = new Datei(in_name);
        importFileWerbespots.openInFile_FS();

        while (!importFileWerbespots.eof()){
            String importString = importFileWerbespots.readLine_FS();
            if (importString != null){

                String arrayWerbung[] = importString.split(";");

                String importWerbespotBezeichnung = String.valueOf(arrayWerbung[0]);
                int importWerbespotEinnahmenen = Integer.valueOf(arrayWerbung[1]);
                int importWerbespotLaufzeit = Integer.valueOf(arrayWerbung[2]);

                WerbefilmVerwaltung.setWerbefilm(new Werbefilm(importWerbespotBezeichnung, importWerbespotLaufzeit, importWerbespotEinnahmenen));
            }
        }
        WerbefilmVerwaltung.werbeplanSortieren();
    }

}
