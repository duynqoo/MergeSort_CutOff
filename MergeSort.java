public class MergeSort {
  private static void InsertionSort(Comparable[] a, int lo, int hi) {
    for (int i = lo + 1; i <= hi; i++) {
      Comparable key = a[i];
      int j = i - 1;
      while (j >= lo && a[j].compareTo(key) > 0) {
        a[j + 1] = a[j];
        j--;
      }
      a[j + 1] = key;
    }
  }

  private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi, int cutoff) {
    if (lo >= hi)
      return;

    if (hi - lo + 1 <= cutoff) {
      InsertionSort(a, lo, hi);
      return;
    }

    int mid = lo + (hi - lo) / 2;
    sort(a, aux, lo, mid, cutoff);
    sort(a, aux, mid + 1, hi, cutoff);
    merge(a, aux, lo, mid, hi);
  }

  private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
    for (int i = lo; i <= hi; ++i) {
      aux[i] = a[i];
    }
    int i = lo, j = mid + 1;
    for (int k = lo; k <= hi; k++) {
      if (i > mid)
        a[k] = aux[j++];
      else if (j > hi)
        a[k] = aux[i++];
      else if (aux[i].compareTo(aux[j]) <= 0)
        a[k] = aux[i++];
      else
        a[k] = aux[j++];
    }
  }

  public static void sort(Comparable[] a, int cutoff) {
    if (a == null || a.length <= 1) {
      return;
    }
    Comparable[] aux = new Comparable[a.length];
    sort(a, aux, 0, a.length - 1, cutoff);
  }
}