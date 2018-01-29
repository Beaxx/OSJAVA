package Gruppe7.Exporter;

import Gruppe7.Importer.Datei;
import Gruppe7.Logic.Planer;
import Gruppe7.Logic.Vorstellung;

/**
 * @author  Nicole und Fabian
 */
public class ExportFinanzplan extends Datei {

    Planer spielplanObj;

    public ExportFinanzplan(String in_Name, Planer in_SpielplanObj) {
        super(in_Name);
        Datei exportFinanzplan = new Datei(in_Name);
        exportFinanzplan.openOutFile_FS();

        Vorstellung[][][][] spielplan = in_SpielplanObj.GetSpielplan();
        spielplanObj = in_SpielplanObj;

        // Exportüberschrift
        String headerString =
                        "Kinofilm\t" +
                        "Vorführwoche\t" +
                        "Wochentag\t" +
                        "Kinosaal\t" +
                        "Spielzeit\t" +
                        "Eintrittspreis (EUR)\t" +
                        "Erwartete Zuschauer Parkett\t" +
                        "Erwartete Zuschauer Loge\t" +
                        "Erwartete Ticketeinnahmen Vorstellung\t" +
                        "Erwartete Werbeeinnahmen Vorstellung\t" +
                        "Erwartete Ausgaben für Film NICHT für Vorstellung\t";

        for (int i = 0; i < 10; i++) {
            headerString += "Werbefilm" + (i+1) + "-Name\t" +
                            "Werbefilm" + (i+1) + "-Einnahmen\t";
        }
        headerString += "Gesamtausgaben\t" +
                        "Gesamteinnahmen Werbung:\t" +
                        "Gesamteinnahmen Tickets:\t" +
                        "Gewinn:\t";

        exportFinanzplan.writeLine_FS(headerString);

        for (int iWoche = 0; iWoche < 3; iWoche++) {
            for (int iTag = 0; iTag < 7; iTag++) {
                for (int iSaal = 0; iSaal < in_SpielplanObj.GetAnzahlSaele(); iSaal++) {
                    for (int iSpielzeit = 0; iSpielzeit < 4; iSpielzeit++) {

                        Vorstellung aktuelleVorstellung = spielplan[iWoche][iTag][iSaal][iSpielzeit];

                        String kWoche = String.valueOf(iWoche + 1);
                        String kFilm = aktuelleVorstellung.GetKinofilm().GetTitel();
                        String kPreis = String.valueOf(aktuelleVorstellung.GetEintrittspreis());
                        String kSpielzeit = aktuelleVorstellung.GetSpielzeiten().ToString();
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

                        String kZuschauerParkett = String.valueOf(aktuelleVorstellung.GetZuschauerParkett());
                        String kZuschauerZuschauerLoge = String.valueOf(aktuelleVorstellung.GetZuschauerLoge());
                        String kEinnahmenTickets = String.valueOf(
                                aktuelleVorstellung.GetVorstellungTicketeinnahmen()[0] +
                                        aktuelleVorstellung.GetVorstellungTicketeinnahmen()[1]);
                        String kEinnahmenWerbung = String.valueOf(aktuelleVorstellung.GetVorstellungWerbeeinnahmen());
                        String kAusgabenFilmGesamt = String.valueOf(aktuelleVorstellung.GetKinofilm().GetGesamtkostenInSpielplan());

                        String exportString = kFilm + "\t" +
                                kWoche + "\t" +
                                kTag + "\t" +
                                kSaal + "\t" +
                                kSpielzeit + "\t" +
                                kPreis + "\t" +
                                kZuschauerParkett + "\t" +
                                kZuschauerZuschauerLoge + "\t" +
                                kEinnahmenTickets + "\t" +
                                kEinnahmenWerbung + "\t" +
                                kAusgabenFilmGesamt + "\t";

                        for (int i = 0; i < 10; i++) {
                            try {
                                exportString += aktuelleVorstellung.GetWerbefilme().get(i).GetTitel() + "\t";
                                exportString += aktuelleVorstellung.GetWerbefilme().get(i).GetUmsatzProZuschauer() *
                                        (aktuelleVorstellung.GetZuschauerLoge() +
                                                aktuelleVorstellung.GetZuschauerParkett()) + "\t";
                            } catch (IndexOutOfBoundsException ex) {
                                exportString += "--"  + "\t" + "--"  + "\t";
                            }
                        }

                        // In erster Zeile Gesamtdaten anhängen
                        if (iSpielzeit == 0 && iSaal == 0 && iTag == 0 && iWoche == 0) {
                            exportString +=  String.valueOf(in_SpielplanObj.GetSpielplanAusgaben()) + "\t" +
                                    String.valueOf(in_SpielplanObj.GetSpielplanWerbeEinnahmen()) + "\t" +
                                    String.valueOf(in_SpielplanObj.GetSpielplanTicketeinnahmen()) + "\t" +
                                    String.valueOf(in_SpielplanObj.GetSpielplanGewinn());
                        }
                        exportFinanzplan.writeLine_FS(exportString);
                    }
                }
            }
        }
        exportFinanzplan.closeOutFile_FS();
        exportFinanzplan.eof();
    }
}