package Gruppe7.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;

/**
 * @author Lennart Völler
 * @date 25.01.2018
 */
public class Vorstellung {

    //Attribute
    private Kinofilm vorstellungsFilm;
    private ArrayList<Werbefilm> werbungen = new ArrayList<>();
    private Saal vorstellungsSaal;
    private Spielzeiten vorstellungsTimeslot;
    private int eintrittspreis;

    //Constructor
    public Vorstellung(int in_saalIndex, int in_vorstellungsTimeslotIndex) {
        vorstellungsSaal = SaalVerwaltung.getSaele().get(in_saalIndex);
        vorstellungsTimeslot = Spielzeiten.values()[in_vorstellungsTimeslotIndex];

        //Für Saal und Timeslot gültige Kinofilme werden in ein Set gejoint, aus dem dann zufällig ausgewählt wird.
        Set<Kinofilm> filmSet = FilmVerwaltung.getFilmSet(vorstellungsSaal.getThreeD(), vorstellungsTimeslot);

        // TODO: Möglichkeit zur Effizienten Filmauswahl, schafft es aber nicht durch den GenreCheck
//        for (Kinofilm film: filmSet){
//            if ((double)film.getBeliebtheit() / (double)film.getVerleihpreisProWoche() >
//                    ((double)vorstellungsFilm.getBeliebtheit() / (double)vorstellungsFilm.getVerleihpreisProWoche())){
//                vorstellungsFilm = film;
//            }
//        }

        // TODO: Was ist mit der Sprache?
        // Zufälligen Film aus dem Set auswählen.
        vorstellungsFilm = (Kinofilm)filmSet.toArray()[ThreadLocalRandom.current().nextInt(0, filmSet.size() - 1)];

        /* Der Andrang wird bei einem Eintrittspreis von >=27 --> 0 (obergrenze)
         * Die Kinosäle sind bei einem Eintrittspres von <=7 nah an der maximalen Kapazität (untergrenze)
         */
        eintrittspreis = ThreadLocalRandom.current().nextInt(10, 18);

        //Werbung hinzufügen
        werbungen = werbungAnhaengen();
    }

    /**
     * Je nach verbleibender Zeit zum Zeigen von Werbung wird eine Liste mit den besten Profitabilitätswerten
     * (UmsatzProZuschauer/Laufzeit) erstellt. Die Zeit zum Zeigen von Werbung ist auf 20 Minuten begrenzt. Für
     * den Fall, dass 20 Minuten Werbung gezeigt werden können wird ein Standard-Werbeblock verwendet.
     * @return Eine ArrayList der Werbung einer Vorstellung
     */
    private ArrayList<Werbefilm> werbungAnhaengen() {
        int werbeDauerSoll = vorstellungsTimeslot.getSlotDuration() - vorstellungsFilm.getLaufzeit();

        if (werbeDauerSoll >= 20) {
            return WerbefilmVerwaltung.getWerbefilme20MinutenStandard();
        } else {
            int werbeDauerIst = 0; // TODO: Subtraktionsansatz, solange vom 20Min standard subtrahieren bis es passt.

            ArrayList<Werbefilm> output = new ArrayList<>();

            for (Werbefilm werbung : WerbefilmVerwaltung.getWerbefilme()) {
                if ((werbeDauerIst + werbung.getLaufzeit()) <= werbeDauerSoll) {
                    output.add(werbung);
                    werbeDauerIst += werbung.getLaufzeit();
                }
            }
            return output;
        }
    }

    //Getter
    public Kinofilm getKinofilm(){ return vorstellungsFilm; }
    public Saal getSaal(){ return vorstellungsSaal; }
    public Spielzeiten getSpielzeiten(){ return vorstellungsTimeslot; }
    public ArrayList<Werbefilm> getWerbefilme(){ return werbungen; }
    public int getEintrittspreis() { return eintrittspreis; }

    @Override
    public String toString() {
        String output = "";
        // Saal
        output += "Saal: " + vorstellungsSaal.getSaalNummer() + "\n";

        //Uhrzeit
        output += "Uhrzeit: " + vorstellungsTimeslot + "\n";

        // Film
        output += "Titel: " + vorstellungsFilm.getTitel()+ "\n" +
                  "Regisseur: " + vorstellungsFilm.getRegisseur()+ "\n" +
                  "Laufzeit: " + vorstellungsFilm.getLaufzeit()+ "\n" +
                  "FSK: " + vorstellungsFilm.getFsk()+ "\n" +
                  "Genre: " + vorstellungsFilm.getGenre()+ "\n" +
                  "Sprache: " + vorstellungsFilm.getSprache()+ "\n" +
                  "Land: " + vorstellungsFilm.getLaufzeit()+ "\n";

        //Financials
        output += "Beliebtheit: " + vorstellungsFilm.getBeliebtheit() + "\n"+
                  "Verleihpreis: " + vorstellungsFilm.getVerleihpreisProWoche() + "\n";

        output += "-----------------------------\n";
        return output;
    }
}
