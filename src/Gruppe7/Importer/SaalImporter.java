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

                String array[] = importString.split(";");

                importSaalNr = Integer.valueOf(array[0]);
                importPlaetzeParkett = Integer.valueOf(array[1]);
                importPlaetzeLoge = Integer.valueOf(array[2]);
                importThreeD = Boolean.valueOf(array[3]);

                SaalVerwaltung.setSaele(new Saal(importPlaetzeLoge, importPlaetzeParkett, importThreeD, importSaalNr));
            }
        }
        SaalVerwaltung.saalplanSortieren();

        int local2D = 0;
        int local3D = 0;
        for (Saal saal: SaalVerwaltung.getSaele()) {
            if (saal.getThreeD()) {local3D++;}
            else {local2D++;}
        }
        SaalVerwaltung.setAnzahl3D(local3D);
        SaalVerwaltung.setAnzahl2D(local2D);
    }
}
