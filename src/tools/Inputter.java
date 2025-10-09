package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Inputter {

    private Inputter() {}

    // ===== String =====
    public static String getString(java.util.Scanner sc, String prompt, int minLen, int maxLen) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.length() >= minLen && s.length() <= maxLen) return s;
            System.out.println("Invalid input! Length must be " + minLen + "–" + maxLen + ".");
        }
    }

    /** Trả về null nếu người dùng nhập đúng keepToken (ví dụ "-"). */
    public static String getStringOrKeep(java.util.Scanner sc, String prompt, String keepToken) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.equals(keepToken)) return null;           // giữ nguyên
            return s;
        }
    }

    // ===== Int =====
    /** Nhập số nguyên trong [min, max]. Bắt nhập lại tới khi hợp lệ. */
    public static int getInt(java.util.Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n >= min && n <= max) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid input! Enter an integer from " + min + " to " + max + ".");
        }
    }

    /** Trả về null nếu nhập 0 (giữ nguyên). Kiểm tra hợp lệ khi >0. */
    public static Integer getIntOrKeepZero(java.util.Scanner sc, String prompt, int minPositive, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n == 0) return null;                    // giữ nguyên
                if (n >= minPositive && n <= max) return n; // cập nhật
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid input! Enter 0 to keep or an integer " +
                               minPositive + "–" + max + " to update.");
        }
    }

    // ===== Date/Time =====
    /** Nhập LocalDate theo pattern, VD: "yyyy-MM-dd". */
    public static LocalDate getDate(java.util.Scanner sc, String prompt, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        while (true) {
            System.out.print(prompt);
            try { return LocalDate.parse(sc.nextLine().trim(), fmt); }
            catch (Exception e) { System.out.println("Invalid date format! Expected " + pattern + "."); }
        }
    }

    /** Nhập LocalTime theo pattern, VD: "HH:mm". */
    public static LocalTime getTime(java.util.Scanner sc, String prompt, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        while (true) {
            System.out.print(prompt);
            try { return LocalTime.parse(sc.nextLine().trim(), fmt); }
            catch (Exception e) { System.out.println("Invalid time format! Expected " + pattern + "."); }
        }
    }

    /** Nhập LocalDateTime, cho phép bỏ trống để giữ nguyên (trả null). */
    public static LocalDateTime getOptionalDateTime(java.util.Scanner sc, String prompt, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return null;                   // giữ nguyên
            try { return LocalDateTime.parse(s, fmt); }
            catch (Exception e) { System.out.println("Invalid datetime format! Expected " + pattern + "."); }
        }
    }

    /** Nhập LocalDateTime bắt buộc. */
    public static LocalDateTime getDateTime(java.util.Scanner sc, String prompt, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        while (true) {
            System.out.print(prompt);
            try { return LocalDateTime.parse(sc.nextLine().trim(), fmt); }
            catch (Exception e) { System.out.println("Invalid datetime format! Expected " + pattern + "."); }
        }
    }
}
