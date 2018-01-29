package Gruppe7.Importer;


import Gruppe7.Data.Saal;
import Gruppe7.Data.SaalVerwaltung;

public class SaalImporter extends Datei {

    /**
     * Konstruktor fuer Objekte der Klasse Datei
     * Legt einen String mit dem Namen der zu bearbeitenden Datei an.
     *
     * @param in_name (String): Dateiname der benutzt werden soll.
     */
    public SaalImporter(String in_name) {
        super(in_name);

        Datei importFileSaele = new Datei(in_name);
        importFileSaele.openInFile_FS();

        while (!importFileSaele.eof()){
            String importString = importFileSaele.readLine_FS();
            if (importString != null){

                String array[] = importString.split(";");

                int importSaalNr = Integer.valueOf(array[0]);
                int importPlaetzeParkett = Integer.valueOf(array[1]);
                int importPlaetzeLoge = Integer.valueOf(array[2]);
                boolean importThreeD = Boolean.valueOf(array[3]);

                SaalVerwaltung.setSaele(new Saal(importPlaetzeLoge, importPlaetzeParkett, importThreeD, importSaalNr));
            }
        }
        SaalVerwaltung.saalplanSortieren();
    }
}
