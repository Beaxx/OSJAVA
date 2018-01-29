package Gruppe7.Exporter;

import Gruppe7.Importer.Datei;
import Gruppe7.Logic.Planer;
import Gruppe7.Logic.Vorstellung;

// TODO: DOKU!
public class ExportKinoprogramm extends Datei {

    public ExportKinoprogramm(String in_Name, Planer in_SpielplanObj) {
        super(in_Name);
        Datei exportKinoprogramm = new Datei(in_Name);
        exportKinoprogramm.openOutFile_FS();

        Vorstellung[][][][] spielplan = in_SpielplanObj.GetSpielplan();

        // Exportüberschrift
        String ueberschriftenString = "Kinofilm\t" +
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
                        String kSpielzeit = aktuelleVorstellung.GetSpielzeiten().toString();
                        String kSaal = "Saal " + (iSaal + 1);

                        String kTag;
                        switch (iTag) {
                            case 0: {
                                kTag = "Montag";
                                break;
                            }
                            case 1: {
                                kTag = "Dienstag";
                                break;
                            }
                            case 2: {
                                kTag = "Mittwoch";
                                break;
                            }
                            case 3: {
                                kTag = "Donnerstag";
                                break;
                            }
                            case 4: {
                                kTag = "Freitag";
                                break;
                            }
                            case 5: {
                                kTag = "Samstag";
                                break;
                            }
                            case 6: {
                                kTag = "Sonntag";
                                break;
                            }
                            default: {
                                kTag = "-----";
                                break;
                            }
                        }

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