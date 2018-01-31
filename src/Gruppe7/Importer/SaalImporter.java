package Gruppe7.Importer;

import Gruppe7.Data.Saal;
import Gruppe7.Data.SaalVerwaltung;
import java.lang.*;
import java.lang.String;

import static java.lang.Integer.parseInt;

/**
 * @author Fabian Ueberle
 *
 * Der Saalimporter ließt zeilenweise Saalinformationen aus der "saele.csv"-Datei aus dem Datensatz.
 */
public class SaalImporter extends Datei {

    String importString;

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
            importString = importFileSaele.readLine_FS();

            if (importString != null) {

                flowControl(importString);


                    String array[] = importString.split(";", 4);

                    int importSaalNr = Integer.valueOf(array[0]);
                    int importPlaetzeParkett = Integer.valueOf(array[1]);
                    int importPlaetzeLoge = Integer.valueOf(array[2]);
                    boolean importThreeD = Boolean.valueOf(array[3]);

                    SaalVerwaltung.SetSaele(new Saal(importPlaetzeLoge, importPlaetzeParkett, importThreeD, importSaalNr));

            }
        }
        SaalVerwaltung.SaalplanSortieren();
    }

    private boolean flowControl (String in_importstring){

        String testImportStrigng = in_importstring;

        String array[] = testImportStrigng.split(";");

        if (array.length!=4){
            System.err.println("Fehlerhafte Importdatei für Kinosäle. Das Programm wird abgebrochen. " +
                            "Bitte prüfen Sie Ihre saal.csv Datei auf vier Spalten.");
            System.exit(-1);
            return false;
        }


        return true;


    }

}