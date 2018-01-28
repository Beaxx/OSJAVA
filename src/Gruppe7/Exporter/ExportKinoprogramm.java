package Gruppe7.Exporter;

import Gruppe7.Data.*;
import Gruppe7.Importer.Datei;
import Gruppe7.Importer.Importer;
import Gruppe7.Logic.Planer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExportKinoprogramm extends Datei  {

    //@TODO Attribute hier:
    // Vorstellung[][][][] spielPlanObj;    Raumplanung
    //String exportStringRaumplan;          Raumplanung
    private Vorstellung[][][][] spielPlanObj;
    private Planer planerObj;

    int iWoche = 0, iTag = 0, iSaal = 0, iSpielzeit = 0;


    public ExportKinoprogramm(Vorstellung[][][][] in_spielPlanObj, String in_name, Planer in_Planer) {
        super(in_name);


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

        System.out.println(planerObj.alleFilmeFinancials.size()); //Anzahl der Iterationen
        for(int iFilme = 0; iFilme <= planerObj.alleFilmeFinancials.size()-1; iFilme++){
            //System.out.println(planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung());
            //TODO: Array zerlegen und schleife für jede Vorstellung(Eintrag)
            System.out.println(planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().size());//Anzahl Iterationen

            System.out.println(planerObj.alleFilmeFinancials.get(iFilme).GetTitel());

            for(int iVorstellungen = 0; iVorstellungen<= planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().size() -1; iVorstellungen++){

                //System.out.println(planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().get(iVorstellungen));

                String idVorstellungString = planerObj.alleFilmeFinancials.get(iFilme).GetIdVorstellung().get(iVorstellungen);

                if (idVorstellungString != null) {

                    String array[] = idVorstellungString.split("-");

                    iWoche = Integer.valueOf(array[0]);
                    iTag = Integer.valueOf(array[1]);
                    iSaal = Integer.valueOf(array[2]);
                    iSpielzeit = Integer.valueOf(array[3]);

                    //System.out.println("Woche: "+ (iWoche+1));

                    System.out.println(spielPlanObj[iWoche][iTag][iSaal][iSpielzeit]);

                }else {
                    break;
                }




            }


        }


        //System.out.println(planerObj.alleFilmeFinancials.get(0).GetIdVorstellung());

        planerObj.getSpielplan()[0][0][0][0].GetKinofilm().GetIdVorstellung();




       /* aldiSupplier.put( "Carbo, spanischer Sekt", "Freixenet" );
        aldiSupplier.put( "ibu Stapelchips", "Bahlsen Chipsletten" );
        aldiSupplier.put( "Ko-kra Katzenfutter", "felix Katzenfutter" );
        aldiSupplier.put( "Küchenpapier", "Zewa" );
        aldiSupplier.put( "Nuss-Nougat-Creme", "Zentis" );
        aldiSupplier.put( "Pommes Frites", "McCaine" );
       */

          //  System.out.println(spielPlanObj[0][0][0][3].GetKinofilm().hashCode());


            Datei exportKinoprogramm = new Datei(in_name);
            exportKinoprogramm.openOutFile_FS();
            exportKinoprogramm.writeLine_FS("Bacon");


            exportKinoprogramm.closeOutFile_FS();

        }
    }
