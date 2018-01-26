package Gruppe7.Importer;

import Gruppe7.Data.*;
import java.util.ArrayList;

public class KinofilmImporter extends Datei {

    private Datei importFileKinofilme;
    private String importString;
    private int importKinofilmFskInt;
    private Fsk importKinofilmFSK;

    private String importKinofilmGemresString;
    private ArrayList<Genre> importKinofilmGenres;

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

        importFileKinofilme = new Datei(in_name);
        importFileKinofilme.openInFile_FS();

        //Schleife läuft bis zum Ende der Datei.
        while (true) {

            //Jede Zeile wird in importString eingelesen, es sei denn, in der letzten Zeile steht nichts drin.
            importString = importFileKinofilme.readLine_FS();

            // Schleifen Abbruch statement
            if (importString == null) {
                break;
            }

            String[] arrayKinofilm;

            //Zerlegt den Import String (Zeile der Datei) und erstellt ein Array.
            arrayKinofilm = importString.split(";");

            //Die einzelnen Array Positionen werden nachfolgend Variablen der Klasse zugewiesen.

            //FSK des aktuellen Films
            //Hier wird der int Wert der FSK ausgewertet und dafür eine Enumeration gesetzt.
            importKinofilmFskInt = Integer.valueOf(arrayKinofilm[2]);
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
            //Test ob FSK Zusweisung erfolgreich war.

            //Genres Auslesen und Zuweisen
            //Für jeden Durchgang muss eine neue Liste erstellt werden.
            //Es wird eine ArrayList benötigt, da ein Film mehrere Genres haben kann.
            importKinofilmGenres = new ArrayList<>();

            //Erstellung eines String mit allen Genres
            importKinofilmGemresString = String.valueOf(arrayKinofilm[3]);

            //Der String wird aufgeteilt
            String arrayGenre[] = importKinofilmGemresString.split(",");
            for (int i = 0; i < arrayGenre.length; i++) {
                //Leerzeichen entfernen
                arrayGenre[i] = arrayGenre[i].trim();
            }

            //Die ausgelesenen Genres werden gepfrüft und eindeutigen Enums zugewiesen.
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

            Kinofilm tempKinofilmNachBeliebtheitsfilter = (KinofilmFilter(minBeliebtheit, tempKinofilm));

            KinofilmVerteiler3D2D(tempKinofilmNachBeliebtheitsfilter);
            KinofilmVerteilerTimeslot(tempKinofilmNachBeliebtheitsfilter);
            KinofilmVerteilerLaufzeit(tempKinofilmNachBeliebtheitsfilter);
        }
    }

    /**
     * Fügt einen Kinofilm der Sammlung hinzu, wenn dessen Beliebtheitswert den Schwellenwert überschreitet
     *
     * @param in_beliebtheitsSchwellenwert Beliebtheitswert zwischen 0 und 100
     * @param in_film                      ein Kinofilm
     */
    private Kinofilm KinofilmFilter(int in_beliebtheitsSchwellenwert, Kinofilm in_film) {
        if (in_film.getBeliebtheit() >= in_beliebtheitsSchwellenwert) {
            return in_film;
        }
        return null;
    }

    /**
     * Fügt einen Film entsprechend seiner 3D-eigenschaft zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film im entsprechenden Saal gezeigt werden kann.
     * Ein 2D-Film kann sowohl in 3D-Sälen als auch in 2D-Sälen gezeigt werden.
     * <p>
     * Ein 2D-Film wird also zur Liste der 3D-Filme hinzugefügt, da er in 3D-Sälen auch gezeigt werden kann
     *
     * @param in_film ein Kinofilm
     */
    private void KinofilmVerteiler3D2D(Kinofilm in_film) {
        if (in_film != null) {
            if (in_film.getThreeD()) {
                FilmVerwaltung.setFilme3D(in_film);
            } else {
                FilmVerwaltung.setFilme2D(in_film);
                FilmVerwaltung.setFilme3D(in_film);
            }
        }
    }

    /**
     * Fügt einen Film entsprechend seiner FSK-Eigenschaften zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film zum entsprechenden Timeslot gezeigt werden darf.
     * Ein FSK0 kann also zu jeder Tageszeit, auch um 23:00 gezeigt werden.
     *
     * @param in_film ein Kinofilm
     */
    private void KinofilmVerteilerTimeslot(Kinofilm in_film) {
        if (in_film != null) {
            switch (in_film.getFsk()) {
                case FSK_0:
                case FSK_6:
                case FSK_12: {
                    FilmVerwaltung.setFilme1500(in_film);
                    FilmVerwaltung.setFilme1730(in_film);
                    FilmVerwaltung.setFilme2000(in_film);
                    FilmVerwaltung.setFilme2300(in_film);
                    break;
                }
                case FSK_16: {
                    FilmVerwaltung.setFilme2000(in_film);
                    FilmVerwaltung.setFilme2300(in_film);
                    break;
                }
                case FSK_18: {
                    FilmVerwaltung.setFilme2300(in_film);
                    break;
                }
                default: break;
            }
        }
    }

    private void KinofilmVerteilerLaufzeit(Kinofilm in_film) {
        // Switch arbeitet nicht mit komplexen typen, also Übersetzung
        if (in_film != null) {
            int switchIntLaufzeit;
            if (in_film.getLaufzeit() <= 150) {
                switchIntLaufzeit = 150;
            }
            else if (in_film.getLaufzeit() <= 180) {
                switchIntLaufzeit = 180;
            }
            else { switchIntLaufzeit = 150; }

            switch (switchIntLaufzeit) {
                case 150: {
                    FilmVerwaltung.setFilme150min(in_film);
                    break;
                }

                case 180: {
                    FilmVerwaltung.setFilme150min(in_film);
                    break;
                }

                default:
                    break;
            }
        }
    }
}








