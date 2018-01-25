package Gruppe7.Data;

public abstract class Film
{
    private String titel;
    private int laufzeit;

    public Film(String in_titel, int in_laufzeit)
    {
        titel = in_titel;
        laufzeit = in_laufzeit;
    }

    // Getter
    public String getTitel() { return titel; }
    public int getLaufzeit() { return laufzeit; }
}
