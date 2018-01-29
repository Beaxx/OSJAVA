package Gruppe7.Exporter;

import Gruppe7.Data.*;
import Gruppe7.Importer.Datei;
import Gruppe7.Importer.Importer;
import Gruppe7.Logic.Planer;
import Gruppe7.Logic.Vorstellung;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExportKinoprogramm extends Datei  {

    //@TODO Attribute hier:
    // Vorstellung[][][][] spielPlanObj;    Raumplanung
    //String exportStringRaumplan;          Raumplanung
    private Vorstellung[][][][] spielPlanObj;
    private Planer planerObj;


    private String kFilm, kWoche, kTag, kSaal, kSpielzeit, kPreis;


    int iWoche = 0, iTag = 0, iSaal = 0, iSpielzeit = 0;


    public ExportKinoprogramm(Vorstellung[][][][] in_spielPlanObj, String in_name, Planer in_Planer) {
        super(in_name);
        Datei exportKinoprogramm = new Datei(in_name);
        exportKinoprogramm.openOutFile_FS();

        spielPlanObj = in_spielPlanObj;
        planerObj = in_Planer;


        for (int iWoche = 0; iWoche <= spielPlanObj.length - 1; iWoche++) {
            for (int iTag = 0; iTag <= spielPlanObj[iWoche].length - 1; iTag++) {
                for (int iSaal = 0; iSaal <= spielPlanObj[iWoche][iTag].length - 1; iSaal++) {
                    for (int iSpielzeit = 0; iSpielzeit <= spielPlanObj[iWoche][iTag][iSaal].length - 1; iSpielzeit++) {

                        String idStringVorstellung = iWoche + "-" + iTag + "-" + iSaal + "-" + iSpielzeit;
                        //System.out.println(idStringVorstellung);

                        spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetKinofilm().SetIdVorstellung(idStringVorstellung);
                    }
                }
            }
        }



       //spielPlanObj[0][0][0][0].GetKinofilm().GetIdVorstellung().size();
       //System.out.println(spielPlanObj[0][0][0][0].GetKinofilm().GetIdVorstellung().size());

        //System.out.println("Anzahlfilme gesamt: "+planerObj.alleFilmeFinancials.size()); //Anzahl der Iterationen


        String ueberschriftenString= "Kinofilm \t Vorführwoche\tWochentag\tKinosaal\tSpielzeit\tEintrittspreis (EUR)*";
        System.out.println(ueberschriftenString);
        exportKinoprogramm.writeLine_FS(ueberschriftenString);

        for(int iFilme = 0; iFilme <= planerObj.alleFilmeFinancials.size()-1; iFilme++){
            //System.out.println(planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung());
            //TODO: Array zerlegen und schleife für jede Vorstellung(Eintrag)
           // System.out.println("Anzahlvorstellungen: "+planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().size());//Anzahl Iterationen

            kFilm = planerObj.alleFilmeFinancials.get(iFilme).GetTitel();

            for(int iVorstellungen = 0; iVorstellungen<= planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().size() -1; iVorstellungen++){

                //System.out.println(planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().get(iVorstellungen));

                String idVorstellungString = planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().get(iVorstellungen);

                if (idVorstellungString != null) {

                    String array[] = idVorstellungString.split("-");

                    iWoche = Integer.valueOf(array[0]);
                    iTag = Integer.valueOf(array[1]);
                    iSaal = Integer.valueOf(array[2]);
                    iSpielzeit = Integer.valueOf(array[3]);

                    spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetEintrittspreis();

                    kSpielzeit=spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetSpielzeiten().toString();



                    kPreis = ""+spielPlanObj[iWoche][iTag][iSaal][iSpielzeit].GetEintrittspreis();

                    kWoche = "Woche "+(iWoche+1);

                    String kWochentag;


                        if (iTag == 0){kWochentag="Montag";
                           }else if (iTag == 1) {
                            kWochentag = "Dienstag";
                            }else if (iTag == 2) {
                            kWochentag = "Mittwoch";
                            }else if(iTag == 3) {
                            kWochentag = "Donnerstag";
                            }else if(iTag == 4) {
                            kWochentag = "Freitag";
                            }else if(iTag == 5) {
                            kWochentag = "Samstag";
                            }else if(iTag == 6) {
                            kWochentag = "Sonntag";
                            }else{ kWochentag="kein Wochentag";}

                    kTag = kWochentag;

                    kSaal = "Saal " + (iSaal+1);

                    String exportString = "";

                    exportString= kFilm+"\t"+kWoche+"\t"+kTag+"\t"+kSaal+"\t"+kSpielzeit+ "\t" +kPreis;

                    System.out.println(exportString);


                    exportKinoprogramm.writeLine_FS(exportString);



                }else {
                    break;
                }




            }



        }

        String fusszeileString= "\r*Logenaufschlag 2,00 EUR.";
        System.out.println(fusszeileString);
        exportKinoprogramm.writeLine_FS(fusszeileString);
        exportKinoprogramm.closeOutFile_FS();



        }
    }
