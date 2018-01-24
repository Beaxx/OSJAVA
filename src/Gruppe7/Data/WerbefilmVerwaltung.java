package Gruppe7.Data;

import java.util.ArrayList;
import java.util.Collections;

// Auf die Verwaltungsklassen muss aus dem gesamten Kode zugegriffen werden
public class WerbefilmVerwaltung
{
    static private ArrayList<Werbefilm> werbefilme = new ArrayList<>();

    //Getter
    public static ArrayList<Werbefilm> getWerbefilme() { return werbefilme; }
    public static int getSize() { return werbefilme.size(); }

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
}