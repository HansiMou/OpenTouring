public class SLAlgo {

  private static final int M = 24 * 60;
  private static Null data;
  public static double[] mValue;
  public static void init(Null data) {
    SLAlgo.data = data;
    mValue = new double[M];
    for (int i=0;i<M;i++) {
      mValue[i] = 0;
    }
  }

  private static void calculateMValue() {
    for (int i=1;i<=data.sites;i++) {
      double avg = data.value[i] / data.desiredTime[i];
      for (int m=data.beginMinute[i][1];m<data.endMinute[i][1];m++) {
        if (avg>mValue[m]) {
          mValue[m] = avg;
        }
      }
    }

    for (int m = M-2;m>=0;m--) {
      mValue[m] = mValue[m+1] + mValue[m];
    }
  }

  public static void main(String[] args) {
    Null data = new Null("input.txt");
    SLAlgo.init(data);
    SLAlgo.calculateMValue();
//    for (double d: SLAlgo.mValue) {
//      System.out.println(d);
//    }
    OneDayAStar aStar = new OneDayAStar();
    aStar.startSearch();
    aStar.output();
    System.out.println(aStar.stateList.size());
  }


}
