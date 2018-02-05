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


        while (true) {

            String importString = importFileWerbespots.readLine_FS();

            if (importString == null) {
                break;
            }


            if (!dataValidation(importString, in_Name)) {
                System.err.println("Fehler in Spaltenstruktur.");
                while (!dataValidation(importString, in_Name)) {
                    importString = importFileWerbespots.readLine_FS();
                    if (importString == null) {
                        break;
                    }
                    dataValidation(importString, in_Name);
                }
            }

            //Aufteilung des Importstrings ins Array.
            if (importString == null) {
                break;
            }
            String arrayWerbung[] = importString.split(";", 3);

            if (!checkForInt(arrayWerbung[1]) || !checkForInt(arrayWerbung[2])) {
                System.err.println("Fehler in der Datei " + in_Name + ". Fehlerhafte Datensätze wurden übersprungen.");
                while (!checkForInt(arrayWerbung[1]) || !checkForInt(arrayWerbung[2])) {
                    importString = importFileWerbespots.readLine_FS();
                    arrayWerbung = importString.split(";", 3);
                }

            }

            String importWerbespotBezeichnung = String.valueOf(arrayWerbung[0]);
            int importWerbespotEinnahmenen = Integer.valueOf(arrayWerbung[1]);
            int importWerbespotLaufzeit = Integer.valueOf(arrayWerbung[2]);

            WerbefilmVerwaltung.SetWerbefilm(new Werbefilm(importWerbespotBezeichnung, importWerbespotLaufzeit, importWerbespotEinnahmenen));
        }

        WerbefilmVerwaltung.WerbeplanSortieren();
    }



        /**
         * @param  in_importstring der einzulesende String für die späteren Objektinstanzen
         * @param  in_name Name und Pfade der Importdatei
         * @author Fabian Ueberle
         * <p>
         *     Die Methode dataValidation() prüft jede Zeile der Importdatei ob diese die erwartete Struktur aufweist.
         *     Dies soll zum einen einen Absturz des Programms sowie die Erzeugung unvollständiger Objekte vermeiden.
         *     Die ausgegebene Fehlermeldung soll den Anwender auf die betroffene Datei hinweisen.
         * </p>
         * */
        private boolean dataValidation (String in_importstring, String in_name){

            String array[] = in_importstring.split(";");

            if (array.length != 3) {
                System.err.println("Fehlerhafte Importdatei für Kinosäle. " +
                        "Bitte prüfen Sie Ihre Datei " + in_name + " auf drei Spalten.");
                return false;

            }
            return true;
        }


        /**
         * @param  in_InputCheck der zu prüfende String
         * @author Fabian Ueberle
         * <p>
         *     Die Methode checkForInt prüft ob der aktuelle String einen Integer Wert ist.
         * </p>
         * */

        private boolean checkForInt (String in_InputCheck){
            try {
                Integer.valueOf(in_InputCheck);
                return true;
            } catch (NumberFormatException e) {
                System.err.println("Kein Integer Wert");
                return false;
            }
        }
}
