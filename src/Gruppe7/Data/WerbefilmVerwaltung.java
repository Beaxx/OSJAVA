package Gruppe7.Data;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Die WerbefilmVerwaltung und all ihre Felder sind statisch, damit von allen Stellen des Programs einfach auf sie
 * zugegriffen werden kann. Weiterhin ergibt sich der Vorteil, dass für die Verwendung nicht bei jedem Durchlauf des
 * Programs neue Objekte angelegt werden müssen. Die Objekte liegen nur ein mal im Speicher und werden von den Klassen
 * die sie verwenden auf unterscchiedliche weise referenziert.
 *
 * Die Qualität eines Werbefilms misst sich am Quotienten aus seinem Umsatz pro Zuschauer und seiner Laufzeit.
 * Bei der Erstellung der Gesamtliste aller Werbefilme werden diese daher absteigend nach diesem Quotienten sortiert.
 * Der so entstehende Stack mit den besten Elementen zu oberst wird genutzt um den besten Werbeplan zu erstellen der für
 * die Maximalwerbedauer von 20 Minuten möglich ist.
 *
 * Dadurch, dass der Werbeblock statisch zum Programmstart erstellt wird sind bei Erstellung der Werbepläne für die
 * einzelnen Vorstellungen nur noch Lesezugriffe auf diesen Standard-Werbeblock notwendig, was die Effizienz steigert.
 */
public class WerbefilmVerwaltung {
    static private ArrayList<Werbefilm> werbefilme = new ArrayList<>();
    static private ArrayList<Werbefilm> werbefilme20MinutenStandard = new ArrayList<>();
    static private int werbefilme20MinutenStandardDauer = 0;
    static private int werbefilme20MinutenStandardUmsatzProZuschauer = 0;

    //Getter

    /**
     * Getmethode für die Liste aller Werbefilme
     *
     * @return Liste mit allen (nach Qualität sortierten) Werbefilmen.
     */
    static ArrayList<Werbefilm> getWerbefilme() {
        return werbefilme;
    }

    /**
     * Getmethode für den bestmöglichen 20-Minuten Werbeblock
     *
     * @return Liste mit Werbefilmen, die die Lauzeit von 20 Minuten nicht überschreiten.
     */
    public static ArrayList<Werbefilm> getWerbefilme20MinutenStandard() {
        return werbefilme20MinutenStandard;
    }

    /**
     * Getmethode für die Laufzeit des Standardwerbeblocks
     *
     * @return die Laufzeit des Standardwerbeblocks
     */
    public static int getWerbefilme20MinutenStandardDauer() {
        return werbefilme20MinutenStandardDauer;
    }

    //Setter

    /**
     * Setmethode für die Werbefilm Liste
     *
     * @param in_werbefilm eine Liste aus Werbefilmen (geliefert durch Werbefilmimporter)
     */
    public static void setWerbefilm(Werbefilm in_werbefilm) {
        werbefilme.add(in_werbefilm);
    }

    /**
     * Sortiert die Werbespots nach ihrer Profitabilität.
     * Profitabilität = UmsatzProZuschauer/Laufzeit.
     */
    public static void werbeplanSortieren() {
        Collections.sort(werbefilme, (w1, w2) -> {
            if (((double) w1.getUmsatzProZuschauer() / (double) w1.GetLaufzeit()) <
                    (double) w2.getUmsatzProZuschauer() / (double) w2.GetLaufzeit()) {
                return 1;
            }
            if (((double) w1.getUmsatzProZuschauer() / (double) w1.GetLaufzeit()) >
                    (double) w2.getUmsatzProZuschauer() / (double) w2.GetLaufzeit()) {
                return -1;
            }
            return 0;
        });
    }

    /**
     * Die besten Werbefilme, die in einem 20Minuten Werbeblock passen werden separat abgespeichert.
     * So muss der Werbeblock nur in Fällen, in denen Filme im 150min Spielblock über 130min lang sind und
     * in denen Filme im 180min Block über 160 min lang sind dynamisch erstellt werden. In alen anderen Fällen
     * kann der profitablste 20min-Block gechaltet werden.
     */
    public static void standardWerbeblock() {
        int werbedauerSoll = 20;
        int werbedauerIst = 0;

        for (Werbefilm werbung : werbefilme) {
            if ((werbedauerIst <= werbedauerSoll) && ((werbedauerIst + werbung.GetLaufzeit()) <= werbedauerSoll)) {
                werbefilme20MinutenStandard.add(werbung);
                werbedauerIst += werbung.GetLaufzeit();
            }
        }
        werbefilme20MinutenStandardDauer = werbedauerIst;
    }

    public static void standardWerbeblockUmsatzProZuschauer() {
        for (Werbefilm werbung : werbefilme20MinutenStandard) {
            werbefilme20MinutenStandardUmsatzProZuschauer += werbung.getUmsatzProZuschauer();
        }
    }

    public static int GetWerbefilme20MinutenStandardUmsatzProZuschauer() {
        return werbefilme20MinutenStandardUmsatzProZuschauer;
    }

}