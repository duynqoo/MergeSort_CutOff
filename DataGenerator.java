import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class DataGenerator {
  public static void main(String[] args) {
    int[] sizes = { 1_000_000, 10_000_000, 100_000_000 };
    int datasetsPerSize = 10;
    Random random = new Random(42); // Seed cố định để kết quả có thể tái lập

    Path dataDir = Paths.get("data");
    try {
      Files.createDirectories(dataDir);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    for (int size : sizes) {
      System.out.printf("Đang sinh dữ liệu kích thước N = %,d...\n", size);
      for (int i = 1; i <= datasetsPerSize; i++) {
        String filename = String.format("data/input_N%d_set%d.txt", size, i);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
          writer.write(size + "\n");
          for (int j = 0; j < size; j++) {
            writer.write(random.nextInt() + " ");
            if ((j + 1) % 10 == 0) {
              writer.newLine(); // Đảm bảo ghi buffer hiệu quả
            }
          }
          System.out.printf("  -> Đã tạo: %s\n", filename);
        } catch (IOException e) {
          System.err.println("Lỗi khi ghi file " + filename + ": " + e.getMessage());
        }
      }
    }
    System.out.println("Đã sinh xong toàn bộ 30 bộ dữ liệu!");
  }
}