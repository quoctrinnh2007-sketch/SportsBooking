package utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {

    private static final LinkedHashMap<Integer, String> items = new LinkedHashMap<>();

    static {
        items.put(1, "Import Facilities / Services");
        items.put(2, "Update Facility Information");
        items.put(3, "Show All Facilities");
        items.put(4, "Book a Facility or Service");
        items.put(5, "View Bookings by Date");
        items.put(6, "Cancel a Booking");
        items.put(7, "Monthly Revenue Report");
        items.put(8, "Service Usage Statistics");
        items.put(9, "Save All Data");
        items.put(10, "Quit");
        items.put(0, "Exit Program (Force)");
    }

    public static int showMainMenu() {
        System.out.println("\n=========== SPORTS BOOKING SYSTEM ===========");
        for (Map.Entry<Integer, String> e : items.entrySet()) {
            System.out.printf("%2d. %s\n", e.getKey(), e.getValue());
        }
        System.out.println("============================================");

        System.out.print("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid input! Please enter a number 0–10.");
            return -1;
        }
    }
}
