package Gruppe7.Data;

import java.util.ArrayList;
import java.util.Collections;

// Auf die Verwaltungsklassen muss aus dem gesamten Kode zugegriffen werden
public class SaalVerwaltung
{
    static private ArrayList<Saal> saele = new ArrayList<>();
    static private int anzahl3D = 0;
    static private int anzahl2D = 0;

    // Getter
    public static ArrayList<Saal> getSaele() { return saele; }
    public static int getSize(){return saele.size();}
    public static int getAnzahl2D() {
        return anzahl2D;
    }
    public static int getAnzahl3D() {
        return anzahl3D;
    }

    //Setter
    public static void setSaele(Saal in_saal) {saele.add(in_saal);}
    public static void setAnzahl2D(int in_anzahl2D) {
        anzahl2D = in_anzahl2D;
    }
    public static void setAnzahl3D(int in_anzahl3D) {
        anzahl3D = in_anzahl3D;
    }

    /**
     * Sortiert die Saele nach ihrer 3D-Fäigkeit
     */
    public static void saalplanSortieren() {
        Collections.sort(saele, (s1, s2) -> {
            if (s1.getThreeD()) {
                return -1;
            }
            else {return 1;}
        });
    }
}