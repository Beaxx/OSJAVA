package Gruppe7.Data;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Lennart Völler
 *
 * Die Saalverwaltung und all ihre Felder sind statisch, damit von allen Stellen des Programs einfach auf sie
 * zugegriffen werden kann. Weiterhin ergibt sich der Vorteil, dass für die Verwendung nicht bei jedem Durchlauf des
 * Programs neue Objekte angelegt werden müssen. Die Objekte liegen nur ein mal im Speicher und werden von den Klassen
 * die sie verwenden auf unterscchiedliche weise referenziert.
 *
 * Für die Preisformel wir die Anzahl der Plätze im größten und zweitgrößten Saal benötigt. Diese Information wird
 * statisch bereitgestellt, sodass aus dem Programmablauf nur Lesezugriffe benötigt.
 */
public class SaalVerwaltung {
    static private ArrayList<Saal> saele = new ArrayList<>();
    private static int plaetzeGroesterSaal;
    private static int plaetzeZweitgroesterSaal;

    /**
     * Speichert größen und zweitgrößten Saal ab.
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

    // Getter

    /**
     * Getmethode für alle Säle
     *
     * @return Eine Liste aller Säle
     */
    public static ArrayList<Saal> getSaele() {
        return saele;
    }

    /**
     * Getmethode für die größe der Saalliste.
     *
     * @return Die zahl der Säle in der Liste aller Säle.
     */
    public static int getSize() {
        return saele.size();
    }

    /**
     * Getmethode für die Anzahl der Plätze im größten Saal
     *
     * @return Die Anzahl der Plätze im größten Saal
     */
    public static int getPlaetzeGroesterSaal() {
        return plaetzeGroesterSaal;
    }

    /**
     * Getmethode für die Anzahl der Plätze im zweitgrößten Saal
     *
     * @return Die Anzahl der Plätze im zweitgrößten Saal
     */
    public static int getPlaetzeZweitgroesterSaal() {
        return plaetzeZweitgroesterSaal;
    }

    //Setter

    /**
     * Setmethode für die Saalliste
     *
     * @param in_saal Eine Liste aller Säle (wird vom SaalImporter bereitgestellt)
     */
    public static void setSaele(Saal in_saal) {
        saele.add(in_saal);
    }

    /**
     * Sortiert die Liste aller Säle nach ihrer Fähigkeit 3D-Filme abzuspielen.
     * Säle, die 3D-Filme abspielen könnnen stehen am Anfang der Liste.
     */
    public static void saalplanSortieren() {
        Collections.sort(saele, (s1, s2) -> {
            if (s1.getThreeD()) {
                return -1;
            } else {
                return 1;
            }
        });
    }
}