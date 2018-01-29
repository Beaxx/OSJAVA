package Gruppe7.Exporter;

import Gruppe7.Importer.Datei;
import Gruppe7.Logic.Planer;
import Gruppe7.Logic.Vorstellung;

/**
 * @author  Nicole und Fabian
 */
public class ExportFinanzplan extends Datei {
    private Vorstellung[][][][] spielPlanObj;
    private Planer planerObj;
    private String kFilm, kWoche, kTag, kSaal, kSpielzeit, kPreis;


    int iWoche = 0, iTag = 0, iSaal = 0, iSpielzeit = 0;


    public ExportFinanzplan(Vorstellung[][][][] in_SpielPlanObj, String in_name, Planer in_Planer) {
        super(in_name);
        Datei exportFinanzplan = new Datei(in_name);
        exportFinanzplan.openOutFile_FS();

        spielPlanObj = in_SpielPlanObj;
        planerObj = in_Planer;

        for (int iWoche = 0; iWoche <= spielPlanObj.length - 1; iWoche++) {
            for (int iTag = 0; iTag <= spielPlanObj[iWoche].length - 1; iTag++) {
                for (int iSaal = 0; iSaal <= spielPlanObj[iWoche][iTag].length - 1; iSaal++) {
                    for (int iSpielzeit = 0; iSpielzeit <= spielPlanObj[iWoche][iTag][iSaal].length - 1; iSpielzeit++) {

                        String idStringVorstellung = iWoche + "-" + iTag + "-" + iSaal + "-" + iSpielzeit;

                        spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetKinofilm().SetIdVorstellung(idStringVorstellung);
                    }
                }
            }
        }

        String ueberschriftenString = "Kinofilm\tVorführwoche\tWochentag\tKinosaal\tSpielzeit\tEintrittspreis (EUR)*\tErwartete Einnahmen aus Ticketverkaeufen\tWerbespots\tErwartete Einnahmen aus Werbespots\tAusgaben fuer Filmmiete";
//        System.out.println(ueberschriftenString);
        exportFinanzplan.writeLine_FS(ueberschriftenString);

        for (int iFilme = 0; iFilme <= planerObj.alleFilmeFinancials.size() - 1; iFilme++) {
            //System.out.println(planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung());
            //TODO: Array zerlegen und schleife für jede Vorstellung(Eintrag)
            // System.out.println("Anzahlvorstellungen: "+planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().size());//Anzahl Iterationen

            kFilm = planerObj.alleFilmeFinancials.get(iFilme).GetTitel();

            for (int iVorstellungen = 0; iVorstellungen <= planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().size() - 1; iVorstellungen++) {

                //System.out.println(planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().get(iVorstellungen));

                String idVorstellungString = planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().get(iVorstellungen);

                if (idVorstellungString != null) {

                    String array[] = idVorstellungString.split("-");

                    iWoche = Integer.valueOf(array[0]);
                    iTag = Integer.valueOf(array[1]);
                    iSaal = Integer.valueOf(array[2]);
                    iSpielzeit = Integer.valueOf(array[3]);

                    spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetEintrittspreis();

                    kSpielzeit = spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetSpielzeiten().toString();


                    kPreis = "" + spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetEintrittspreis();

                    // int ticketeinnahmen = (((spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetZuschauerParkett()*spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetEintrittspreis()))+(spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetZuschauerLoge()*(spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetEintrittspreis()+2)));

                    int Parkett = spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetZuschauerParkett();
                    int Loge = spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetZuschauerLoge();
                    int Preis = spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetEintrittspreis();

                    int EinnahmenParkett = Parkett * Preis;
                    int EinnahmenLoge = Loge * (Preis + 2);

                    int ticketeinnahmen = EinnahmenParkett + EinnahmenLoge;

//                    System.out.println(ticketeinnahmen);


                    for (int iWerbung = 0; iWerbung <= spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetWerbefilme().size() - 1; iWerbung++) {

                        String Werbespot = spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetWerbefilme().get(iWerbung).getWerbespotTitel();


                        int Werbeeinahmen = spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetWerbefilme().get(iWerbung).getEinnahmenProWerbeSpot();

//                        System.out.println(Werbespot + ": " + Werbeeinahmen);
                    }

                    kWoche = "Woche " + (iWoche + 1);

                    String kWochentag;


                    if (iTag == 0) {
                        kWochentag = "Montag";
                    } else if (iTag == 1) {
                        kWochentag = "Dienstag";
                    } else if (iTag == 2) {
                        kWochentag = "Mittwoch";
                    } else if (iTag == 3) {
                        kWochentag = "Donnerstag";
                    } else if (iTag == 4) {
                        kWochentag = "Freitag";
                    } else if (iTag == 5) {
                        kWochentag = "Samstag";
                    } else if (iTag == 6) {
                        kWochentag = "Sonntag";
                    } else {
                        kWochentag = "kein Wochentag";
                    }

                    kTag = kWochentag;

                    kSaal = "Saal " + (iSaal + 1);

                    String exportString = "";

                    exportString = kFilm + "\t" + kWoche + "\t" + kTag + "\t" + kSaal + "\t" + kSpielzeit + "\t" + kPreis;

//                    System.out.println(exportString);


                    exportFinanzplan.writeLine_FS(exportString);

                } else {
                    break;
                }
            }
        }

        String fusszeileString = "\r*Logenaufschlag 2,00 EUR.";
//        System.out.println(fusszeileString);
        exportFinanzplan.writeLine_FS(fusszeileString);

        exportFinanzplan.closeOutFile_FS();
    }

}
