package Gruppe7.Importer;

import Gruppe7.Data.FilmVerwaltung;
import Gruppe7.Data.Fsk;
import Gruppe7.Data.Genre;
import Gruppe7.Data.Kinofilm;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Fabian Ueberle
 * Erbt von Datei
 *
 * Der KinofilmImporter ließt zeilenweise Kinofilminformationen aus der "filme.csv"-Datei aus dem Datensatz.
 */
public class KinofilmImporter extends Datei {

    private int minBeliebtheit;

    /**
     * Erstellt aus den serialisierten Kinofilmobjekten in der Import-Datei Kinofilm-Objekte.
     *
     * @param in_Name           Name und Pfad der Importdatei
     * @param in_MinBeliebtheit Mindestbeliebtheitswert unter dem keine Filme importiert werden.
     *
     */
    public KinofilmImporter(String in_Name, int in_MinBeliebtheit) {
        super(in_Name);

        minBeliebtheit = in_MinBeliebtheit;

        Datei importFileKinofilme = new Datei(in_Name);
        importFileKinofilme.openInFile_FS();

        while (true) {

            //Jede Zeile wird in importString eingelesen, es sei denn, in der letzten Zeile steht nichts drin.
            String importString = importFileKinofilme.readLine_FS();

            // Schleifen Abbruch statement
            if (importString == null) {
                break;
            }

            //Prüft auf Array Größe
            if (!dataValidation(importString, in_Name) || importFileKinofilme.errorCode != 0) {
                System.err.println("Fehler in Spaltenstruktur.");

                while (!dataValidation(importString, in_Name)){
                    importString = importFileKinofilme.readLine_FS();

                    if (importString == null || importFileKinofilme.errorCode != 0) {
                        break;
                    }
                    dataValidation(importString, in_Name);
                }
            }

            //Zerlegt den Import String (Zeile der Datei) und erstellt ein Array.
            String[] arrayKinofilm;
            arrayKinofilm = importString.split(";");

            //Validierung der wichtigsten Spalten.
            if (
                    //FSK Prüfung
                    !checkForInt(arrayKinofilm[2]) ||
                    !checkForValidFSK(Integer.valueOf(arrayKinofilm[2])) ||
                    //Preis Prüfung
                    !checkForInt(arrayKinofilm[4]) ||
                    //Beliebtheit Prüfung
                    !checkForInt(arrayKinofilm[5]) ||
                    !checkForValidBeliebtheit(Integer.valueOf(arrayKinofilm[5])) ||
                    //Spielzeit Prüfung
                    !checkForInt(arrayKinofilm[6]) ||
                    !checkForValidSpielzeit(Integer.valueOf(arrayKinofilm[6])) ||
                    //Prüfung auf Jahr
                    !checkForInt(arrayKinofilm[9])||
                    !isValidJahr(Integer.valueOf(arrayKinofilm[9])) ||
                    //Prüung auf 3D
                    !checkForBoolean(arrayKinofilm[10])) {



                    System.err.println("Fehler in der Datei "+in_Name+". Fehlerhafte Datensätze wurden übersprungen.");

                            while ( !checkForInt(arrayKinofilm[2])||
                                    !checkForValidFSK(Integer.valueOf(arrayKinofilm[2]))||
                                    //Preis Prüfung
                                    !checkForInt(arrayKinofilm[4])||
                                    //Beliebtheit Prüfung
                                    !checkForInt(arrayKinofilm[5])||
                                    !checkForValidBeliebtheit(Integer.valueOf(arrayKinofilm[5]))||
                                    //Spielzeit Prüfung
                                    !checkForInt(arrayKinofilm[6])||
                                    !checkForValidSpielzeit(Integer.valueOf(arrayKinofilm[6]))||
                                    //Prüfung auf Jahr
                                    !checkForInt(arrayKinofilm[9])||
                                    !isValidJahr(Integer.valueOf(arrayKinofilm[9]))||
                                    //Prüung auf 3D
                                    !checkForBoolean(arrayKinofilm[10])
                                    )
                            {
                                importString=importFileKinofilme.readLine_FS(); //nächste Zeile
                                if (importString == null) {break;}
                                arrayKinofilm = importString.split(";");  //Array neu bestücken.
                            }
            }

            //region FSK des aktuellen Films
            int importKinofilmFskInt = Integer.valueOf(arrayKinofilm[2]);
            Fsk importKinofilmFSK;
            if (importKinofilmFskInt == 18) {
                importKinofilmFSK = Fsk.FSK_18;
            }
            else if (importKinofilmFskInt == 16) {
                importKinofilmFSK = Fsk.FSK_16;
            }
            else if (importKinofilmFskInt == 12) {
                importKinofilmFSK = Fsk.FSK_12;
            }
            else if (importKinofilmFskInt == 6) {
                importKinofilmFSK = Fsk.FSK_6;
            }
            else if (importKinofilmFskInt == 0) {
                importKinofilmFSK = Fsk.FSK_0;
            }

            /* Ist die FSK-Einstufung nicht lesbar wird FSK = 18 gesetzt um sicher zu gehen, das der Jugendschutz
            nicht verletzt wird. */
            else{ importKinofilmFSK = Fsk.FSK_18; }
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
                else if (inputGenre.trim().equals("Dokumentation")) {
                    importKinofilmGenres.add(Genre.DOKUMENTATION);
                }
                else if (inputGenre.trim().equals("Drama")) {
                    importKinofilmGenres.add(Genre.DRAMA);
                }
                else if (inputGenre.trim().equals("Horror")) {
                    importKinofilmGenres.add(Genre.HORROR);
                }
                else if (inputGenre.trim().equals("Komödie")) {
                    importKinofilmGenres.add(Genre.KOMOEDIE);
                }
                else if (inputGenre.trim().equals("Krimi")) {
                    importKinofilmGenres.add(Genre.KRIMI);
                }
                else if (inputGenre.trim().equals("Science Fiction")) {
                    importKinofilmGenres.add(Genre.SCIENCE_FICTION);
                }
                else if (inputGenre.trim().equals("Zeichentrick")) {
                    importKinofilmGenres.add(Genre.ZEICHENTRICK);
                }
                else if (inputGenre.trim().equals("Thriller")) {
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

            //Beliebtheitscheck -> Verteilung auf ArrayListen in Verwaltungsklasse.
            if (kinofilmFilter(tempKinofilm)) {
                kinofilmVerteiler3D2D(tempKinofilm);
                kinofilmVerteilerTimeslot(tempKinofilm);
                kinofilmVerteilerLaufzeit(tempKinofilm);
            }
        }
    }

    /**
     * @author Lennart Völler
     * <p>
     * Ermittelt, ob ein Film die Mindestbeliebtheit erfüllt.
     */
    private boolean kinofilmFilter(Kinofilm in_Film) {
        return in_Film.GetBeliebtheit() >= minBeliebtheit;
    }

    /**
     * @param in_Film ein Kinofilm
     * @author Lennart Völler
     * <p>
     * Fügt einen Film entsprechend seiner 3D-eigenschaft zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film im entsprechenden Saal gezeigt werden könnte.
     * Ein 2D-Film kann sowohl in 3D-Sälen als auch in 2D-Sälen gezeigt werden.
     * Ein 2D-Film wird daher zur Liste der 3D-Filme hinzugefügt, da er in 3D-Sälen auch gezeigt werden kann.
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
     * @param in_Film ein Kinofilm
     * @author Lennart Völler
     * <p>
     * Fügt einen Film entsprechend seiner FSK-Einstufung zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film zum entsprechenden Timeslot gezeigt werden darf.
     * Ein FSK0 kann also zu jeder Tageszeit, auch um 23:00 gezeigt werden.
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
     * @param in_Film ein Kinofilm
     * @author Lennart Völler
     * <p>
     * Fügt einen Film entsprechend seiner Spieldauer zu den Kinofilmlisten hinzu.
     * Entscheidungskriterium ist, ob ein Film in den entsprechenden Timeslot passt.
     * Ein 150 Minuten langer film kann also auch in einem 180 Minuten timeslot laufen.
     */
    private void kinofilmVerteilerLaufzeit(Kinofilm in_Film) {

        if (in_Film.GetLaufzeit() <= 150) {
            FilmVerwaltung.SetFilmeFuer150minSlotlaenge(in_Film);
            FilmVerwaltung.SetFilmeFuer180minSlotlaenge(in_Film);
        } else if (in_Film.GetLaufzeit() > 150) {
            FilmVerwaltung.SetFilmeFuer180minSlotlaenge(in_Film);
        }
    }

    /**
     * @param  in_importstring der einzulesende String für die späteren Objektinstanzen
     * @param  in_name Name und Pfade der Importdatei
     * @author Fabian Ueberle
     * <p>
     *     Die Methode dataValidation() prüft jede Zeile der Importdatei ob diese die erwartete Struktur aufweist.
     *     Dies soll zum einen einen Absturz des Programms sowie die Erzeugung unvollständiger Objekte vermeiden.
     *     Die ausgegebene Fehlermeldung soll den Anwender auf die betroffene Datei hinweisen.
     * </p>
     * */
    private boolean dataValidation(String in_importstring, String in_name){

       String array[] = in_importstring.split(";");

        if (array.length!=11){
            System.err.println("Fehlerhafte Importdatei für Kinofilme. Die Fehlerhafte Zeile wird übersprungen. " +
                    "Bitte prüfen Sie Ihre Datei "+ in_name+" auf 11 Spalten.");
            return false;

        }
        return true;
    }

    /**
     * @param  in_InputCheck der zu prüfende String
     * @author Fabian Ueberle
     * <p>
     *     Die Methode checkForInt prüft ob der aktuelle String einen Integer Wert ist.
     * </p>
     * */

    private boolean checkForInt(String in_InputCheck) {
        try {
            Integer.valueOf(in_InputCheck);
            return true;
        } catch (NumberFormatException e) {
            System.err.println("Kein Integer Wert");
            return false;
        }
    }

    /**
     * @param  in_InputCheck der zu prüfende Wert
     * @author Fabian Ueberle
     * <p>
     *     Die Methode checkForBoolean prüft ob der aktuelle Wert ein Boolen ist.
     * </p>
     * */

    private boolean checkForBoolean(String in_InputCheck) {
        try {
            Boolean.valueOf(in_InputCheck);
            return true;
        } catch (NumberFormatException e) {
            System.err.println("Kein Boolean Wert");
            return false;
        }
    }

    /**
     * @param  in_Input der zu prüfende String bzw. Integer.
     * @author Fabian Ueberle
     * <p>
     *     Die Methode checkForValidFSK prüft oder der aktuelle String bzw. Integer einen validen FSK Wert aufweißt.
     * </p>
     * */

    private boolean checkForValidFSK(Integer in_Input) {
        try {
            if ((in_Input == 0 || in_Input == 6 || in_Input == 12 || in_Input == 16 || in_Input == 18)) {
                return true;
            }
            throw new Exception();

        } catch (Exception e) {
            System.err.println("Fehlerhafter FSK Wert");
            return false;
        }
    }

    /**
     * @param  in_Input der zu prüfende String bzw. Integer
     * @author Fabian Ueberle
     * <p>
     *     Die Methode checkForValidBeliebtheit prüft oder der aktuelle Integer Wert für die Beliebtheit
     *     zwischen 0 und 100 liegt.
     * </p>
     * */

        private boolean checkForValidBeliebtheit(Integer in_Input) {
            try {
                if (in_Input >= 0 && in_Input <= 100) {
                    return true;
                }
                throw new Exception();

            } catch (Exception e) {
                System.err.println("Beliebtheit außerhalb der Norm von 0-100.");

                return false;
            }

        }

    /**
     * @param  in_Input der zu prüfende String bzw. Integer
     * @author Fabian Ueberle
     * <p>
     *     Die Methode isValidJahr prüft ob das Erscheinungsjahr zwischen 1900 und dem aktuellen Jahr liegt.
     * </p>
     * */

    private boolean isValidJahr(Integer in_Input) {

        Calendar cal = Calendar.getInstance();
        int jahr = cal.get(Calendar.YEAR);

        try {
            if (in_Input >= 1900 && in_Input <= jahr) {
                return true;
            }
            throw new Exception();

        } catch (Exception e) {
            System.err.println("Erscheinungsjahr außerhalb der Norm von 1900-akutelles Jahr.");

            return false;
        }
    }

    /**
     * @param  in_Input der zu prüfende String bzw. Integer
     * @author Fabian Ueberle
     * <p>
     *     Die Methode checkForValidSpielzeit prüft ob die Filmspielzeit größer 0 und kleiner 180 (maximal Filmlänge)
     *     liegt. Filme mit mehr als 180 min können nicht gezeigt werden.
     * </p>
     * */

     private boolean checkForValidSpielzeit(Integer in_Input){

            int checkedInputSpielzeit = in_Input;

            try {
                if (checkedInputSpielzeit > 0 && checkedInputSpielzeit <= 180) {
                    return true;
                }
                throw new Exception();

            } catch (Exception e) {
                System.err.println("Unzulässige Spielzeitdauer.");
                return false;
            }
     }
}