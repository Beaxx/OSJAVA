package Gruppe7.Data;

import java.util.*;

/**
 * @author Lennart Völler
 * @version 25.01.2018
 *
 * Die Worte "Set" und "ArrayList" werden im folgenden synomym verwendet, da sie sich im folgenden Zusammenhang
 * nicht unterscheiden.
 *
 * Es gibt für Kinofilme drei Kriterien, die entscheiden, ob sie zu einer bestimmten Zeit in einem bestimmten
 * Saal laufen dürfen: die 3D-Technik des Films, die FSK-Einstufung des Films und die Laufzeit des Films.
 * Die Menge der möglichen Sets an Filmen ergibt sich somit durch die Menge der Merkmalsausprägungen:
 *
 * 3D: 0 und 1 -> 2
 * FSK: 0, 12, 16, 18 -> 4
 * Laufzeit: <150 oder <180 -> 2
 *
 * Die Menge der möglichen Sets ist smot beschränkt auf 2 * 4 * 2 = 16 sets.
 *
 * Diese Menge wird reduziert durch gewisse Kombinationen die auszuschließen sind. So z. B. das set film3D_150_2000, da
 * da ein Set für Filme um 20.00 Uhr immer auch den einzigen Timeslot mit einer Spieldauer von 180 Minuten erhält.
 *
 * Alternativ zu diesem Vorgehen ist die erstellung von (im Java Sinne) Set's währen der Erstellung der Spielpläne. Hier
 * müssen die einzelnen Set's jedoch immer wieder neu erstellt werden. Dies ist angesichts der begrenzen Zahl an
 * möglichen Set nicht sinnvoll. Die Importdauer mit dem hier angewandten Verfahren beträgt 79 Millisekunden. Dadurch
 * wird eine verbesserung der Performance um den Faktor 6 erreicht. (Performance misst sich an den erstellten
 * gültigen Spielplänen pro Sekunde.)
 *
 * ###### ..> Da die maximale Dauer die ein Fil haben darf direkt vom Zeitslot abhängt ist dieser nicht zu überprüfen
 */
public class FilmVerwaltung {
    static private ArrayList<Kinofilm> filmeFuer3DSaele = new ArrayList<>();
    static private ArrayList<Kinofilm> filmeFuer2DSaele = new ArrayList<>();

    static private ArrayList<Kinofilm> filmeFuer1500Uhr_1730Uhr = new ArrayList<>();
    static private ArrayList<Kinofilm> filmeFuer2000Uhr = new ArrayList<>();
    static private ArrayList<Kinofilm> filmeFuer2300Uhr = new ArrayList<>();

    static private ArrayList<Kinofilm> filmeFuer150minSlotlaenge = new ArrayList<>();
    static private ArrayList<Kinofilm> filmeFuer180minSlotlaenge = new ArrayList<>();

    static private ArrayList<Kinofilm> filme3D_150_1500_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_150_2300 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_180_2000 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme2D_150_1500_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_150_2300 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_180_2000 = new ArrayList<>();

    /** Debugged
     * Hilfsmethode die die Iteration der setFilmArrays() übernimmt.
     * Da die Filme für die 15:00 und 17:30 Vorstellung das Selbe FSK-Siegel tragen und
     * dementsprechend die gleiche Charakteristiken aufweisen wird die Abfrage des 15:00 Uhr Slots
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

    private static void setFilmArrays(int in_saal3Dfaehig, Spielzeiten in_uhrzeit) {

        switch (in_saal3Dfaehig) {

            //region  3D-Faehiger Saal
            case 1: {
                switch (in_uhrzeit) {
                    case SLOT_1500:
                    case SLOT_1730: {
                        filme3D_150_1500_1730 = new ArrayList<>(filmeFuer3DSaele);
                        filme3D_150_1500_1730.retainAll(filmeFuer1500Uhr_1730Uhr);
                        filme3D_150_1500_1730.retainAll(filmeFuer150minSlotlaenge);
                        break;
                    }

                    case SLOT_2000: {
                        filme3D_180_2000 = new ArrayList<>(filmeFuer3DSaele);
                        filme3D_180_2000.retainAll(filmeFuer2000Uhr);
                        filme3D_180_2000.retainAll(filmeFuer180minSlotlaenge);
                        break;
                    }

                    case SLOT_2300: {
                        filme3D_150_2300 = new ArrayList<>(filmeFuer3DSaele);
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
                        filme2D_150_1500_1730 = new ArrayList<>(filmeFuer2DSaele);
                        filme2D_150_1500_1730.retainAll(filmeFuer1500Uhr_1730Uhr);
                        filme2D_150_1500_1730.retainAll(filmeFuer150minSlotlaenge);
                        break;
                    }

                    case SLOT_2000: {
                        filme2D_180_2000 = new ArrayList<>(filmeFuer2DSaele);
                        filme2D_180_2000.retainAll(filmeFuer2000Uhr);
                        filme2D_180_2000.retainAll(filmeFuer180minSlotlaenge);
                        break;
                    }
                    case SLOT_2300: {
                        filme2D_150_2300 = new ArrayList<>(filmeFuer2DSaele);
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

    // Setter
    public static void setFilmeFuer3DSaele(Kinofilm in_film) {
        filmeFuer3DSaele.add(in_film);
    }

    public static void setFilmeFuer2DSaele(Kinofilm in_film) {
        filmeFuer2DSaele.add(in_film);
    }

    public static void setFilmeFuer1500Uhr_1730Uhr(Kinofilm in_film) {
        filmeFuer1500Uhr_1730Uhr.add(in_film);
    }

    public static void setFilmeFuer2000Uhr(Kinofilm in_film) {
        filmeFuer2000Uhr.add(in_film);
    }

    public static void setFilmeFuer2300Uhr(Kinofilm in_film) {
        filmeFuer2300Uhr.add(in_film);
    }

    public static void setFilmeFuer150minSlotlaenge(Kinofilm in_film) {
        filmeFuer150minSlotlaenge.add(in_film);
    }

    public static void setFilmeFuer180minSlotlaenge(Kinofilm in_film) {
        filmeFuer180minSlotlaenge.add(in_film);
    }

    // Getter
//    public static ArrayList<Kinofilm> getFilme(boolean in_ThreeD, Spielzeiten in_timeslot) {
//        int switchSlotDuration;
//        if (in_timeslot.getSlotDuration() == 150) {
//            switchSlotDuration = 150;
//        } else {
//            switchSlotDuration = 180;
//        }
//
//        int switchThreeD;
//        if (in_ThreeD) {
//            switchThreeD = 1;
//        } else {
//            switchThreeD = 0;
//        }
//
//        switch (switchThreeD) {
//
//            // 3D Film
//            case 1: {
//                switch (switchSlotDuration) {
//
//                    case 150: {
//                        switch (in_timeslot) {
//                            case SLOT_1500: {
//                                return filme3D_150_1500_1730;
//                            }
//                            case SLOT_1730: {
//                                return filme3D_150_1730;
//                            }
//                            case SLOT_2300: {
//                                return filme3D_150_2300;
//                            }
//                        }
//                    }
//                    break;
//
//                    case 180: {
//                        switch (in_timeslot) {
//                            case SLOT_2000: {
//                                return filme3D_180_2000;
//                            }
//                        }
//                    }
//                    break;
//
//                    default:
//                        return null;
//                }
//            }
//            break;
//
//            // 2D Film
//            case 0: {
//                switch (switchSlotDuration) {
//
//                    case 150: {
//                        switch (in_timeslot) {
//                            case SLOT_1500: {
//                                return filme2D_150_1500_1730;
//                            }
//                            case SLOT_1730: {
//                                return filme2D_150_1730;
//                            }
//                            case SLOT_2300: {
//                                return filme2D_150_2300;
//                            }
//                        }
//                    }
//                    break;
//
//                    case 180: {
//                        switch (in_timeslot) {
//                            case SLOT_2000: {
//                                return filme2D_180_2000;
//                            }
//                        }
//                    }
//                    break;
//
//                    default:
//                        return null;
//                }
//            }
//            break;
//
//            default:
//                return null;
//        }
//        return null;
//    }
}
