/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collections;

import DataObjects.FileManager;
import model.Facility;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phuquoc
 */
public class FacilityList {

    public static ArrayList<Facility> facilities = new ArrayList<Facility>();
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final java.time.format.DateTimeFormatter D = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final java.time.format.DateTimeFormatter T = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

    // Func 1 :
    public static int importFile(String path) {
        facilities.clear();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("File not found: " + path);
            return 0;
        }

        int count = 0;
        try {
            List<String> lines = FileManager.readAllLines(path);
            java.util.HashSet<String> nameSet = new java.util.HashSet<String>();

            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                String lower = line.toLowerCase();
                if (lower.startsWith("id,") || lower.contains("facility name")) {
                    continue;
                }

                String[] p = line.split(",");
                if (p.length < 7) {
                    continue;
                }

                try {
                    String id = p[0].trim();
                    String name = p[1].trim();
                    String type = p[2].trim();
                    String loc = p[3].trim();
                    int cap = Integer.parseInt(p[4].trim());
                    LocalDateTime start = LocalDateTime.parse(p[5].trim(), TS);
                    LocalDateTime end = LocalDateTime.parse(p[6].trim(), TS);

                    String nameKey = name.toLowerCase();
                    if (cap <= 0 || nameKey.length() == 0 || nameSet.contains(nameKey)) {
                        continue;
                    }

                    facilities.add(new Facility(id, name, type, loc, cap, start, end));
                    nameSet.add(nameKey);
                    count++;
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to import: " + e.getMessage());
        }
        return count;
    }

    //Func 2:
    public static boolean update(String key, String newLoc, int newCap,
            LocalDateTime start, LocalDateTime end) {
        Facility f = findByIdOrName(key);
        if (f == null) {
            return false;
        }

        if (newLoc != null && !newLoc.trim().equals("-")) {
            f.setLocation(newLoc.trim());
        }
        if (newCap > 0) {
            f.setCapacity(newCap);
        }

        if (start != null && end != null) {
            if (!end.isBefore(start)) { // end >= start
                f.setStart(start);
                f.setEnd(end);
            }
        } else {
            if (start != null) {
                f.setStart(start);
            }
            if (end != null && (f.getStart() == null || !end.isBefore(f.getStart()))) {
                f.setEnd(end);
            }
        }
        return true;
    }

    //Func 3 :
    public static void printAll() {
        if (facilities == null || facilities.isEmpty()) {
            System.out.println("Facilities & Services list is currently empty, not loaded yet.");
            return;
        }
        showAll(facilities);
    }

    // Sub-function: Show all facilities
    private static void showAll(List<Facility> list) {
        System.out.println("Facilities & Services List");
        System.out.printf("%-19s | %-12s | %-19s | %8s | %s\n",
                "Facility Name", "Type", "Location", "Capacity", "Availability");
        System.out.println("-------------------+--------------+-------------------+----------+--------------------------");

        for (Facility f : list) {
            String avail = "";
            if (f.getStart() != null && f.getEnd() != null) {
                avail = f.getStart().format(TS) + " - " + f.getEnd().format(TS);
            }
            System.out.printf("%-19s | %-12s | %-19s | %8d | %s\n",
                    f.getName(), f.getType(), f.getLocation(), f.getCapacity(), avail);
        }
    }

    public static Facility findByIdOrName(String key) {
        if (key == null) {
            return null;
        }
        String k = key.trim();
        for (Facility f : facilities) {
            if (f.getId().equalsIgnoreCase(k) || f.getName().equalsIgnoreCase(k)) {
                return f;
            }

        }
        return null;
    }

//    private static boolean containsName(String nameLower) {
//        for (Facility f : facilities) {
//            if (f.getName() != null && f.getName().trim().toLowerCase().equals(nameLower)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
