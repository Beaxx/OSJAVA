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

                flowControl(importString);

                String arrayWerbung[] = importString.split(";");

                String importWerbespotBezeichnung = String.valueOf(arrayWerbung[0]);
                int importWerbespotEinnahmenen = Integer.valueOf(arrayWerbung[1]);
                int importWerbespotLaufzeit = Integer.valueOf(arrayWerbung[2]);

                WerbefilmVerwaltung.SetWerbefilm(new Werbefilm(importWerbespotBezeichnung, importWerbespotLaufzeit, importWerbespotEinnahmenen));
            }
        }
        WerbefilmVerwaltung.WerbeplanSortieren();
    }

    /**
     * @param in_importstring der einzulesende String für die späteren Objektinstanzen
     * @author Fabian Ueberle
     * <p>
     *     Die Methode flowControl() prüft jede Zeile der Importdatei ob diese die erwartete Struktur aufweist.
     *     Dies soll zum einen einen Absturz des Programms sowie die Erzeugung unvollständiger Objekte vermeiden.
     *     Die ausgegebene Fehlermeldung soll den Anwender auf die betroffene Datei hinweisen.
     * </p>
     * */
    private boolean flowControl (String in_importstring){

        String testImportStrigng = in_importstring;

        String array[] = testImportStrigng.split(";");

        if (array.length!=11){
            System.err.println("Fehlerhafte Importdatei für Kinofilme. Das Programm wird abgebrochen. " +
                    "Bitte prüfen Sie Ihre filme.csv Datei auf 11 Spalten.");
            System.exit(-1);
            return false;
        }
        return true;
    }
}
