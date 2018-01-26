package Gruppe7.Data;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Lennart Völler
 */
public class SaalVerwaltung
{
    static private ArrayList<Saal> saele = new ArrayList<>();
    private static int plaetzeGroesterSaal;
    private static int plaetzeZweitgroesterSaal;

    // Getter
    public static ArrayList<Saal> getSaele() { return saele; }
    public static int getSize(){return saele.size();}
    public static int getPlaetzeGroesterSaal() {
        return plaetzeGroesterSaal;
    }
    public static int getPlaetzeZweitgroesterSaal() {
        return plaetzeZweitgroesterSaal;
    }

    //Setter
    public static void setSaele(Saal in_saal) {saele.add(in_saal);}

    /**Sortiert Säle nach 3D
     * Debugged
     */
    public static void saalplanSortieren() {
        Collections.sort(saele, (s1, s2) -> {
            if (s1.getThreeD()) {
                return -1;
            }
            else {return 1;}
        });
    }

    /**Speichert größen und zweitgrößten Saal ab
     * Debugged
     */
    public static void plaetzteInGroestemUndZweitgroestemSaal() {
        int localPlaetzeGroesterSaal = 0;
        int localPlaetzeZweitgroesterSaal = 0;

        for (Saal saal : saele) {
            if (localPlaetzeGroesterSaal < (saal.getPlaetzeLoge() + saal.getPlaetzeParkett())) {
                localPlaetzeGroesterSaal = saal.getPlaetzeLoge() + saal.getPlaetzeParkett();
            }
        }

        for (Saal saal : saele) {
            if ((localPlaetzeZweitgroesterSaal < (saal.getPlaetzeLoge() + saal.getPlaetzeParkett())) &&
                    ((saal.getPlaetzeLoge() + saal.getPlaetzeParkett()) < localPlaetzeGroesterSaal)) {
                localPlaetzeZweitgroesterSaal = saal.getPlaetzeLoge() + saal.getPlaetzeParkett();
            }
        }

        plaetzeGroesterSaal = localPlaetzeGroesterSaal;
        plaetzeZweitgroesterSaal = localPlaetzeZweitgroesterSaal;
    }
}