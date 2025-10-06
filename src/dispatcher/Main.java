package dispatcher;

import collections.*;
import DataObjects.*;
import utils.Menu;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Load dữ liệu ban đầu
        FacilityList.importFile("test/facility_schedule.csv");
        BookingDAO bookingDAO = new BookingDAO("test/BookingInfo.txt");
        bookingDAO.load();
        BookingList bookingList = new BookingList(bookingDAO);

        boolean running = true;

        // ========================== MENU LOOP ==========================
        do {
            int choice = Menu.showMainMenu();
            switch (choice) {
                case 1:
                    System.out.print("Enter facility file path: ");
                    String path = sc.nextLine().trim();
                    int count = FacilityList.importFile(path);
                    System.out.println("Imported " + count + " facilities successfully.");
                    break;

                case 2:
                    System.out.print("Enter facility ID or name to update: ");
                    String key = sc.nextLine().trim();
                    System.out.print("Enter new location (- to skip): ");
                    String newLoc = sc.nextLine().trim();
                    System.out.print("Enter new capacity (0 to skip): ");
                    int cap = Integer.parseInt(sc.nextLine());
                    boolean ok = FacilityList.update(key, newLoc, cap, null, null);
                    System.out.println(ok ? "Updated successfully!" : "Facility not found.");
                    break;

                case 3:
                    FacilityList.printAll();
                    break;

                case 4:
                    System.out.print("Enter player name: ");
                    String player = sc.nextLine().trim();
                    System.out.print("Enter facility name: ");
                    String fname = sc.nextLine().trim();
                    System.out.print("Enter booking date (yyyy-MM-dd): ");
                    LocalDate d = LocalDate.parse(sc.nextLine().trim());
                    System.out.print("Enter start time (HH:mm): ");
                    java.time.LocalTime t = java.time.LocalTime.parse(sc.nextLine().trim());
                    System.out.print("Enter number of hours: ");
                    int h = Integer.parseInt(sc.nextLine().trim());
                    String result = bookingList.book(player, fname, d, t, h);
                    System.out.println(result);
                    break;

                case 5:
                    System.out.print("Enter date (yyyy-MM-dd) or leave blank for today: ");
                    String input = sc.nextLine().trim();
                    LocalDate viewDate = input.isEmpty() ? null : LocalDate.parse(input);
                    bookingList.view(viewDate);
                    break;

                case 6:
                    bookingList.cancel();
                    break;

                case 7:
                    System.out.print("Enter month (1-12): ");
                    int month = Integer.parseInt(sc.nextLine());
                    System.out.print("Enter year (e.g. 2025): ");
                    int year = Integer.parseInt(sc.nextLine());
                    bookingList.monthlyRevenue(month, year);
                    break;

                case 8:
                    System.out.print("Enter FROM date (yyyy-MM-dd) or leave blank: ");
                    String fromStr = sc.nextLine().trim();
                    LocalDate from = fromStr.isEmpty() ? null : LocalDate.parse(fromStr);
                    System.out.print("Enter TO date (yyyy-MM-dd) or leave blank: ");
                    String toStr = sc.nextLine().trim();
                    LocalDate to = toStr.isEmpty() ? null : LocalDate.parse(toStr);
                    bookingList.usageStats(from, to);
                    break;

                case 9:
                    bookingList.saveAllDataToTxt();
                    break;

                case 10:
                    bookingList.exitProgram();
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                case 0:
                    System.out.println("Force exit program!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice! Please enter 0–10.");
                    break;
            }

        } while (running);

        sc.close();
    }
}
