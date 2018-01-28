package Gruppe7.Exporter;

import Gruppe7.Data.Vorstellung;
import Gruppe7.Logic.Planer;
import Gruppe7.Importer.Datei;
import java.lang.String;


public class ExportRaumplanung extends Datei{
    Vorstellung[][][][] spielPlanObj;
    String exportStringRaumplan;


    public ExportRaumplanung(Vorstellung[][][][] in_spielPlanObj, String in_name) {

            super(in_name);
            Datei exportRaumplan = new Datei(in_name);

        exportRaumplan.openOutFile_FS();

        spielPlanObj = in_spielPlanObj;


        exportStringRaumplan = "";

        exportStringRaumplan = "Raumplanung";
        exportRaumplan.writeLine_FS(exportStringRaumplan);
        exportStringRaumplan = "\r";
        exportRaumplan.writeLine_FS(exportStringRaumplan);
        exportStringRaumplan = "\r";
        exportRaumplan.writeLine_FS(exportStringRaumplan);

        for (int iWoche = 0; iWoche <= spielPlanObj.length - 1; iWoche++) {
            System.out.println("Woche: " + (iWoche + 1));
            exportStringRaumplan = "\r";

            exportStringRaumplan = "Woche: " + (iWoche + 1);

            exportRaumplan.writeLine_FS(exportStringRaumplan);

            exportStringRaumplan = "\r";
            exportRaumplan.writeLine_FS(exportStringRaumplan);
            exportStringRaumplan = "\r";
            exportRaumplan.writeLine_FS(exportStringRaumplan);

            for (int iTag = 0; iTag <= spielPlanObj[iWoche].length - 1; iTag++) {

                String wochenTag = null;
                switch (iTag + 1) {
                    case 1:
                        wochenTag = "Montag";
                        break;
                    case 2:
                        wochenTag = "Dienstag";
                        break;
                    case 3:
                        wochenTag = "Mittwoch";
                        break;
                    case 4:
                        wochenTag = "Donnerstag";
                        break;
                    case 5:
                        wochenTag = "Freitag";
                        break;
                    case 6:
                        wochenTag = "Samstag";
                        break;
                    case 7:
                        wochenTag = "Sonntag";
                        break;

                    default:
                        wochenTag = "Invalid Day";
                        break;
                }
                System.out.println("Tag: " + (iTag + 1) + " " + wochenTag);

                exportStringRaumplan += "Tag: " + (iTag + 1) + " " + wochenTag;

               exportRaumplan.writeLine_FS(exportStringRaumplan);
                exportStringRaumplan = "\r";
                exportRaumplan.writeLine_FS(exportStringRaumplan);
                exportStringRaumplan = "\r";

                exportRaumplan.writeLine_FS(exportStringRaumplan);

                for (int iSaal = 0; iSaal <= spielPlanObj[iWoche][iTag].length - 1; iSaal++) {
                    System.out.println("Saal Nummer: " + spielPlanObj[iWoche][iTag][iSaal][0].GetSaal().GetSaalNummer());

                    exportStringRaumplan += "Saal Nummer: " + spielPlanObj[iWoche][iTag][iSaal][0].GetSaal().GetSaalNummer();
                    exportRaumplan.writeLine_FS(exportStringRaumplan);
                    exportStringRaumplan = "\r";
                    exportRaumplan.writeLine_FS(exportStringRaumplan);


                    for (int iSpielzeit = 0; iSpielzeit <= spielPlanObj[iWoche][iTag][iSaal].length - 1; iSpielzeit++) {
                        System.out.println(spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetSpielzeiten().toString() + ": " + spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetKinofilm().GetTitel());

                        exportStringRaumplan += spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetSpielzeiten().toString() + ": " + spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetKinofilm().GetTitel();
                        exportRaumplan.writeLine_FS(exportStringRaumplan);
                        exportStringRaumplan = "\r";
                        exportRaumplan.writeLine_FS(exportStringRaumplan);




                    }

                    exportStringRaumplan = "\r";
                    exportRaumplan.writeLine_FS(exportStringRaumplan);



                }




            }


            System.out.println(exportStringRaumplan);




            }

            exportRaumplan.closeOutFile_FS();
            exportRaumplan.eof();

        }
    }


