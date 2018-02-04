package Gruppe7.Exporter;

import Gruppe7.Logic.*;
import Gruppe7.Importer.Datei;
import java.lang.String;

/**
 * @author  Nicole Distler
 * Erbt von Datei.
 *
 * Der Raumplan Exporter erstellt eine Datei ähnlich dem Kinoprogramm, das Aufschluss darüber gibt, wann in welchem
 * Saal zu welcher Uhrzeit welcher Film läuft. Da dieser Plan das Kinopersonal unterstützen soll, ist er im .txt-Format
 * gespeichert, sodass er schnell ausgedruckt und ausgehängt werden kann.
 */
public class ExportRaumplanung extends Datei {

    /**
     * Konstruktor
     * <p>
     * Als new-Line Characters werden sowohl carriage returns als auch newline returns verwendet, sodass sowohl eine
     * Ausgabe in der Konsole wie auch in der Dateiausgabe das richtige Format hat.
     *
     * @param in_Name         Dateinpfad
     * @param in_SpielplanObj Planer-Objekt, das den Spielplan enthält.
     */
    public ExportRaumplanung(String in_Name, Planer in_SpielplanObj) {

        super(in_Name);
        Datei exportRaumplan = new Datei(in_Name);

        exportRaumplan.openOutFile_FS();
        Vorstellung[][][][] spielplan = in_SpielplanObj.GetSpielplan();

        String exportStringRaumplan = "Raumplanung" + "\r\r\n\n";

        // WOCHEN
        for (int iWoche = 0; iWoche < 3; iWoche++) {

            // TAGE
            for (int iTag = 0; iTag < 7; iTag++) {

                String wochenTag = Wochentage.values()[iTag].ToString();
                exportStringRaumplan += "Woche " + (iWoche + 1) + " | " + wochenTag + "\r\r\n\n";

                // SÄLE
                for (int iSaal = 0; iSaal < in_SpielplanObj.GetAnzahlSaele(); iSaal++) {

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
        exportRaumplan.writeLine_FS(exportStringRaumplan);
        exportRaumplan.closeOutFile_FS();
        exportRaumplan.eof();
    }
}