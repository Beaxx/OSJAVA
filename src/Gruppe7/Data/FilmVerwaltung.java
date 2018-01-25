package Gruppe7.Data;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Lennart Völler
 * @date 25.01.2018
 *
 */
public class FilmVerwaltung
{
    static private ArrayList<Kinofilm> filme3D_150_1500 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_150_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_150_2000 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_150_2300 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme3D_180_1500 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_180_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_180_2000 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme3D_180_2300 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme2D_150_1500 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_150_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_150_2000 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_150_2300 = new ArrayList<>();

    static private ArrayList<Kinofilm> filme2D_180_1500 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_180_1730 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_180_2000 = new ArrayList<>();
    static private ArrayList<Kinofilm> filme2D_180_2300 = new ArrayList<>();

    // Getter
    public static Set<Kinofilm> getFilmSet(boolean in_threeD, Spielzeiten in_timeslot){

        // Switch arbeitet nicht mit boolean, also Übersetzung
        int switchInt3D;
        if (in_threeD) { switchInt3D = 1;}
            else { switchInt3D = 0;}

        int switchSlotDuration;
        if (in_timeslot.getSlotDuration() == 150){ switchSlotDuration = 150;}
            else {switchSlotDuration = 180; }

        Set<Kinofilm> output;

        switch (switchInt3D){

            // 3D Film
            case 1:{
                switchInt3D
                switch (in_timeslot){
                    case SLOT_1500: {
                        output = new HashSet<>(filme3D);
                        output.retainAll(filme1500);
                        output.retainAll(filme150min);
                        break;
                    }
                    case SLOT_1730: {
                        output = new HashSet<>(filme3D);
                        output.retainAll(filme1730);
                        output.retainAll(filme150min);
                        break;
                    }
                    case SLOT_2000: {
                        output = new HashSet<>(filme3D);
                        output.retainAll(filme2000);
                        break;
                    }
                    case SLOT_2300: {
                        output = new HashSet<>(filme3D);
                        output.retainAll(filme2300);
                        output.retainAll(filme150min);
                        break;
                    }
                    default: return null;
                }
                break;
            }

            // 2D Film
            case 0:{
                switch (in_timeslot){
                    case SLOT_1500: {
                        output = new HashSet<>(filme2D);
                        output.retainAll(filme1500);
                        output.retainAll(filme150min);
                        break;
                    }
                    case SLOT_1730: {
                        output = new HashSet<>(filme2D);
                        output.retainAll(filme1730);
                        output.retainAll(filme150min);
                        break;
                    }
                    case SLOT_2000: {
                        output = new HashSet<>(filme2D);
                        output.retainAll(filme2000);
                        break;
                    }
                    case SLOT_2300: {
                        output = new HashSet<>(filme2D);
                        output.retainAll(filme2300);
                        output.retainAll(filme150min);
                        break;
                    }
                    default: return null;
                }
            }
            break;
            default: return null;
        }
        return output;
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
}
