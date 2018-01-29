package Gruppe7.Data;

import java.util.*;

/**
 * @author Lennart Völler
 *
 * Die FilmVerwaltung und all ihre Felder sind statisch, damit von allen Stellen des Programs einfach auf sie
 * zugegriffen werden kann. Weiterhin ergibt sich der Vorteil, dass für die Verwendung nicht bei jedem Durchlauf des
 * Programs neue Objekte angelegt werden müssen. Die Objekte liegen nur ein mal im Speicher und werden von den Klassen
 * die sie verwenden auf unterscchiedliche weise referenziert.
 *
 * Es gibt für Kinofilme drei Kriterien, die entscheiden, ob sie zu einer bestimmten Zeit in einem bestimmten
 * Saal gezeigt werden können: die 3D-Technik, die FSK-Einstufung des Films und die Spieldauer des Films.
 * Die Menge der möglichen Sets an Filmen ergibt sich somit durch die Menge der Merkmalsausprägungen:
 *
 * 3D: 0 und 1: 2 Ausprägungen
 * FSK: 0, 12, 16, 18: 4 Ausprägungen
 * Laufzeit: <=150 oder <=180: 2 Ausprägungen
 *
 * Die Menge der möglichen Kombinationen aus allen Merkmalen ist somit beschränkt auf 2 * 4 * 2 = 16 Kombinationen.
 *
 * Diese Menge wird reduziert durch Kombinationen, die sich logisch ausschließen. Beispielsweise ist es nicht nötig
 * das Set film3D_150_2000 zu erstellen, da zur Uhrzeit 20:00 Filme immer eine Länge von 180 Minuten haben können.
 * Das Merkmal Laufzeit ist an das Merkmal Uhrzeit (FSK) gekoppelt. Es verbleiben so 6 kombinierte Listen, die alle
 * Kombinationen an Merkmalsausprägungen enthalten, die bestimmen ob ein Film an einer Stelle im Spielplan gezeigt
 * werden kann.
 *
 * Alternativ zu diesem Vorgehen ist die erstellung von Set's während der Erstellung der Spielpläne. Hier
 * müssen die einzelnen Set's jedoch bei jedem Durchlauf neu erstellt werden. Dies ist angesichts der begrenzten Zahl an
 * möglichen Set nicht sinnvoll. Die Importdauer mit dem hier angewandten Verfahren beträgt (wenn alle Filme importiert
 * werden und kein Wert für die Mindestbeliebtheit gesetzt wird) im Mittel 250 Millisekunden. Dadurch wird eine
 * Verbesserung der Performance um den Faktor 6-10 erreicht. (Performance misst sich an den erstellten
 * gültigen Spielplänen pro Sekunde.)
 */
public class FilmVerwaltung {

    // Importierte Listen
    static private ArrayList<Kinofilm> filmeFuer3DSaele = new ArrayList<>();
    static private ArrayList<Kinofilm> filmeFuer2DSaele = new ArrayList<>();

    static private ArrayList<Kinofilm> filmeFuer1500Uhr_1730Uhr = new ArrayList<>();
    static private ArrayList<Kinofilm> filmeFuer2000Uhr = new ArrayList<>();
    static private ArrayList<Kinofilm> filmeFuer2300Uhr = new ArrayList<>();

    static private ArrayList<Kinofilm> filmeFuer150minSlotlaenge = new ArrayList<>();
    static private ArrayList<Kinofilm> filmeFuer180minSlotlaenge = new ArrayList<>();

    // Kombinierte Listen
    static private ArrayList<Kinofilm> filme3D_150_1500_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_150_2300 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_180_2000 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme2D_150_1500_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_150_2300 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_180_2000 = new ArrayList<>();

    /**
     * Hilfsmethode, die die Iteration der setFilmArrays() übernimmt. Wird aus Main() aufgerufen.
     * Da die Filme für die 15:00 und 17:30 Vorstellung das Selbe FSK-Siegel tragen und
     * dementsprechend die gleiche Charakteristiken aufweisen, wird die Abfrage des 15:00 Uhr Slots
     * übersprungen um redundanten Schreibaufwand zu sparen.
     */
    public static void FilmArraysHelper() {

        for (int saal3Dfaehig = 0; saal3Dfaehig < 2; saal3Dfaehig++) {
            for (Spielzeiten uhrzeit : Spielzeiten.values()) {
                if (!(uhrzeit == Spielzeiten.SLOT_1500)) {
                    setFilmArrays(saal3Dfaehig, uhrzeit);
                }
            }
        }
    }

    /**
     * Befüllung der 6 vordefinierten Sets aus allen Filmen.
     * (s. Kommentare FilmVerwaltung Konstruktor)
     * <p>
     * Die Metode bekommt ihre Parameter von FilmArraysHelper(), die die Iteration steuert.
     *
     * @param in_saal3Dfaehig 1 oder 0 | 1: Vorstellung kann 3D-Filme zeigen 2: Vorstellung kann nur 2D-Filme zeigen
     * @param in_uhrzeit      Elemnt der Enumeration Spielzeiten: 15:00, 17:30, 20:00 oder 23:00
     */
    private static void setFilmArrays(int in_saal3Dfaehig, Spielzeiten in_uhrzeit) {

        switch (in_saal3Dfaehig) {

            //region  3D-Faehiger Saal
            case 1: {
                switch (in_uhrzeit) {
                    case SLOT_1500:
                    case SLOT_1730: {
                        filme3D_150_1500_1730 = filmeFuer3DSaele;
                        filme3D_150_1500_1730.retainAll(filmeFuer1500Uhr_1730Uhr);
                        filme3D_150_1500_1730.retainAll(filmeFuer150minSlotlaenge);
                        break;
                    }

                    case SLOT_2000: {
                        filme3D_180_2000 = filmeFuer3DSaele;
                        filme3D_180_2000.retainAll(filmeFuer2000Uhr);
                        filme3D_180_2000.retainAll(filmeFuer180minSlotlaenge);
                        break;
                    }

                    case SLOT_2300: {
                        filme3D_150_2300 = filmeFuer3DSaele;
                        filme3D_150_2300.retainAll(filmeFuer2300Uhr);
                        filme3D_150_2300.retainAll(filmeFuer150minSlotlaenge);
                        break;
                    }
                }
            }
            break;
            //endregion

            //region 2D-Faehiger Saal
            case 0: {
                switch (in_uhrzeit) {
                    case SLOT_1500:
                    case SLOT_1730: {
                        filme2D_150_1500_1730 = filmeFuer2DSaele;
                        filme2D_150_1500_1730.retainAll(filmeFuer1500Uhr_1730Uhr);
                        filme2D_150_1500_1730.retainAll(filmeFuer150minSlotlaenge);
                        break;
                    }

                    case SLOT_2000: {
                        filme2D_180_2000 = filmeFuer2DSaele;
                        filme2D_180_2000.retainAll(filmeFuer2000Uhr);
                        filme2D_180_2000.retainAll(filmeFuer180minSlotlaenge);
                        break;
                    }
                    case SLOT_2300: {
                        filme2D_150_2300 = filmeFuer2DSaele;
                        filme2D_150_2300.retainAll(filmeFuer2300Uhr);
                        filme2D_150_2300.retainAll(filmeFuer150minSlotlaenge);
                        break;
                    }
                }
            }
            break;
            //endregion
        }
    }

    // Setter (fügen Kinofilme zu Arraylisten hinzu)
    public static void SetFilmeFuer3DSaele(Kinofilm in_film) {
        filmeFuer3DSaele.add(in_film);
    }

    public static void SetFilmeFuer2DSaele(Kinofilm in_film) {
        filmeFuer2DSaele.add(in_film);
    }

    public static void SetFilmeFuer1500Uhr_1730Uhr(Kinofilm in_film) {
        filmeFuer1500Uhr_1730Uhr.add(in_film);
    }

    public static void SetFilmeFuer2000Uhr(Kinofilm in_film) {
        filmeFuer2000Uhr.add(in_film);
    }

    public static void SetFilmeFuer2300Uhr(Kinofilm in_film) {
        filmeFuer2300Uhr.add(in_film);
    }

    public static void SetFilmeFuer150minSlotlaenge(Kinofilm in_film) {
        filmeFuer150minSlotlaenge.add(in_film);
    }

    public static void SetFilmeFuer180minSlotlaenge(Kinofilm in_film) {
        filmeFuer180minSlotlaenge.add(in_film);
    }

    /**
     * Der Getter funktioniert ähnlich dem Setter, anhand von 3D-Fähigkeit des Saals und dem Timeslot wird
     * das passsende Filmset ausgewählt und zurückgegeben.
     *
     * @param in_saal3Dfaehig 1 oder 0 | 1: Vorstellung kann 3D-Filme zeigen 2: Vorstellung kann nur 2D-Filme zeigen
     * @param in_uhrzeit      Elemnt der Enumeration Spielzeiten: 15:00, 17:30, 20:00 oder 23:00
     * @return eine Liste an Kinofilmen, die in dieser Vorstellung gezeigt werden dürfen.
     */
    public static ArrayList<Kinofilm> GetFilme(boolean in_saal3Dfaehig, Spielzeiten in_uhrzeit) {

        // Übersetzung von Boolean in Int für Switch
        int switch3D;
        if (in_saal3Dfaehig) {
            switch3D = 1;
        } else {
            switch3D = 0;
        }

        switch (switch3D) {

            //region  3D-Faehiger Saal
            case 1: {
                switch (in_uhrzeit) {
                    case SLOT_1500:
                    case SLOT_1730: {
                        return filme3D_150_1500_1730;
                    }

                    case SLOT_2000: {
                        return filme3D_180_2000;
                    }

                    case SLOT_2300: {
                        return filme3D_150_2300;
                    }
                }
            }
            //endregion

            //region 2D-Faehiger Saal
            case 0: {
                switch (in_uhrzeit) {
                    case SLOT_1500:
                    case SLOT_1730: {
                        return filme2D_150_1500_1730;
                    }

                    case SLOT_2000: {
                        return filme2D_180_2000;
                    }

                    case SLOT_2300: {
                        return filme2D_150_2300;
                    }
                }
            }
            default:
                return null;
            //endregion
        }
    }

    /**
     * Um die Gesamtkosten, die ein Kinofilm hervorruft erfassen zu können brauchen Kinofilme ein entsprechendes
     * Feld zur Speicherung dieser Information. Da Kinofilme jedoch statisch sind, können diese Ojekteigenschaften
     * nicht automatisch bereinigt werden sondern müssen manuell beim erstellen eines neuen Spielplans überschrieben
     * werden.
     *
     * Die Methode iteriert durch alle Kinofilme und setzt ihre Gesamtkosten auf 0.
     */
    public static void CleanUpGesamtkosten() {
        for (Kinofilm film : filmeFuer3DSaele) {
            film.SetGesamtkostenInSpielplan(0);
        }

        for (Kinofilm film : filmeFuer2DSaele) {
            film.SetGesamtkostenInSpielplan(0);
        }

        for (Kinofilm film : filmeFuer1500Uhr_1730Uhr) {
            film.SetGesamtkostenInSpielplan(0);
        }

        for (Kinofilm film : filmeFuer2000Uhr) {
            film.SetGesamtkostenInSpielplan(0);
        }

        for (Kinofilm film : filmeFuer2300Uhr) {
            film.SetGesamtkostenInSpielplan(0);
        }
        for (Kinofilm film : filmeFuer150minSlotlaenge) {
            film.SetGesamtkostenInSpielplan(0);
        }
        for (Kinofilm film : filmeFuer180minSlotlaenge) {
            film.SetGesamtkostenInSpielplan(0);
        }
    }
}
