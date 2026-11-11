public class MergeSortTest {

  public static void main(String[] args) {
    MergeSortTest tests = new MergeSortTest();
    tests.testMergeSortInteger();
    tests.testMergeSortString();
    tests.testEmptyArray();
    tests.testSingleElementArray();
    tests.testAlreadySortedArray();
    tests.testReverseSortedArray();
    tests.testDuplicateElements();
  }

  public void testMergeSortInteger() {
    Integer[] input = { 38, 27, 43, 3, 9, 82, 10 };
    Integer[] expected = { 3, 9, 10, 27, 38, 43, 82 };

    MergeSort.sort(input, 0);

    assertArrayEquals(expected, input, "Mảng số nguyên chưa được sắp xếp đúng!");
  }

  public void testMergeSortString() {
    String[] input = { "Banana", "Apple", "Orange", "Mango", "Grapes" };
    String[] expected = { "Apple", "Banana", "Grapes", "Mango", "Orange" };

    MergeSort.sort(input, 0);

    assertArrayEquals(expected, input, "Mảng chuỗi chưa được sắp xếp đúng!");
  }

  public void testEmptyArray() {
    Integer[] input = {};
    Integer[] expected = {};

    MergeSort.sort(input, 0);

    assertArrayEquals(expected, input, "Mảng rỗng phải giữ nguyên!");
  }

  public void testSingleElementArray() {
    Integer[] input = { 42 };
    Integer[] expected = { 42 };

    MergeSort.sort(input, 0);

    assertArrayEquals(expected, input, "Mảng 1 phần tử phải giữ nguyên!");
  }

  public void testAlreadySortedArray() {
    Integer[] input = { 1, 2, 3, 4, 5 };
    Integer[] expected = { 1, 2, 3, 4, 5 };

    MergeSort.sort(input, 0);

    assertArrayEquals(expected, input, "Mảng đã sắp xếp phải giữ nguyên!");
  }

  public void testReverseSortedArray() {
    Integer[] input = { 5, 4, 3, 2, 1 };
    Integer[] expected = { 1, 2, 3, 4, 5 };

    MergeSort.sort(input, 0);

    assertArrayEquals(expected, input, "Mảng ngược chưa được sắp xếp đúng!");
  }

  public void testDuplicateElements() {
    Integer[] input = { 4, 2, 4, 1, 2, 3 };
    Integer[] expected = { 1, 2, 2, 3, 4, 4 };

    MergeSort.sort(input, 0);

    assertArrayEquals(expected, input, "Mảng có phần tử trùng chưa được sắp xếp đúng!");
  }

  private static void assertArrayEquals(Object[] expected, Object[] actual, String message) {
    if (!java.util.Arrays.equals(expected, actual)) {
      throw new AssertionError(message);
    }
  }
}