import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

public class CutoffBenchmark {

  // Đọc nhanh dữ liệu từ file (nhanh hơn Scanner rất nhiều với 10^8 phần tử)
  static class FastReader {
    BufferedReader br;
    StringTokenizer st;

    public FastReader(String filename) throws FileNotFoundException {
      br = new BufferedReader(new FileReader(filename), 1024 * 1024); // Buffer 1MB
    }

    String next() throws IOException {
      while (st == null || !st.hasMoreElements()) {
        String line = br.readLine();
        if (line == null)
          return null;
        st = new StringTokenizer(line);
      }
      return st.nextToken();
    }

    int nextInt() throws IOException {
      return Integer.parseInt(next());
    }

    void close() throws IOException {
      br.close();
    }
  }

  // Đọc toàn bộ mảng từ file vào bộ nhớ một lần
  private static Integer[] loadDataset(String filename) throws IOException {
    FastReader reader = new FastReader(filename);
    int n = reader.nextInt();
    Integer[] arr = new Integer[n];
    for (int i = 0; i < n; i++) {
      arr[i] = reader.nextInt();
    }
    reader.close();
    return arr;
  }

  public static void main(String[] args) {
    int[] sizes = { 1_000_000, 10_000_000, 100_000_000 };
    int datasetsPerSize = 10;
    // Các giá trị cutoff cần khảo sát (cutoff = 0 tương đương MergeSort thuần túy)
    int[] cutoffs = { 0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
    String outputCsv = "benchmark_results.csv";

    // 1. JVM Warm-up (Làm nóng JIT Compiler để tránh sai số ở các lần chạy đầu)
    System.out.println("Đang chạy JVM Warm-up...");
    Random rand = new Random(42);
    for (int w = 0; w < 5; w++) {
      Integer[] warmupArr = new Integer[200_000];
      for (int i = 0; i < warmupArr.length; i++)
        warmupArr[i] = rand.nextInt();
      MergeSort.sort(warmupArr, 16);
    }
    System.out.println("Warm-up hoàn tất!\n");

    // 2. Chạy Benchmark và ghi kết quả ra CSV
    try (PrintWriter writer = new PrintWriter(new FileWriter(outputCsv))) {
      // Ghi dòng tiêu đề CSV
      writer.println("N,Dataset,Cutoff,Time_ms");

      for (int size : sizes) {
        for (int set = 1; set <= datasetsPerSize; set++) {
          String filename = String.format("data/input_N%d_set%d.txt", size, set);
          File file = new File(filename);
          if (!file.exists()) {
            System.out.println("Bỏ qua file không tồn tại: " + filename);
            continue;
          }

          System.out.printf("Đang đọc dữ liệu: %s ...\n", filename);
          Integer[] originalData = loadDataset(filename);

          for (int cutoff : cutoffs) {
            // Clone mảng gốc để mỗi cutoff đều chạy trên cùng 1 dữ liệu chưa sắp xếp
            Integer[] workingCopy = Arrays.copyOf(originalData, originalData.length);

            // Dọn rác nhẹ trước khi đo để giảm thiểu GC xen ngang
            System.gc();

            long startTime = System.nanoTime();
            MergeSort.sort(workingCopy, cutoff);
            long endTime = System.nanoTime();

            double elapsedMs = (endTime - startTime) / 1_000_000.0;

            // Ghi trực tiếp vào CSV và flush ngay để tránh mất dữ liệu nếu dừng giữa chừng
            writer.printf(Locale.US, "%d,%d,%d,%.3f\n", size, set, cutoff, elapsedMs);
            writer.flush();

            System.out.printf("  [N=%,d | Set=%d | Cutoff=%3d] -> %.3f ms\n",
                size, set, cutoff, elapsedMs);
          }

          // Giải phóng bộ nhớ trước khi đọc file tiếp theo
          originalData = null;
          System.gc();
        }
      }
      System.out.println("\nHoàn thành! Kết quả đã được lưu tại: " + outputCsv);

    } catch (IOException e) {
      System.err.println("Lỗi I/O: " + e.getMessage());
      e.printStackTrace();
    }
  }
}