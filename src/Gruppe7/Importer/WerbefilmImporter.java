package Gruppe7.Importer;

import Gruppe7.Data.*;

/**
 * @author Fabian Ueberle
 * Erbt von Datei
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

                dataValidation(importString, in_Name);

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
     * @param in_name         Name und Pfade der Importdatei
     * @author Fabian Ueberle
     * <p>
     * Die Methode dataValidation() prüft jede Zeile der Importdatei ob diese die erwartete Struktur aufweist.
     * Dies soll zum einen einen Absturz des Programms sowie die Erzeugung unvollständiger Objekte vermeiden.
     * Die ausgegebene Fehlermeldung soll den Anwender auf die betroffene Datei hinweisen.
     * </p>
     */
    private void dataValidation(String in_importstring, String in_name) {

        String array[] = in_importstring.split(";");

        if (array.length != 3) {
            System.err.println("Fehlerhafte Importdatei für Werbespots. Das Programm wird abgebrochen. " +
                    "Bitte prüfen Sie Ihre Datei " + in_name + " auf 3 Spalten.");
            System.exit(-1);
        }

    }
}
