public class SLAlgo {
  private static int M;
  public static Null data;
  public static double[] mValue;

  // site, then day
  public static int[][] beginMinute;
  public static int[][] endMinute;


  public static void main(String[] args) {
    Null data = new Null("input.txt");
    SLAlgo.init(data);
    if (mValue[0] > Integer.MAX_VALUE) {
      System.out.println("Maximum value exceeds INT_MAX");
      return;
    }
    int maxValue = (int) mValue[0];
    OneDayAStar aStar = new OneDayAStar(maxValue + 2);
    long start = System.currentTimeMillis();
    String result = aStar.startSearch(10 * 1000);

    System.out.println("time:" + (System.currentTimeMillis() - start));
    System.out.println(result);
  }


  public static void init(Null data) {
    M = 24 * 60 * data.days;
    SLAlgo.data = data;
    mValue = new double[M];
    for (int i = 0; i < M; i++) {
      mValue[i] = 0;
    }
    SLAlgo.calculateBeginAndEndMinutes();
    SLAlgo.calculateMValue();
  }

  private static void calculateMValue() {
    for (int day = 1; day <= data.days; day++) {
      for (int i = 1; i <= data.sites; i++) {
        double avg = data.value[i] / data.desiredTime[i];
        for (int m = beginMinute[i][day]; m < endMinute[i][day]; m++) {
          if (avg > mValue[m]) {
            mValue[m] = avg;
          }
        }
      }
    }

    for (int m = M - 2; m >= 0; m--) {
      mValue[m] = mValue[m + 1] + mValue[m];
    }
  }


  private static void calculateBeginAndEndMinutes() {
    int days = data.days;
    int sites = data.sites;
    beginMinute = new int[sites + 1][days + 1];
    endMinute = new int[sites + 1][days + 1];

    int minuteOneDay = 24 * 60;
    for (int day = 1; day <= days; day++) {
      int extraMinute = minuteOneDay * (day - 1);
      for (int site = 1; site <= sites; site++) {
        beginMinute[site][day] = data.beginMinute[site][day] + extraMinute;
        endMinute[site][day] = data.endMinute[site][day] + extraMinute;
      }
    }
  }

}
