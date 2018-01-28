package Gruppe7.Exporter;

import Gruppe7.Data.Vorstellung;
import Gruppe7.Importer.Datei;

import Gruppe7.Data.Vorstellung;
import Gruppe7.Importer.Datei;
import java.lang.String;


public class ExportKinoprogramm extends Datei  {

    //@TODO Attribute hier:
    // Vorstellung[][][][] spielPlanObj;    Raumplanung
    //String exportStringRaumplan;          Raumplanung



    public ExportKinoprogramm(Vorstellung[][][][] in_spielPlanObj, String in_name) {

        super(in_name);
        Datei exportKinoprogramm = new Datei(in_name);

        exportKinoprogramm.openOutFile_FS();
        exportKinoprogramm.writeLine_FS("Bacon");
        exportKinoprogramm.closeOutFile_FS();

    }
    }