package Gruppe7.Data;

import java.util.ArrayList;

// Auf die Verwaltungsklassen muss aus dem gesamten Kode zugegriffen werden
public class WerbefilmVerwaltung
{
    static private ArrayList<Werbefilm> werbefilme = new ArrayList<>();
    static private ArrayList<Double> werbefilmProfitabilitaet = new ArrayList<>();



    //Getter
    public static ArrayList<Werbefilm> getWerbefilme() { return werbefilme; }
    public static int getSize() { return werbefilme.size(); }


    //Setter
    public static void setWerbefilm(Werbefilm in_werbefilm) {werbefilme.add(in_werbefilm);}
    public static void setWerbefilmProfitabilitaet(Werbefilm in_werbefilm){
        werbefilmProfitabilitaet.add((double)in_werbefilm.getUmsatzProZuschauer() / (double)in_werbefilm.getLaufzeit());
    }
}