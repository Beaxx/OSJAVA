package Gruppe7.Data;

public class Werbefilm extends Film
{
    private int umsatzProZuschauer;

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
}
