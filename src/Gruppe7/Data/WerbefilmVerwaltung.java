package Gruppe7.Data;

import java.util.ArrayList;
import java.util.Collections;

// Auf die Verwaltungsklassen muss aus dem gesamten Code zugegriffen werden
public class WerbefilmVerwaltung
{
    static private ArrayList<Werbefilm> werbefilme = new ArrayList<>();
    static private ArrayList<Werbefilm> werbefilme20MinutenStandard = new ArrayList<>();
    static private int werbefilme20MinutenStandardDauer = 0;

    //Getter
    static ArrayList<Werbefilm> getWerbefilme() { return werbefilme; }
    static ArrayList<Werbefilm> getWerbefilme20MinutenStandard() {return werbefilme20MinutenStandard; }
    public static int getWerbefilme20MinutenStandardDauer() {
        return werbefilme20MinutenStandardDauer;
    }

    //Setter
    public static void setWerbefilm(Werbefilm in_werbefilm) {werbefilme.add(in_werbefilm);}

    /**Debugged
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

    /**Debugged
     * Die besten spots, die in einem 20Minuten Werbeblock passen werden separat abgespeichert.
     * So muss der Werbeblock nur in Fällen, in denen Filme im 150min Spielblock über 130min lang sind und
     * in denen Filme im 180min spielblock über 160 min lang sind dynamisch erstellt werden. In alen anderen Fällen
     * kann der profitablste 20min-Block gechaltet werden.
     */
    public static void standardWerbeblock(){
        int werbedauerSoll = 20;
        int werbedauerIst = 0;

        for(Werbefilm werbung: werbefilme){
            if ((werbedauerIst <= werbedauerSoll) && ((werbedauerIst + werbung.getLaufzeit()) <= werbedauerSoll)){
                werbefilme20MinutenStandard.add(werbung);
                werbedauerIst += werbung.getLaufzeit();
            }
        }
        werbefilme20MinutenStandardDauer = werbedauerIst;
    }
}