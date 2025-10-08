package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Facility implements Serializable {

    private String id;
    private String name;
    private String type;
    private String location;
    private int capacity;
    private int pricePerHour;
    private LocalDateTime start;
    private LocalDateTime end;

    public Facility(String id, String name, String type, String location,
            int capacity, int pricePerHour, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.pricePerHour = Math.max(0, pricePerHour);
        this.start = start;
        this.end = end;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = Math.max(0, pricePerHour);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setLocation(String loc) {
        this.location = loc;
    }

    public void setCapacity(int c) {
        this.capacity = c;
    }

    public void setStart(LocalDateTime s) {
        this.start = s;
    }

    public void setEnd(LocalDateTime e) {
        this.end = e;
    }
}
