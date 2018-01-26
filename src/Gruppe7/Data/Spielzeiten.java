package Gruppe7.Data;

public enum Spielzeiten {

    SLOT_1500(150),
    SLOT_1730(150),
    SLOT_2000(180),
    SLOT_2300(150);

    private int slotDuration;
    Spielzeiten(int slotDuration){
        this.slotDuration = slotDuration;
    }

    public int getSlotDuration(){
        return this.slotDuration;
    }

    @Override
    public String toString() {
        switch(this){
            case SLOT_1500: return "15:00 Uhr";
            case SLOT_1730: return "17:30 Uhr";
            case SLOT_2000: return "20:00 Uhr";
            case SLOT_2300: return "23:00 Uhr";
            default: return "--:-- Uhr";
        }
    }
}