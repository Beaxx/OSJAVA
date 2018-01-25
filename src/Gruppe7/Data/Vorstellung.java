package Gruppe7.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;

public class Vorstellung {

    //Attribute
    private Kinofilm vorstellungsFilm;
    private ArrayList<Werbefilm> werbungen = new ArrayList<>();
    private Saal vorstellungsSaal;
    private Spielzeiten vorstellungsTimeslot;
    private int eintrittspreis = 7; // TODO: Hardcoded

    //Constructor
    public Vorstellung(int in_saalIndex, int in_vorstellungsTimeslotIndex)
    {
        vorstellungsSaal = SaalVerwaltung.getSaele().get(in_saalIndex);
        vorstellungsTimeslot = Spielzeiten.values()[in_vorstellungsTimeslotIndex];

        //Für Saal und Timeslot gültige Kinofilme werden in ein Set gejoint, aus dem dann zufällig ausgewählt wird.
        Set<Kinofilm> filmSet = FilmVerwaltung.getFilmSet(vorstellungsSaal.getThreeD(), vorstellungsTimeslot);

        int randomIndex = ThreadLocalRandom.current().nextInt(0, filmSet.size()-1);
        Iterator<Kinofilm> iter = filmSet.iterator();
        for (int i = 0; i < randomIndex; i++) {
            iter.next();
        }
        vorstellungsFilm =  iter.next();

        //Werbung hinzufügen
        werbungen = werbungAnhaengen(); // TODO: Wenn Werbeblock 20min Standard Werbeblock anhängen.
    }
    //Werbung anhängen
    /**
     * Je nach verbleibender Zeit zum Zeigen von Werbung wird eine Liste mit dem besten Profitabilitätswert
     * (UmsatzProZuschauer/Laufzeit) erstellt.
     * @return Eine ArrayList der Werbung einer Vorstellung
     */
    private ArrayList<Werbefilm> werbungAnhaengen(){
        int werbeDauerSoll = vorstellungsTimeslot.getSlotDuration() - vorstellungsFilm.getLaufzeit();
            if (werbeDauerSoll > 20) { werbeDauerSoll = 20;}

        int werbeDauerIst = 0;

        ArrayList<Werbefilm> output = new ArrayList<>();

        for (Werbefilm werbung : WerbefilmVerwaltung.getWerbefilme()){
            if ((werbeDauerIst + werbung.getLaufzeit()) <= werbeDauerSoll){
                output.add(werbung);
                werbeDauerIst += werbung.getLaufzeit();
            }
        }
        return output;
    }


   //alte Check Methoden
//    //Check 3D
//    private boolean check3D(Kinofilm vorstellungsFilm, Saal vorstellungsSaal) {
//
//        //Wenn der Saal 3D-Fähig ist, immer True
//        if (vorstellungsSaal.getThreeD())
//            return true;
//
//        //Wenn Saal 2D und der Film auch
//        return !vorstellungsFilm.getThreeD() && !vorstellungsSaal.getThreeD();
//    }
//
//    //Check FSK
//    private boolean checkFSK(Spielzeiten vorstellungsTimeslot, Kinofilm vorstellungsFilm) {
//
//        // Um 15 Uhr und um 17:30 dürfen keine FSK16 und FSK18 Filme gezeigt werden
//        if ((vorstellungsTimeslot == Spielzeiten.SLOT_1500 || vorstellungsTimeslot == Spielzeiten.SLOT_1730) &&
//                (vorstellungsFilm.getFsk() == Fsk.FSK_16 || vorstellungsFilm.getFsk() == Fsk.FSK_18)){
//            return false;}
//
//        // Um 20:00 dürfen keine FSK18 Filme gezeigt werden
//        else return vorstellungsTimeslot != Spielzeiten.SLOT_2000 || vorstellungsFilm.getFsk() != Fsk.FSK_18;
//    }
//
//    //Check Film Laufzeiten
//    private boolean checkLaufzeiten(Kinofilm vorstellungsFilm, Spielzeiten vorstellungsTimeslot) {
//        return vorstellungsTimeslot.getSlotDuration() >= vorstellungsFilm.getLaufzeit();
//    }

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
