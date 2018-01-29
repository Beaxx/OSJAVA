package Gruppe7.Exporter;

import Gruppe7.Logic.*;
import Gruppe7.Importer.Datei;
import java.lang.String;

/**
 * @author Fabian und Nicole
 */
public class ExportRaumplanung extends Datei {
    public ExportRaumplanung(String in_name, Planer in_spielPlanObj) {

        super(in_name);
        Datei exportRaumplan = new Datei(in_name);

        exportRaumplan.openOutFile_FS();
        Vorstellung[][][][] spielplan = in_spielPlanObj.GetSpielplan();

        String exportStringRaumplan = "Raumplanung" + "\r\r\n\n";

        // WOCHEN
        for (int iWoche = 0; iWoche < 3; iWoche++) {

            // TAGE
            for (int iTag = 0; iTag < 7; iTag++) {

                String wochenTag;
                switch (iTag) {
                    case 0:
                        wochenTag = "Montag";
                        break;
                    case 1:
                        wochenTag = "Dienstag";
                        break;
                    case 2:
                        wochenTag = "Mittwoch";
                        break;
                    case 3:
                        wochenTag = "Donnerstag";
                        break;
                    case 4:
                        wochenTag = "Freitag";
                        break;
                    case 5:
                        wochenTag = "Samstag";
                        break;
                    case 6:
                        wochenTag = "Sonntag";
                        break;
                    default:
                        wochenTag = "-----";
                        break;
                }

                exportStringRaumplan += "Woche " + (iWoche + 1) + " | " + wochenTag + "\r\r\n\n";

                // SÄLE
                for (int iSaal = 0; iSaal < in_spielPlanObj.GetAnzahlSaele(); iSaal++) {

                    exportStringRaumplan += "Saal Nummer: " + (iSaal + 1) + "\r\r\n\n";

                    // VORSTELLUNG
                    for (int iSpielzeit = 0; iSpielzeit < 4; iSpielzeit++) {

                        exportStringRaumplan += spielplan[iWoche][iTag][iSaal][iSpielzeit].GetSpielzeiten().ToString() +
                                ": " + spielplan[iWoche][iTag][iSaal][iSpielzeit].GetKinofilm().GetTitel() + "\r\r\n\n";
                    }
                    exportStringRaumplan += "\r\r\n\n";
                }
            }
        }
        System.out.println(exportStringRaumplan);
        exportRaumplan.writeLine_FS(exportStringRaumplan);
        exportRaumplan.closeOutFile_FS();
        exportRaumplan.eof();
    }
}
