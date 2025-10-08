package DataObjects;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.Facility;

public class FacilityDAO {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static List<Facility> importFromCSV(String path) throws IOException {
        List<Facility> list = new ArrayList<>();
        List<String> lines = FileManager.readAllLines(path);
        for (int i = 1; i < lines.size(); i++) { // bỏ header
            String[] p = lines.get(i).split(",");
            if (p.length < 8) {
                continue;
            }
            try {
                Facility f = new Facility(p[0], p[1], p[2], p[3],
                        Integer.parseInt(p[4]),
                        Integer.parseInt(p[5].trim()),
                        LocalDateTime.parse(p[6], FMT),
                        LocalDateTime.parse(p[7], FMT));
                list.add(f);
            } catch (Exception ignored) {
            }
        }
        return list;
    }
}
