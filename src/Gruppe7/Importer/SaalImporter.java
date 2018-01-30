package Gruppe7.Importer;

import Gruppe7.Data.Saal;
import Gruppe7.Data.SaalVerwaltung;

/**
 * @author Fabian Ueberle
 *
 * Der Saalimporter liest zeilenweise Saalinformationen aus der "saele.csv"-Datei aus dem Datensatz.
 */
public class SaalImporter extends Datei {

    /**
     * Konstruktor fuer Objekte der Klasse Datei
     * Legt einen String mit dem Namen der zu bearbeitenden Datei an.
     *
     * @param in_Name (String): Dateiname der benutzt werden soll.
     */
    public SaalImporter(String in_Name) {
        super(in_Name);

        Datei importFileSaele = new Datei(in_Name);
        importFileSaele.openInFile_FS();

        while (!importFileSaele.eof()) {
            String importString = importFileSaele.readLine_FS();
            if (importString != null) {

                String array[] = importString.split(";");

                int importSaalNr = Integer.valueOf(array[0]);
                int importPlaetzeParkett = Integer.valueOf(array[1]);
                int importPlaetzeLoge = Integer.valueOf(array[2]);
                boolean importThreeD = Boolean.valueOf(array[3]);

                SaalVerwaltung.SetSaele(new Saal(importPlaetzeLoge, importPlaetzeParkett, importThreeD, importSaalNr));
            }
        }
        SaalVerwaltung.SaalplanSortieren();
    }
}
