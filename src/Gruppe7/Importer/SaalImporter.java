package Gruppe7.Importer;


import Gruppe7.Data.Saal;
import Gruppe7.Data.SaalVerwaltung;

public class SaalImporter extends Datei {

    private Datei importFileSaele;
    private String importString;
    private int importSaalNr;
    private int importPlaetzeParkett;
    private int importPlaetzeLoge;
    private boolean importThreeD;

    /**
     * Konstruktor fuer Objekte der Klasse Datei
     * Legt einen String mit dem Namen der zu bearbeitenden Datei an.
     *
     * @param in_name (String): Dateiname der benutzt werden soll.
     */
    public SaalImporter(String in_name) {
        super(in_name);

        importFileSaele = new Datei(in_name);
        importFileSaele.openInFile_FS();

        while (!importFileSaele.eof()){
            importString = importFileSaele.readLine_FS();
            if (importString != null){
//                System.out.println("Import String:"  +importString+ "\naus Importdatei " +in_name );

                String array[] = importString.split(";");

                importSaalNr = Integer.valueOf(array[0]);
                importPlaetzeParkett = Integer.valueOf(array[1]);
                importPlaetzeLoge = Integer.valueOf(array[2]);
                importThreeD = Boolean.valueOf(array[3]);

                SaalVerwaltung.setSaele(new Saal(importPlaetzeLoge, importPlaetzeParkett, importThreeD, importSaalNr));

            }
        }
        SaalVerwaltung.saalplanSortieren();
    }
}
