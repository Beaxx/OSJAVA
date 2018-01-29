package Gruppe7.Importer;

import Gruppe7.Data.*;
import java.util.ArrayList;

/**
 * @author Fabian Ueberle
 * nur noch doku
 *
 */
public class KinofilmImporter extends Datei {

    private Fsk importKinofilmFSK;
    private int minBeliebtheit;

    /**
     * Konstruktor fuer Objekte der Klasse Datei
     * Legt einen String mit dem Namen der zu bearbeitenden Datei an.
     *
     * @param in_Name (String): Dateiname der benutzt werden soll.
     */
    public KinofilmImporter(String in_Name, int in_minBeliebtheit) {
        super(in_Name);

        minBeliebtheit = in_minBeliebtheit;

        Datei importFileKinofilme = new Datei(in_Name);
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

            if (kinofilmFilter(tempKinofilm)) {
                kinofilmVerteiler3D2D(tempKinofilm);
                kinofilmVerteilerTimeslot(tempKinofilm);
                kinofilmVerteilerLaufzeit(tempKinofilm);
            }

        }
    }

    /**
     * Debugged
     * Ermittelt, ob ein Film die mindest Beliebtheit erfüllt.
     */
    private boolean kinofilmFilter(Kinofilm in_Film) {
        return in_Film.GetBeliebtheit() >= minBeliebtheit;
    }

    /**
     * Debugged
     * Fügt einen Film entsprechend seiner 3D-eigenschaft zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film im entsprechenden Saal gezeigt werden könnte.
     * Ein 2D-Film kann sowohl in 3D-Sälen als auch in 2D-Sälen gezeigt werden.
     * Ein 2D-Film wird daher zur Liste der 3D-Filme hinzugefügt, da er in 3D-Sälen auch gezeigt werden kann
     *
     * @param in_Film ein Kinofilm
     */
    private void kinofilmVerteiler3D2D(Kinofilm in_Film) {
        if (in_Film.GetThreeD()) {
            FilmVerwaltung.SetFilmeFuer3DSaele(in_Film);
        } else {
            FilmVerwaltung.SetFilmeFuer2DSaele(in_Film);
            FilmVerwaltung.SetFilmeFuer3DSaele(in_Film);
        }
    }

    /**
     * Debugged
     * Fügt einen Film entsprechend seiner FSK-Eigenschaften zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film zum entsprechenden Timeslot gezeigt werden darf.
     * Ein FSK0 kann also zu jeder Tageszeit, auch um 23:00 gezeigt werden.
     *
     * @param in_Film ein Kinofilm
     */
    private void kinofilmVerteilerTimeslot(Kinofilm in_Film) {
        switch (in_Film.GetFsk()) {
            case FSK_0:
            case FSK_6:
            case FSK_12: {
                FilmVerwaltung.SetFilmeFuer1500Uhr_1730Uhr(in_Film);
                FilmVerwaltung.SetFilmeFuer2000Uhr(in_Film);
                FilmVerwaltung.SetFilmeFuer2300Uhr(in_Film);
                break;
            }
            case FSK_16: {
                FilmVerwaltung.SetFilmeFuer2000Uhr(in_Film);
                FilmVerwaltung.SetFilmeFuer2300Uhr(in_Film);
                break;
            }
            case FSK_18: {
                FilmVerwaltung.SetFilmeFuer2300Uhr(in_Film);
                break;
            }
            default:
                break;
        }
    }

    /**
     * Debugged
     * Fügt einen Film entsprechend seiner Spieldauer zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film in den entsprechenden Timeslot passt.
     * Ein 150 Minuten langer film kann also auch in einem 180 Minuten timeslot laufen.
     *
     * @param in_Film ein Kinofilm
     */
    private void kinofilmVerteilerLaufzeit(Kinofilm in_Film) {

        if (in_Film.GetLaufzeit() <= 150) {
            FilmVerwaltung.SetFilmeFuer150minSlotlaenge(in_Film);
            FilmVerwaltung.SetFilmeFuer180minSlotlaenge(in_Film);
        } else if (in_Film.GetLaufzeit() > 150) {
            FilmVerwaltung.SetFilmeFuer180minSlotlaenge(in_Film);
        }
    }
}