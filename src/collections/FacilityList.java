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
import java.util.Scanner;
import utils.Inputter;

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
                if (p.length < 8) {
                    continue;
                }

                try {
                    String id = p[0].trim();
                    String name = p[1].trim();
                    String type = p[2].trim();
                    String loc = p[3].trim();
                    int cap = Integer.parseInt(p[4].trim());
                    int price = Integer.parseInt(p[5].trim());
                    LocalDateTime start = LocalDateTime.parse(p[6].trim(), TS);
                    LocalDateTime end = LocalDateTime.parse(p[7].trim(), TS);

                    String nameKey = name.toLowerCase();
                    if (cap <= 0 || nameKey.length() == 0 || nameSet.contains(nameKey)) {
                        continue;
                    }

                    facilities.add(new Facility(id, name, type, loc, cap, price, start, end));
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

// =====================================================
// Func 2 - Update Facility Information (dùng Inputter)
// =====================================================
    public static boolean update(Scanner sc) {
        String key = Inputter.getString(sc,
                "Enter facility ID or name to update: ", 1, 100);

        Facility f = findByIdOrName(key);
        if (f == null) {
            System.out.println("No Facility or Service found in database!");
            return false;
        }

        System.out.printf("Current -> loc=%s | cap=%d | start=%s | end=%s%n",
                f.getLocation(), f.getCapacity(),
                f.getStart() == null ? "-" : f.getStart().format(TS),
                f.getEnd() == null ? "-" : f.getEnd().format(TS));

        // location: nhập "-" để giữ nguyên -> method trả về null nếu giữ nguyên
        String newLoc = Inputter.getStringOrKeep(sc,
                "New location [- to keep]: ", "-");
        if (newLoc != null && !newLoc.isEmpty()) {
            f.setLocation(newLoc);
        }

        // capacity: nhập 0 để giữ nguyên -> method trả về null nếu 0
        Integer newCap = Inputter.getIntOrKeepZero(sc,
                "New capacity [0 to keep]: ", 1, 1000);
        if (newCap != null) {
            f.setCapacity(newCap);
        }

        // start/end: bỏ trống để giữ nguyên -> method trả null nếu bỏ trống
        LocalDateTime start = Inputter.getOptionalDateTime(sc,
                "New start (yyyy-MM-dd HH:mm) [blank to keep]: ", "yyyy-MM-dd HH:mm");
        LocalDateTime end = Inputter.getOptionalDateTime(sc,
                "New end   (yyyy-MM-dd HH:mm) [blank to keep]: ", "yyyy-MM-dd HH:mm");

        // validate khoảng thời gian
        if (start != null && end != null && end.isBefore(start)) {
            System.out.println("Invalid schedule range (end < start)!");
            return false;
        }
        if (start != null) {
            if (f.getEnd() != null && f.getEnd().isBefore(start)) {
                System.out.println("Invalid: start is after current end!");
                return false;
            }
            f.setStart(start);
        }
        if (end != null) {
            if (f.getStart() != null && end.isBefore(f.getStart())) {
                System.out.println("Invalid: end is before current start!");
                return false;
            }
            f.setEnd(end);
        }

        System.out.println("Facility information updated successfully!");
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
        System.out.printf("%-19s | %-15s | %-24s | %8s | %8s |%s\n",
                "Facility Name", "Type", "Location", "Capacity", "Price", "Availability");
        System.out.println("--------------------+-----------------+--------------------------+----------+----------+--------------------------------------");

        for (Facility f : list) {
            String avail = "";
            if (f.getStart() != null && f.getEnd() != null) {
                avail = f.getStart().format(TS) + " - " + f.getEnd().format(TS);
            }
            System.out.printf("%-19s | %-15s | %-24s | %8d | %8s |%s\n",
                    f.getName(), f.getType(), f.getLocation(), f.getCapacity(), f.getPricePerHour(), avail);
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

}
