package Gruppe7.Importer;

import Gruppe7.Data.Saal;
import Gruppe7.Data.SaalVerwaltung;

/**
 * @author Fabian Ueberle
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