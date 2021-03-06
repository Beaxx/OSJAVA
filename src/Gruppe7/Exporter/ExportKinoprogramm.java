package Gruppe7.Exporter;

import Gruppe7.Importer.Datei;
import Gruppe7.Logic.Planer;
import Gruppe7.Logic.Vorstellung;

/**
 * @author  Nicole Distler
 * Erbt von Datei.
 *
 * Der Kinoprogramm Exporter exportiert das Kinoprogramm im CSV-Format.
 */
public class ExportKinoprogramm extends Datei {

    /**
     * Konstruktor
     *
     * @param in_Name         Dateinpfad
     * @param in_SpielplanObj Planer-Objekt, das den Spielplan enthält.
     */
    public ExportKinoprogramm(String in_Name, Planer in_SpielplanObj) {
        super(in_Name);
        Datei exportKinoprogramm = new Datei(in_Name);
        exportKinoprogramm.openOutFile_FS();

        Vorstellung[][][][] spielplan = in_SpielplanObj.GetSpielplan();

        // CSV-Header Zeile
        String ueberschriftenString =   "Kinofilm\t" +
                                        "Vorführwoche\t" +
                                        "Wochentag\t" +
                                        "Kinosaal\t" +
                                        "Spielzeit\t" +
                                        "Eintrittspreis (EUR)*";

        exportKinoprogramm.writeLine_FS(ueberschriftenString);

        for (int iWoche = 0; iWoche < 3; iWoche++) {
            for (int iTag = 0; iTag < 7; iTag++) {
                for (int iSaal = 0; iSaal < in_SpielplanObj.GetAnzahlSaele(); iSaal++) {
                    for (int iSpielzeit = 0; iSpielzeit < 4; iSpielzeit++) {

                        Vorstellung aktuelleVorstellung = spielplan[iWoche][iTag][iSaal][iSpielzeit];

                        String kFilm = aktuelleVorstellung.GetKinofilm().GetTitel();
                        String kWoche = String.valueOf(iWoche + 1);
                        String kPreis = String.valueOf(aktuelleVorstellung.GetEintrittspreis());
                        String kSpielzeit = aktuelleVorstellung.GetSpielzeiten().ToString();
                        String kSaal = "Saal " + (iSaal + 1);
                        String kTag = Wochentage.values()[iTag].ToString();

                        String exportString = kFilm + "\t" +
                                kWoche + "\t" +
                                kTag + "\t" +
                                kSaal + "\t" +
                                kSpielzeit + "\t" +
                                kPreis;

                        exportKinoprogramm.writeLine_FS(exportString);
                    }
                }
            }
        }
        String fusszeileString = "\r*Logenaufschlag 2,00 EUR.";
        exportKinoprogramm.writeLine_FS(fusszeileString);
        exportKinoprogramm.closeOutFile_FS();
        exportKinoprogramm.eof();
    }
}