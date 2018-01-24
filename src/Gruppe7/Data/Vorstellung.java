package Gruppe7.Data;

import com.sun.corba.se.impl.io.TypeMismatchException;

import java.util.stream.IntStream;
import java.util.concurrent.ThreadLocalRandom;

public class Vorstellung {

    //Attribute
    private Kinofilm vorstellungsFilm;
    private Werbefilm[] werbungen; // TODO: Anzhal der Werbefilme über ihre Länge geregelt
    private Saal vorstellungsSaal;
    private Spielzeiten vorstellungsTimeslot;
    private int eintrittspreis = 7; // TODO: Hardcoded

    // Constant
    private static final int werbezeitMax = 20;

    //Constructor
    public Vorstellung()
    {
        //Boolean Check Variablen
        boolean threeD = false;
        boolean FSK = false;
        boolean filmLaufzeit = false;

        // Solange Vorstellungen erstellen, bis gültig
        while (!threeD || !FSK || !filmLaufzeit) {

            //Random Index für Vorstellungserstellung
            int kinofilmIndex = ThreadLocalRandom.current().nextInt(0, FilmVerwaltung.getSize());
            int saalIndex = ThreadLocalRandom.current().nextInt(0, SaalVerwaltung.getSize());
            int vorstellungsTimeslotIndex = ThreadLocalRandom.current().nextInt(0, 3);

            vorstellungsFilm = FilmVerwaltung.getFilme().get(kinofilmIndex);
            vorstellungsSaal = SaalVerwaltung.getSaele().get(saalIndex);
            vorstellungsTimeslot = Spielzeiten.values()[vorstellungsTimeslotIndex];

            threeD = check3D(vorstellungsFilm, vorstellungsSaal);
            FSK = checkFSK(vorstellungsTimeslot, vorstellungsFilm);
            filmLaufzeit = checkLaufzeiten(vorstellungsFilm, vorstellungsTimeslot);
        }

        //Wenn Vorstellung fertig, Werbung anhängen
        werbungAnhängen(vorstellungsFilm, vorstellungsTimeslot);
    }

    /*Check Methoden*/
    //Check 3D
    private boolean check3D(Kinofilm vorstellungsFilm, Saal vorstellungsSaal) {

        //Wenn der Saal 3D-Fähig ist, immer True
        if (vorstellungsSaal.getThreeD())
            return true;

        //Wenn Saal 2D und der Film auch
        return !vorstellungsFilm.getThreeD() && !vorstellungsSaal.getThreeD();
    }

    //Check FSK
    private boolean checkFSK(Spielzeiten vorstellungsTimeslot, Kinofilm vorstellungsFilm) {

        // Um 15 Uhr und um 17:30 dürfen keine FSK16 und FSK18 Filme gezeigt werden
        if ((vorstellungsTimeslot == Spielzeiten.SLOT_1500 || vorstellungsTimeslot == Spielzeiten.SLOT_1730) &&
                (vorstellungsFilm.getFsk() == Fsk.FSK_16 || vorstellungsFilm.getFsk() == Fsk.FSK_18)){
            return false;}

        // Um 20:00 dürfen keine FSK18 Filme gezeigt werden
        else return vorstellungsTimeslot != Spielzeiten.SLOT_2000 || vorstellungsFilm.getFsk() != Fsk.FSK_18;
    }

    //Check Film Laufzeiten
    private boolean checkLaufzeiten(Kinofilm vorstellungsFilm, Spielzeiten vorstellungsTimeslot) {
        return vorstellungsTimeslot.getSlotDuration() >= vorstellungsFilm.getLaufzeit();
    }

    //Check Werbefilme
    private Werbefilm[] werbungAnhängen(Kinofilm vorstellungsFilm, Spielzeiten timeslot){
        int werbeDauer = timeslot.getSlotDuration() - vorstellungsFilm.getLaufzeit();

        for (Werbefilm w: werbungen) { // TODO: Über Intstream?
            sumWerbungDuration += w.getLaufzeit();
        }

        /* Wenn die Summe der Werbezeiten größer ist, als die verbleibende Zeit im Timeslot abzüglich des Hauptfilms
            oder der Werbeblock länger als 20min ist return: false*/
        return (sumWerbungDuration <= (vorstellungsTimeslot.getSlotDuration() - vorstellungsFilm.getLaufzeit())) &&
                (sumWerbungDuration <= werbezeitMax);
    }

    //Getter
    public Kinofilm getKinofilm(){
        return vorstellungsFilm;
    }
    public Saal getSaal(){
        return vorstellungsSaal;
    }
    public Spielzeiten getSpielzeiten(){
        return vorstellungsTimeslot;
    }
    public Werbefilm[] getWerbefilme(){
        return werbungen;
    } // TODO: Festlegung der Anzahl der Webefilmelemente wo?
    public int getEintrittspreis() {
        return eintrittspreis;
    } // TODO immernoch hardcoded

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
