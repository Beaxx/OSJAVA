package Gruppe7.Data;

public class Werbefilm extends Film
{
    private int umsatzProZuschauer;
    private int einnahmenProWerbeSpot = 0;

    public Werbefilm(String in_titel, int in_laufzeit, int in_umsatzProZuschauer)
    {
        super(in_titel, in_laufzeit);
        umsatzProZuschauer = in_umsatzProZuschauer;
    }

    //Getter
    public int getUmsatzProZuschauer()
    {
       return umsatzProZuschauer;
    }
    public int getEinnahmenProWerbeSpot() {return einnahmenProWerbeSpot;}

    //Setter
    public void setEinnahmenProWerbeSpot(int in_EinnahmenProWerbeSpot) {
        einnahmenProWerbeSpot = in_EinnahmenProWerbeSpot;
    }
}
