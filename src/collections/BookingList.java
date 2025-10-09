package collections;

import DataObjects.BookingDAO;
import DataObjects.FacilityDAO;
import model.Booking;
import model.Facility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class BookingList {

    private BookingDAO bookingDAO;
    private FacilityDAO facilityDAO;

    public BookingList(BookingDAO b) {
        this.bookingDAO = b;
    }

    public BookingList(BookingDAO b, FacilityDAO f) {
        this.bookingDAO = b;
        this.facilityDAO = f;
    }

    // -------------------- Helpers --------------------
    private static boolean overlaps(LocalDateTime aStart, LocalDateTime aEnd,
            LocalDateTime bStart, LocalDateTime bEnd) {
        // true nếu hai khoảng thời gian giao nhau
        return !(aEnd.isEqual(bStart) || aEnd.isBefore(bStart) || aStart.isEqual(bEnd) || aStart.isAfter(bEnd));
    }

    private static String newId() {
        return "BK" + System.currentTimeMillis();
    }

    private static String fmtTime(LocalTime t) {
        String s = t.toString();
        return s.length() >= 5 ? s.substring(0, 5) : s;
    }

// =====================================================
// Func 4 - Book a Facility / Service
// =====================================================
    public void booking(Scanner sc) {
        String player;
        while (true) {
            System.out.print("Enter player name (2-18 chars): ");
            player = sc.nextLine().trim();
            if (player.length() >= 2 && player.length() <= 18) {
                break;
            }
            System.out.println("Invalid name! Must be 2–18 characters. Please try again.");
        }

        Facility f;
        while (true) {
            System.out.print("Enter facility name: ");
            String fname = sc.nextLine().trim();
            f = FacilityList.findByIdOrName(fname);
            if (f != null) {
                break;
            }
            System.out.println("Facility not found. Please enter again.");
        }

        LocalDate date = null;
        while (true) {
            System.out.print("Enter booking date (yyyy-MM-dd): ");
            try {
                date = LocalDate.parse(sc.nextLine().trim());
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        LocalTime time = null;
        while (true) {
            System.out.print("Enter start time (HH:mm): ");
            try {
                time = LocalTime.parse(sc.nextLine().trim());
                break;
            } catch (Exception e) {
                System.out.println("Invalid time format. Please use HH:mm.");
            }
        }

        int hours = 0;
        while (true) {
            System.out.print("Enter number of hours (1–5): ");
            try {
                hours = Integer.parseInt(sc.nextLine().trim());
                if (hours >= 1 && hours <= 5) {
                    break;
                }
                System.out.println("Duration must be between 1 and 5 hours.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format! Please enter an integer 1–5.");
            }
        }

        // Kiểm tra logic đặt sân
        LocalDateTime reqStart = LocalDateTime.of(date, time);
        LocalDateTime reqEnd = reqStart.plusHours(hours);
        LocalDateTime now = LocalDateTime.now();

        if (reqStart.isBefore(now)) {
            System.out.println("Cannot book a past time.");
            return;
        }
        if (f.getStart() == null || f.getEnd() == null
                || reqStart.isBefore(f.getStart()) || reqEnd.isAfter(f.getEnd())) {
            System.out.println("Selected time is not within facility availability.");
            return;
        }

        // Chống trùng lịch
        for (Booking b : bookingDAO.getAll()) {
            if (b.isCanceled()) {
                continue;
            }
            if (!b.getFacility().equalsIgnoreCase(f.getName())) {
                continue;
            }
            LocalDateTime s = LocalDateTime.of(b.getDate(), b.getTime());
            LocalDateTime e = s.plusHours(b.getDuration());
            if (reqStart.isBefore(e) && reqEnd.isAfter(s)) {
                System.out.println("Time slot overlaps with an existing booking.");
                return;
            }
        }

        String id = "BK" + System.currentTimeMillis();
        bookingDAO.add(new Booking(id, player, f.getName(), date, time, hours));
        bookingDAO.save();

        System.out.println("Booking created successfully.");
        System.out.println("Your Booking ID: " + id);
    }

    // =====================================================
    // Func 5 - View Today's Bookings
    // ➜ Hiển thị booking cho ngày chỉ định; nếu null thì mặc định today. Sort theo time, format như sample.
    // =====================================================
    public void view(LocalDate dateOrNull) {
        LocalDate target = (dateOrNull == null) ? LocalDate.now() : dateOrNull;

        List<Booking> list = new ArrayList<Booking>();
        List<Booking> all = bookingDAO.getAll();

        for (Booking b : all) {
            if (!b.isCanceled() && b.getDate().equals(target)) {
                list.add(b);
            }
        }

        if (list.isEmpty()) {
            System.out.println("There are currently no courts or services booked !.");
            return;
        }

        // sort theo giờ
        Collections.sort(list, new Comparator<Booking>() {
            @Override
            public int compare(Booking a, Booking b) {
                return a.getTime().compareTo(b.getTime());
            }
        });

        showDetails(target, list);
    }

    // Sub-function: Display bookings table
    // ➜ Hàm hiển thị danh sách booking (dùng trong Func 5)
    private void showDetails(LocalDate target, List<Booking> list) {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Bookings on " + target);
        System.out.println("----------------------------------------------------------------------");
        System.out.println(String.format(" %-5s| %-25s | %-18s | %-9s",
                "Time", "Facility", "User", "Duration"));
        System.out.println("----------------------------------------------------------------------");
        for (Booking b : list) {
            String time = fmtTime(b.getTime()); // dùng lại hàm fmtTime()
            System.out.println(String.format(" %-5s| %-25s | %-18s | %9d ",
                    time, b.getFacility(), b.getPlayer(), b.getDuration()));
        }
        System.out.println("----------------------------------------------------------------------");
    }

    // Sub-function: Display booking information (used in Func 6)
    private void showBookingInfo(Booking b) {
        System.out.println("Booking information:");
        System.out.println("+ Booking ID   : " + b.getId());
        System.out.println("+ Player name  : " + b.getPlayer());
        System.out.println("+ Facility name: " + b.getFacility());
        System.out.println("+ Date         : " + b.getDate());
        System.out.println("+ Time         : " + fmtTime(b.getTime())); // dùng lại fmtTime() để in HH:mm
        System.out.println("+ Duration     : " + b.getDuration());
        System.out.println("------------------------------------------------------------");
    }

// =====================================================
// Func 6 - Cancel a Booking
// ➜ Cho phép hủy theo ID hoặc theo (Player + Date + Facility)
// =====================================================
    public void cancel() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Booking ID (press Enter to use Player + Date + Facility): ");
        String idInput = sc.nextLine().trim();

        Booking b = null;

        if (!idInput.isEmpty()) {
            //tim theo ID
            b = bookingDAO.findById(idInput);
            if (b == null) {
                System.out.println("Booking not found.");
                return;
            }
        } else {
            System.out.print("Enter Player name: ");
            String player = sc.nextLine().trim();
            System.out.print("Enter Date (yyyy-MM-dd): ");
            LocalDate date = null;
            try {
                date = LocalDate.parse(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid date format.");
                return;
            }
            System.out.print("Enter Facility name: ");
            String facilityName = sc.nextLine().trim();

            b = findByInfo(player, date, facilityName);
            if (b == null) {
                System.out.println("Booking not found.");
                return;
            }
            idInput = b.getId();
        }

        showBookingInfo(b);

        //check time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(b.getDate(), b.getTime());
        if (start.isBefore(now)) {
            System.out.println("This booking (ID: " + idInput + ") cannot be canceled");
            return;
        }

        // xác nhận hủy
        System.out.print("Do you really want to cancel this court booking? [Y/N]: ");
        String ans = sc.nextLine().trim();
        if (!ans.equalsIgnoreCase("Y")) {
            System.out.println("Cancellation aborted.");
            return;
        }

        // hủy
        b.cancel();
        bookingDAO.save();
        System.out.println("... System message ...");
        System.out.println("The booking ID " + idInput + " has been successfully canceled.");
    }

    // hàm phụ nếu ng dùng kh có ID
    public Booking findByInfo(String player, LocalDate date, String facilityName) {
        if (player == null || date == null || facilityName == null) {
            return null;
        }
        List<Booking> all = bookingDAO.getAll();
        for (int i = 0; i < all.size(); i++) {
            Booking b = all.get(i);
            if (b.getPlayer().equalsIgnoreCase(player.trim())
                    && b.getDate().equals(date)
                    && b.getFacility().equalsIgnoreCase(facilityName.trim())) {
                return b;
            }
        }
        return null;
    }

// =====================================================
// Func 7 - Monthly Revenue Report (group by Facility TYPE, dùng Price từ CSV)
// =====================================================
    public void monthlyRevenue(int month, int year) {
        Map<String, Integer> sumByType = new LinkedHashMap<>();
        List<Booking> all = bookingDAO.getAll();

        for (Booking b : all) {
            if (b.isCanceled()) {
                continue;
            }
            if (b.getDate().getMonthValue() != month || b.getDate().getYear() != year) {
                continue;
            }

            Facility f = FacilityList.findByIdOrName(b.getFacility());
            String type = (f == null) ? "Unknown" : f.getType();
            int rate = (f == null) ? 0 : f.getPricePerHour();   // lấy giá/giờ từ CSV
            int money = rate * b.getDuration();

            sumByType.put(type, sumByType.getOrDefault(type, 0) + money);
        }

        printMonthlyRevenue(month, year, sumByType);
    }

    private void printMonthlyRevenue(int month, int year, Map<String, Integer> data) {
        if (data == null || data.isEmpty()) {
            System.out.println("No data available in the Monthly Revenue Report");
            return;
        }
        System.out.println("Monthly Revenue Report - '" + String.format("%02d/%d", month, year) + "'");
        System.out.println("-------------------------------------");
        System.out.printf("%-4s | %-15s | %s%n", "No.", "Facility", "Amount");
        System.out.println("-------------------------------------");

        int no = 1, total = 0;
        for (Map.Entry<String, Integer> e : data.entrySet()) {
            System.out.printf("%-4d | %-15s | %10d%n", no++, e.getKey(), e.getValue());
            total += e.getValue();
        }
        System.out.println("-------------------------------------");
        System.out.printf("%-22s %12d%n", "Total", total);
        System.out.println("-------------------------------------");
    }

// =====================================================
// Func 8 - Service Usage Statistics
// ➜ Đếm số người chơi duy nhất theo LOẠI facility, trong [from, to] (có thể null).
// =====================================================
    public void usageStats(LocalDate from, LocalDate to) {
        Map<String, Set<String>> typeToPlayers = new LinkedHashMap<String, Set<String>>();
        List<Booking> all = bookingDAO.getAll();

        for (Booking b : all) {
            if (b.isCanceled()) {
                continue;
            }
            if (from != null && b.getDate().isBefore(from)) {
                continue;
            }
            if (to != null && b.getDate().isAfter(to)) {
                continue;
            }

            Facility f = FacilityList.findByIdOrName(b.getFacility());
            String type = (f == null) ? "Unknown" : f.getType();

            Set<String> set = typeToPlayers.get(type);
            if (set == null) {
                set = new HashSet<String>();
                typeToPlayers.put(type, set);
            }
            set.add(b.getPlayer());
        }
        printUsageStats(from, to, typeToPlayers);
    }

    // Sub-function: Print Usage Statistics Table
    private void printUsageStats(LocalDate from, LocalDate to, Map<String, Set<String>> data) {
        if (data == null || data.isEmpty()) {
            System.out.println("No usage data found.");
            return;
        }

        System.out.println("Service Usage Statistics");
        System.out.println("-------------------------------------");
        System.out.printf("%-15s | %s\n", "Facility type", "No. of Players");
        System.out.println("-------------------------------------");

        for (Map.Entry<String, Set<String>> e : data.entrySet()) {
            System.out.printf("%-15s | %14d\n", e.getKey(), e.getValue().size());
        }

        System.out.println("-------------------------------------");
    }

    // Func 9 - Save All Data (TEXT)
    public void saveAllDataToTxt() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to save Registration? (Y/N)");
        String message = sc.nextLine();
        if (message.trim().equalsIgnoreCase("Y")) {
            bookingDAO.save();
            System.out.println("Booking court has been successfully saved to \"BookingInfo.txt\" file.");
        } else {
            System.out.println("Saving process is cancelled.");
        }

    }

    //Func 10 - exit
    public void exitProgram() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Do you want to save before exiting? (Y/N): ");
        String ans = sc.nextLine().trim();

        if (ans.equalsIgnoreCase("Y")) {
            bookingDAO.save();
            System.out.println("Booking court has been successfully saved to \"BookingInfo.txt\" file.");
        } else {
            System.out.println("Exit program without saving successfully!");
        }
        System.out.println("Goodbye!");
    }

}
