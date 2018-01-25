package Gruppe7.Data;

import java.util.ArrayList;
import java.util.Collections;

// Auf die Verwaltungsklassen muss aus dem gesamten Kode zugegriffen werden
public class WerbefilmVerwaltung
{
    static private ArrayList<Werbefilm> werbefilme = new ArrayList<>();
    static private ArrayList<Werbefilm> werbefilme20MinutenStandard = new ArrayList<>();

    //Getter
    static ArrayList<Werbefilm> getWerbefilme() { return werbefilme; }
    static ArrayList<Werbefilm> getWerbefilme20MinutenStandard() {return werbefilme20MinutenStandard; }

    //Setter
    public static void setWerbefilm(Werbefilm in_werbefilm) {werbefilme.add(in_werbefilm);}

    /**
     * Sortiert die Werbespots nach ihrer Profitabilität.
     * Profitabilität = UmsatzProZuschauer/Laufzeit
     */
    public static void werbeplanSortieren() {
        Collections.sort(werbefilme, (w1, w2) -> {
            if (((double)w1.getUmsatzProZuschauer() / (double)w1.getLaufzeit()) <
                    (double)w2.getUmsatzProZuschauer() / (double)w2.getLaufzeit()) {
                return 1;
            }
            if (((double)w1.getUmsatzProZuschauer() / (double)w1.getLaufzeit()) >
                    (double)w2.getUmsatzProZuschauer() / (double)w2.getLaufzeit()) {
                return -1;
            }
            return 0;
        });
    }

    /**
     * Die besten spots, die in einem 20Minuten Werbeblock passen werden separat abgespeichert.
     */
    public static void  standardWerbeblock(){
        int werbedauerSoll = 20;
        int werbedauerIst = 0;

        for(Werbefilm werbung: werbefilme){
            if ((werbedauerIst <= werbedauerSoll) && ((werbedauerIst + werbung.getLaufzeit()) <= werbedauerSoll)){
                werbefilme20MinutenStandard.add(werbung);
                werbedauerIst += werbung.getLaufzeit();
            }
        }
    }
}