package DataObjects;

/**
 *
 * @author phuquoc
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc/ghi file text dạng CSV.
 */
public class FileManager {

    public static List<String> readAllLines(String path) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return lines;
    }

    public static void writeAllLines(String path, List<String> lines) throws IOException {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(path));
            for (String s : lines) {
                pw.println(s);
            }
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
