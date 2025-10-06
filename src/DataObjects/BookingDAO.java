package DataObjects;

import java.io.IOException;
import java.util.*;
import model.Booking;

public class BookingDAO {

    private List<Booking> list = new ArrayList<Booking>();
    private String file;

    public BookingDAO(String file) {
        this.file = file;
    }

    public void add(Booking b) {
        if (b == null) {
            return;
        }
        list.add(b);
    }

    public List<Booking> getAll() {
        return Collections.unmodifiableList(list);
    }

    public Booking findById(String id) {
        if (id == null) {
            return null;
        }
        for (Booking b : list) {
            if (id.equals(b.getId())) {
                return b;
            }
        }
        return null;
    }

    public void save() {
        List<String> lines = new ArrayList<>(list.size());
        for (Booking b : list) {
            lines.add(b.toCSV());
        }
        try {
            FileManager.writeAllLines(file, lines);
        } catch (IOException e) {
            System.err.println("Save fail: " + e.getMessage());
        }
    }

    public void load() {
        list.clear();
        try {
            List<String> lines = FileManager.readAllLines(file);
            for (String s : lines) {
                if (s == null || s.trim().isEmpty()) {
                    continue;
                }
                Booking b = Booking.fromCSV(s); 
                if (b != null) {
                    list.add(b);
                }
            }
        } catch (IOException e) {
        }
    }
}
