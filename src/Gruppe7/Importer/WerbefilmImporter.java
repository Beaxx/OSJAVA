package Gruppe7.Importer;

import Gruppe7.Data.*;

/**
 * @author Fabian Ueberle
 *
 * Der WerbefilmImporter liest zeilenweise Werbefilminformationen aus der "werbespots.csv"-Datei aus dem Datensatz.
 */
public class WerbefilmImporter extends Datei {

    /**
     * Erstellt aus den serialisierten Werbefilm-Objekten in der Import-Datei Werbefilm-Objekte.
     *
     * @param in_Name Name der Importdatei
     */
    public WerbefilmImporter(String in_Name) {
        super(in_Name);

        Datei importFileWerbespots = new Datei(in_Name);
        importFileWerbespots.openInFile_FS();

        while (!importFileWerbespots.eof()) {
            String importString = importFileWerbespots.readLine_FS();
            if (importString != null) {

                String arrayWerbung[] = importString.split(";");

                String importWerbespotBezeichnung = String.valueOf(arrayWerbung[0]);
                int importWerbespotEinnahmenen = Integer.valueOf(arrayWerbung[1]);
                int importWerbespotLaufzeit = Integer.valueOf(arrayWerbung[2]);

                WerbefilmVerwaltung.SetWerbefilm(new Werbefilm(importWerbespotBezeichnung, importWerbespotLaufzeit, importWerbespotEinnahmenen));
            }
        }
        WerbefilmVerwaltung.WerbeplanSortieren();
    }
}
