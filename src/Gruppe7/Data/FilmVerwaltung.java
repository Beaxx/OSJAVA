package Gruppe7.Data;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Lennart Völler
 * @date 25.01.2018
 * Importdauer mit diesem Verfahren 79 Millisekunden.
 */
public class FilmVerwaltung
{
    static private ArrayList<Kinofilm> filme3D = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D = new ArrayList<>();

    static private ArrayList<Kinofilm> filme1500 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2000 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2300 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme150min = new ArrayList<>();
    static private ArrayList<Kinofilm> filme180min = new ArrayList<>();

    static private ArrayList<Kinofilm> filme3D_150_1500 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_150_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_150_2300 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme3D_180_2000 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme2D_150_1500 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_150_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_150_2300 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme2D_180_2000 = new ArrayList<>();

    public static void FilmArraysHelper(){
        for (int threeD = 0; threeD < 2; threeD++) {
            for (Spielzeiten timeslot : Spielzeiten.values()) {
                setFilmArrays(threeD, timeslot);
            }
        }
    }

    private static void setFilmArrays(int in_threeD, Spielzeiten in_timeslot) {

        // Switch arbeitet nicht mit boolean, also Übersetzung
        int switchSlotDuration;
        if (in_timeslot.getSlotDuration() == 150) {
            switchSlotDuration = 150;
        } else {
            switchSlotDuration = 180;
        }

        switch (in_threeD) {

            // 3D Film
            case 1: {
                switch (switchSlotDuration) {

                    case 150: {
                        switch (in_timeslot) {
                            case SLOT_1500: {
                                filme3D_150_1500 = new ArrayList<>(filme3D);
                                filme3D_150_1500.retainAll(filme1500);
                                filme3D_150_1500.retainAll(filme150min);
                                break;
                            }
                            case SLOT_1730: {
                                filme3D_150_1730 = new ArrayList<>(filme3D);
                                filme3D_150_1730.retainAll(filme1730);
                                filme3D_150_1730.retainAll(filme150min);
                                break;
                            }
                            case SLOT_2300: {
                                filme3D_150_2300 = new ArrayList<>(filme3D);
                                filme3D_150_2300.retainAll(filme2300);
                                filme3D_150_2300.retainAll(filme150min);
                                break;
                            }
                        }
                    }
                    break;

                    case 180: {
                        switch (in_timeslot) {
                            case SLOT_2000: {
                                filme3D_180_2000 = new ArrayList<>(filme3D);
                                filme3D_180_2000.retainAll(filme2000);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            break;

            // 2D Film
            case 0: {
                switch (switchSlotDuration) {

                    case 150: {
                        switch (in_timeslot) {
                            case SLOT_1500: {
                                filme2D_150_1500 = new ArrayList<>(filme2D);
                                filme2D_150_1500.retainAll(filme1500);
                                filme2D_150_1500.retainAll(filme150min);
                                break;
                            }
                            case SLOT_1730: {
                                filme2D_150_1730 = new ArrayList<>(filme2D);
                                filme2D_150_1730.retainAll(filme1730);
                                filme2D_150_1730.retainAll(filme150min);
                                break;
                            }
                            case SLOT_2300: {
                                filme2D_150_2300 = new ArrayList<>(filme2D);
                                filme2D_150_2300.retainAll(filme2300);
                                filme2D_150_2300.retainAll(filme150min);
                                break;
                            }
                        }
                    }
                    break;

                    case 180: {
                        switch (in_timeslot) {
                            case SLOT_2000: {
                                filme2D_180_2000 = new ArrayList<>(filme2D);
                                filme2D_180_2000.retainAll(filme2000);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            break;
        }
    }

    // Setter
    public static void setFilme3D(Kinofilm in_film) {filme3D.add(in_film);}
    public static void setFilme2D(Kinofilm in_film) {filme2D.add(in_film);}

    public static void setFilme1500(Kinofilm in_film) {filme1500.add(in_film);}
    public static void setFilme1730(Kinofilm in_film) {filme1730.add(in_film);}
    public static void setFilme2000(Kinofilm in_film) {filme2000.add(in_film);}
    public static void setFilme2300(Kinofilm in_film) {filme2300.add(in_film);}

    public static void setFilme150min(Kinofilm in_film) {filme150min.add(in_film);}
    public static void setFilme180min(Kinofilm in_film) {filme180min.add(in_film);}

    // Getter
    public static ArrayList<Kinofilm> getFilme(boolean in_ThreeD, Spielzeiten in_timeslot){
        int switchSlotDuration;
        if (in_timeslot.getSlotDuration() == 150) {
            switchSlotDuration = 150;
        } else {
            switchSlotDuration = 180;
        }

        int switchThreeD;
        if (in_ThreeD) { switchThreeD = 1; }
            else { switchThreeD = 0; }

        switch (switchThreeD) {

            // 3D Film
            case 1: {
                switch (switchSlotDuration) {

                    case 150: {
                        switch (in_timeslot) {
                            case SLOT_1500: {
                                return filme3D_150_1500;
                            }
                            case SLOT_1730: {
                                return filme3D_150_1730;
                            }
                            case SLOT_2300: {
                                return filme3D_150_2300;
                            }
                        }
                    }
                    break;

                    case 180: {
                        switch (in_timeslot) {
                            case SLOT_2000: {
                                return filme3D_180_2000;
                            }
                        }
                    }
                    break;

                    default: return null;
                }
            }
            break;

            // 2D Film
            case 0: {
                switch (switchSlotDuration) {

                    case 150: {
                        switch (in_timeslot) {
                            case SLOT_1500: {
                                return filme2D_150_1500;
                            }
                            case SLOT_1730: {
                                return filme2D_150_1730;
                            }
                            case SLOT_2300: {
                                return filme2D_150_2300;
                            }
                        }
                    }
                    break;

                    case 180: {
                        switch (in_timeslot) {
                            case SLOT_2000: {
                                return filme2D_180_2000;
                            }
                        }
                    }
                    break;

                    default: return null;
                }
            }
            break;

            default: return null;
        }
        return null;
    }
}
