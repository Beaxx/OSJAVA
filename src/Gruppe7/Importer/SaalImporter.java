package Gruppe7.Importer;

import Gruppe7.Data.Saal;
import Gruppe7.Data.SaalVerwaltung;
import java.lang.*;
import java.lang.String;

import static java.lang.Integer.parseInt;

/**
 * @author Fabian Ueberle
 * Erbt von Datei
 *
 * Der Saalimporter ließt zeilenweise Saalinformationen aus der "saele.csv"-Datei aus dem Datensatz.
 */
public class SaalImporter extends Datei {

    /**
     * Erstellt aus den serialisierten Saalobjekten in der Import-Datei Saal-Objekte.
     *
     * @param in_Name Name der Importdatei
     */
    public SaalImporter(String in_Name) {
        super(in_Name);

        Datei importFileSaele = new Datei(in_Name);
        importFileSaele.openInFile_FS();


        while (true) {

            String importString = importFileSaele.readLine_FS();

            if (importString == null) {
               break;
            }


            if(!dataValidation(importString, in_Name)){
                System.err.println("Fehler in Spaltenstruktur.");
                while (!dataValidation(importString, in_Name)){
                    importString = importFileSaele.readLine_FS();
                    if (importString == null) {break;}
                    dataValidation(importString, in_Name);
                }
            }

                //Aufteilung des Importstrings ins Array.
            if (importString == null) {break;}
            String array[] = importString.split(";", 4);











                    int importSaalNr = Integer.valueOf(array[0]);
                    int importPlaetzeParkett = Integer.valueOf(array[1]);
                    int importPlaetzeLoge = Integer.valueOf(array[2]);
                    boolean importThreeD = Boolean.valueOf(array[3]);

                    SaalVerwaltung.SetSaele(new Saal(importPlaetzeLoge, importPlaetzeParkett, importThreeD, importSaalNr));

                }

            SaalVerwaltung.SaalplanSortieren();
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

            if (array.length != 4) {
                System.err.println("Fehlerhafte Importdatei für Kinosäle. " +
                        "Bitte prüfen Sie Ihre Datei " + in_name + " auf vier Spalten.");
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
            String input = in_InputCheck;
            Boolean isInt;
            isInt = false;
            try {
                Integer.valueOf(in_InputCheck);
                isInt = true;
                return true;
            } catch (NumberFormatException e) {
                System.err.println("Kein Integer Wert");
                isInt = false;
                return false;
            }
        }

        /**
         * @param  in_InputCheck der zu prüfende Wert
         * @author Fabian Ueberle
         * <p>
         *     Die Methode checkForBoolean prüft ob der aktuelle Wert ein Boolen ist.
         * </p>
         * */

        private boolean checkForBoolean (String in_InputCheck){
            String input = in_InputCheck;
            Boolean isBool;
            isBool = false;
            try {
                Boolean.valueOf(in_InputCheck);
                isBool = true;
                return true;
            } catch (NumberFormatException e) {
                System.err.println("Kein Boolean Wert");
                isBool = false;
                return false;
            }
        }


}


