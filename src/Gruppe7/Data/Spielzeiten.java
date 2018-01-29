package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 *
 * Enum zur Verwaltung von Spielzeiten und ihren Längen.
 */
public enum Spielzeiten {

    SLOT_1500(150),
    SLOT_1730(150),
    SLOT_2000(180),
    SLOT_2300(150);

    private int slotDuration;

    /**
     * @param slotDuration Die Spieldauer eines Timeslots in Minuten
     */
    Spielzeiten(int slotDuration) {
        this.slotDuration = slotDuration;
    }

    /**
     * Getter für die Spieldauer eines Timeslots
     *
     * @return Die Spielzeit in Minuten
     */
    public int GetSlotDuration() {
        return this.slotDuration;
    }

    /**
     * ToString Override, um die Uhrzeit eines Timeslots lesbar darzustellen.
     *
     * @return Die Uhrzeit eines Timeslots im Format "XX:XX Uhr"
     */
    public String ToString() {
        switch (this) {
            case SLOT_1500:
                return "15:00 Uhr";
            case SLOT_1730:
                return "17:30 Uhr";
            case SLOT_2000:
                return "20:00 Uhr";
            case SLOT_2300:
                return "23:00 Uhr";
            default:
                return "--:-- Uhr";
        }
    }
}