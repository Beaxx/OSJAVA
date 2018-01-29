package Gruppe7.Data;

import java.util.ArrayList;

/**
 * @author Lennart Völler
 *
 * Die Saalverwaltung und all ihre Felder sind statisch, damit von allen Stellen des Programs einfach auf sie
 * zugegriffen werden kann. Weiterhin ergibt sich der Vorteil, dass für die Verwendung nicht bei jedem Durchlauf des
 * Programs neue Objekte angelegt werden müssen, obwohl sich ihre Inhalte nicht verändern. Die Objekte liegen nur ein
 * mal im Speicher und werden von den Klassen die sie verwenden auf unterscchiedliche weise referenziert.
 *
 * Für die Preisformel wir die Anzahl der Plätze im größten und zweitgrößten Saal benötigt. Diese Information wird
 * statisch bereitgestellt, sodass aus dem Programmablauf nur Lesezugriffe erfolgen.
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
            if (localPlaetzeGroesterSaal < (saal.GetPlaetzeLoge() + saal.GetPlaetzeParkett())) {
                localPlaetzeGroesterSaal = saal.GetPlaetzeLoge() + saal.GetPlaetzeParkett();
            }
        }

        for (Saal saal : saele) {
            if ((localPlaetzeZweitgroesterSaal < (saal.GetPlaetzeLoge() + saal.GetPlaetzeParkett())) &&
                    ((saal.GetPlaetzeLoge() + saal.GetPlaetzeParkett()) < localPlaetzeGroesterSaal)) {
                localPlaetzeZweitgroesterSaal = saal.GetPlaetzeLoge() + saal.GetPlaetzeParkett();
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
    public static ArrayList<Saal> GetSaele() {
        return saele;
    }

    /**
     * Getmethode für die Anzahl der Plätze im größten Saal
     *
     * @return Die Anzahl der Plätze im größten Saal
     */
    public static int GetPlaetzeGroesterSaal() {
        return plaetzeGroesterSaal;
    }

    /**
     * Getmethode für die Anzahl der Plätze im zweitgrößten Saal
     *
     * @return Die Anzahl der Plätze im zweitgrößten Saal
     */
    public static int GetPlaetzeZweitgroesterSaal() {
        return plaetzeZweitgroesterSaal;
    }

    //Setter

    /**
     * Setmethode für die Saalliste
     *
     * @param in_saal Eine Liste aller Säle (wird vom SaalImporter bereitgestellt)
     */
    public static void SetSaele(Saal in_saal) {
        saele.add(in_saal);
    }

    /**
     * Sortiert die Liste aller Säle nach ihrer Fähigkeit 3D-Filme abzuspielen.
     * Säle, die 3D-Filme abspielen könnnen stehen am Anfang der Liste.
     */
    public static void saalplanSortieren() {
        saele.sort((s1, s2) -> {
            if (s1.GetThreeD()) {
                return -1;
            } else {
                return 1;
            }
        });
    }
}