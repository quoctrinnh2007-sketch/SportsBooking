package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Booking implements Serializable {

    private String id;
    private String player;
    private String facility;
    private LocalDate date;
    private LocalTime time;
    private int duration;
    private boolean canceled;

    public Booking(String id, String player, String facility,
            LocalDate date, LocalTime time, int duration) {
        this.id = id;
        this.player = player;
        this.facility = facility;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.canceled = false;
    }

    public String getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public String getFacility() {
        return facility;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void cancel() {
        this.canceled = true;
    }

    public String toCSV() {
        return id + "," + player + "," + facility + ","
                + date + "," + time + "," + duration + "," + canceled;
    }

    public static Booking fromCSV(String line) {
        try {
            if (line == null) {
                return null;
            }
            String[] p = line.split(",");
            if (p.length < 7) {
                return null;
            }

            Booking b = new Booking(p[0].trim(), p[1].trim(), p[2].trim(),
                    java.time.LocalDate.parse(p[3].trim()),
                    java.time.LocalTime.parse(p[4].trim()),
                    Integer.parseInt(p[5].trim()));
            if (Boolean.parseBoolean(p[6].trim())) {
                b.cancel();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }
}
