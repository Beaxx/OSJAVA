package Gruppe7.Importer;

import Gruppe7.Data.*;
import java.util.ArrayList;

public class KinofilmImporter extends Datei {

    private Fsk importKinofilmFSK;
    private int minBeliebtheit;

    /**
     * Konstruktor fuer Objekte der Klasse Datei
     * Legt einen String mit dem Namen der zu bearbeitenden Datei an.
     *
     * @param in_name (String): Dateiname der benutzt werden soll.
     */
    public KinofilmImporter(String in_name, int in_minBeliebtheit) {
        super(in_name);

        minBeliebtheit = in_minBeliebtheit;

        Datei importFileKinofilme = new Datei(in_name);
        importFileKinofilme.openInFile_FS();

        while (true) {

            //Jede Zeile wird in importString eingelesen, es sei denn, in der letzten Zeile steht nichts drin.
            String importString = importFileKinofilme.readLine_FS();

            // Schleifen Abbruch statement
            if (importString == null) {
                break;
            }

            String[] arrayKinofilm;

            //Zerlegt den Import String (Zeile der Datei) und erstellt ein Array.
            arrayKinofilm = importString.split(";");

            //region FSK des aktuellen Films
            int importKinofilmFskInt = Integer.valueOf(arrayKinofilm[2]);
            if (importKinofilmFskInt == 18) {
                importKinofilmFSK = Fsk.FSK_18;
            }
            if (importKinofilmFskInt == 16) {
                importKinofilmFSK = Fsk.FSK_16;
            }
            if (importKinofilmFskInt == 12) {
                importKinofilmFSK = Fsk.FSK_12;
            }
            if (importKinofilmFskInt == 6) {
                importKinofilmFSK = Fsk.FSK_6;
            }
            if (importKinofilmFskInt == 0) {
                importKinofilmFSK = Fsk.FSK_0;
            }
            //endregion

            /*Genres Auslesen und Zuweisen
            Für jeden Durchgang muss eine neue Liste erstellt werden.
            Es wird eine ArrayList benötigt, da ein Film mehrere Genres haben kann.*/
            ArrayList<Genre> importKinofilmGenres = new ArrayList<>();

            //Erstellung eines String mit allen Genres
            String importKinofilmGemresString = String.valueOf(arrayKinofilm[3]);

            String arrayGenre[] = importKinofilmGemresString.split(",");
            for (int i = 0; i < arrayGenre.length; i++) {
                arrayGenre[i] = arrayGenre[i].trim();
            }

            //region Die ausgelesenen Genres werden gepfrüft und eindeutigen Enums zugewiesen.
            for (String inputGenre : arrayGenre) {
                if (inputGenre.trim().equals("Action")) {
                    importKinofilmGenres.add(Genre.ACTION);
                }
                if (inputGenre.trim().equals("Dokumentation")) {
                    importKinofilmGenres.add(Genre.DOKUMENTATION);
                }
                if (inputGenre.trim().equals("Drama")) {
                    importKinofilmGenres.add(Genre.DRAMA);
                }
                if (inputGenre.trim().equals("Horror")) {
                    importKinofilmGenres.add(Genre.HORROR);
                }
                if (inputGenre.trim().equals("Komödie")) {
                    importKinofilmGenres.add(Genre.KOMOEDIE);
                }
                if (inputGenre.trim().equals("Krimi")) {
                    importKinofilmGenres.add(Genre.KRIMI);
                }
                if (inputGenre.trim().equals("Science Fiction")) {
                    importKinofilmGenres.add(Genre.SCIENCE_FICTION);
                }
                if (inputGenre.trim().equals("Zeichentrick")) {
                    importKinofilmGenres.add(Genre.ZEICHENTRICK);
                }
                if (inputGenre.trim().equals("Thriller")) {
                    importKinofilmGenres.add(Genre.THRILLER);
                }
            }
            //endregion

            Kinofilm tempKinofilm = new Kinofilm(
                    String.valueOf(arrayKinofilm[0]),
                    Integer.valueOf(arrayKinofilm[6]),
                    Boolean.valueOf(arrayKinofilm[10]),
                    String.valueOf(arrayKinofilm[7]),
                    String.valueOf(arrayKinofilm[1]),
                    Integer.valueOf(arrayKinofilm[9]),
                    String.valueOf(arrayKinofilm[8]),
                    Integer.valueOf(arrayKinofilm[5]),
                    Integer.valueOf(arrayKinofilm[4]),
                    importKinofilmFSK,
                    importKinofilmGenres);

            if(KinofilmFilter(tempKinofilm)){
                KinofilmVerteiler3D2D(tempKinofilm);
                KinofilmVerteilerTimeslot(tempKinofilm);
                KinofilmVerteilerLaufzeit(tempKinofilm);
            }

        }
    }

    /** Debugged
     * Ermittelt, ob ein Film die mindest Beliebtheit erfüllt.
     */
    private boolean KinofilmFilter(Kinofilm in_film) {
        return in_film.GetBeliebtheit() >= minBeliebtheit;
    }

    /** Debugged
     * Fügt einen Film entsprechend seiner 3D-eigenschaft zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film im entsprechenden Saal gezeigt werden könnte.
     * Ein 2D-Film kann sowohl in 3D-Sälen als auch in 2D-Sälen gezeigt werden.
     * Ein 2D-Film wird daher zur Liste der 3D-Filme hinzugefügt, da er in 3D-Sälen auch gezeigt werden kann
     *
     * @param in_film ein Kinofilm
     */
    private void KinofilmVerteiler3D2D(Kinofilm in_film) {
            if (in_film.GetThreeD()) {
                FilmVerwaltung.setFilmeFuer3DSaele(in_film);
            } else {
                FilmVerwaltung.setFilmeFuer2DSaele(in_film);
                FilmVerwaltung.setFilmeFuer3DSaele(in_film);
            }
    }

    /** Debugged
     * Fügt einen Film entsprechend seiner FSK-Eigenschaften zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film zum entsprechenden Timeslot gezeigt werden darf.
     * Ein FSK0 kann also zu jeder Tageszeit, auch um 23:00 gezeigt werden.
     *
     * @param in_film ein Kinofilm
     */
    private void KinofilmVerteilerTimeslot(Kinofilm in_film) {
            switch (in_film.GetFsk()) {
                case FSK_0:
                case FSK_6:
                case FSK_12: {
                    FilmVerwaltung.setFilmeFuer1500Uhr_1730Uhr(in_film);
                    FilmVerwaltung.setFilmeFuer2000Uhr(in_film);
                    FilmVerwaltung.setFilmeFuer2300Uhr(in_film);
                    break;
                }
                case FSK_16: {
                    FilmVerwaltung.setFilmeFuer2000Uhr(in_film);
                    FilmVerwaltung.setFilmeFuer2300Uhr(in_film);
                    break;
                }
                case FSK_18: {
                    FilmVerwaltung.setFilmeFuer2300Uhr(in_film);
                    break;
                }
                default: break;
        }
    }

    /** Debugged
     * Fügt einen Film entsprechend seiner Spieldauer zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film in den entsprechenden Timeslot passt.
     * Ein 150 Minuten langer film kann also auch in einem 180 Minuten timeslot laufen.
     * @param in_film ein Kinofilm
     */
    private void KinofilmVerteilerLaufzeit(Kinofilm in_film) {

        if (in_film.GetLaufzeit() <= 150) {
            FilmVerwaltung.setFilmeFuer150minSlotlaenge(in_film);
            FilmVerwaltung.setFilmeFuer180minSlotlaenge(in_film);
        }

        else if (in_film.GetLaufzeit() > 150) {
            FilmVerwaltung.setFilmeFuer180minSlotlaenge(in_film);
        }
    }
}








