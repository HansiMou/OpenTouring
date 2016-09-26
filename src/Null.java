import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Null {

  // unchanged variables
  int[] ave;
  int[] street;
  int[][] dist;

  int[] desiredTime;
  double[] value;
  int[][] beginMinute;
  int[][] endMinute;
  int days;
  int sites;
  // TODO assume the final state can visit all sites
  double totalValue;

  // the earilest time you can leave from previous site
  int currentTime;
  double[] FList;
  double[] GList;
  double[] HList;
  StringBuilder path;
  HashSet<Integer> visited;

  public static void main(String[] args) {
    long start = System.currentTimeMillis();

    String fileNameString = "input.txt";

    Null data = new Null(fileNameString);
    SLAlgo.init(data);
    if (SLAlgo.mValue[0] > Integer.MAX_VALUE) {
      System.out.println("Maximum value exceeds INT_MAX");
      return;
    }
    int maxValue = (int) SLAlgo.mValue[0];
    OneDayAStar aStar = new OneDayAStar(maxValue + 2);

    // this is the time limit to run this function, in milliseconds
    // so I set it as 100 second,
    String result = aStar.startSearch(100 * 1000);

    System.out.println("time:" + (System.currentTimeMillis() - start));
    System.out.println(result);
    System.out.println(data.isValid(result));
    System.out.println(data.calculateValue(result));
//
//    Null ot = new Null(fileNameString);
//    System.out.println(ot.isValid(result));
//
//    System.out.println(ot.calculateValue(result));
//    ot.search();
  }

  public Null(String name) {
    getSitesAndDays(name);
    ave = new int[sites + 1];
    street = new int[sites + 1];
    dist = new int[sites + 1][sites + 1];

    desiredTime = new int[sites + 1];
    value = new double[sites + 1];
    beginMinute = new int[sites + 1][days + 1];
    endMinute = new int[sites + 1][days + 1];
    FList = new double[sites + 1];
    GList = new double[sites + 1];
    HList = new double[sites + 1];
    path = new StringBuilder();
    visited = new HashSet<Integer>();
    AssignValues(name);

    // caculate total values
    for (double d : value) {
      totalValue += d;
    }
  }

  public void AssignValues(String fileName) {
    File file = new File(fileName);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String tempString = null;
      while ((tempString = reader.readLine()) != null) {
        if (!tempString.startsWith("site")) {
          String[] ss = tempString.split(" ");
          if (ss.length == 5) {
            int site = Integer.parseInt(ss[0]);
            ave[site] = Integer.parseInt(ss[1]);
            street[site] = Integer.parseInt(ss[2]);
            desiredTime[site] = Integer.parseInt(ss[3]);
            value[site] = Double.parseDouble(ss[4]);
          } else if (ss.length == 4) {
            int site = Integer.parseInt(ss[0]);
            int day = Integer.parseInt(ss[1]);
            beginMinute[site][day] = Integer.parseInt(ss[2]) * 60;
            endMinute[site][day] = Integer.parseInt(ss[3]) * 60;
          }
        }
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    for (int i=1;i<=sites;i++) {
      for (int j=1;j<=sites;j++) {
        dist[i][j] = Math.abs(street[i] - street[j]) + Math.abs(ave[i] - ave[j]);
      }
    }
  }

  public void getSitesAndDays(String fileName) {
    File file = new File(fileName);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String tempString = null;
      String pre = null;
      while ((tempString = reader.readLine()) != null) {
        pre = tempString;
      }
      sites = Integer.parseInt(pre.split(" ")[0]);
      days = Integer.parseInt(pre.split(" ")[1]);
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
  }

  public String search() {
    for (int i = 1; i <= days; i++) {
      int startSite = findStartSite(i);
      if (startSite == -1) {
        System.out.println("No start point found");
        return path.toString();
      }
      visited.add(startSite);
      path.append(startSite);
      currentTime = beginMinute[startSite][i] + desiredTime[startSite];

      int nextSite = -1;
      int fatherSite = startSite;
      while ((nextSite = findNextOne(fatherSite, i)) != -1) {
        path.append(" " + nextSite);
        visited.add(nextSite);
        fatherSite = nextSite;
        int earliestArriveTime = currentTime + Math.abs(ave[i] - ave[fatherSite])
                + Math.abs(street[i] - street[fatherSite]);
        currentTime = Math.max(beginMinute[nextSite][i], earliestArriveTime) + desiredTime[i];
      }
      path.append("\n");
    }
		System.out.println(path.toString());
        System.out.println(isValid(path.toString()));
    System.out.println(calculateValue(path.toString()));
    return path.toString();
  }

  public int findStartSite(int k) {
    int start = -1;
    double min = Double.MAX_VALUE;

    for (int i = 1; i <= sites; i++) {
      HList[i] = -value[i];
    }
    for (int i = 1; i <= sites; i++) {
      if (!visited.contains(i)) {
        int startTime = beginMinute[i][k];
        int leaveTime = startTime + desiredTime[i];
        double valueLost = 0;
        for (int j = 1; j <= sites; j++) {
          if (i != j && !visited.contains(j)) {
            int timeToGetJ = leaveTime + Math.abs(ave[i] - ave[j]) + Math.abs(street[i] - street[j]);
            if (timeToGetJ + desiredTime[j] > endMinute[j][k]) {
              valueLost += value[j];
            }
          }
        }
        GList[i] = valueLost;
        FList[i] = GList[i] + HList[i];

        if (FList[i] < min) {
          min = FList[i];
          start = i;
        }
      }
    }
    return start;
  }

  public int findNextOne(int fatherSite, int day) {
    int next = -1;
    double min = Double.MAX_VALUE;

    for (int i = 1; i <= sites; i++) {
      if (!visited.contains(i)) {
        int earliestArriveTime = currentTime + Math.abs(ave[i] - ave[fatherSite])
                + Math.abs(street[i] - street[fatherSite]);
        // able to visit the site
        if (earliestArriveTime + desiredTime[i] <= endMinute[i][day]) {
          int actualLeaveTime = Math.max(beginMinute[i][day], earliestArriveTime) + desiredTime[i];
          double valueLost = 0;
          for (int j = 1; j <= sites; j++) {
            if (i != j && !visited.contains(j)) {
              int timeToGetJ = actualLeaveTime + Math.abs(ave[i] - ave[j]) + Math.abs(street[i] - street[j]);
              if (timeToGetJ + desiredTime[j] > endMinute[j][day]) {
                valueLost += value[j];
              }
            }
          }
          GList[i] = valueLost;
          FList[i] = GList[i] + HList[i];

          if (FList[i] < min) {
            min = FList[i];
            next = i;
          }
        }
      }
    }
    return next;
  }

  public boolean isValid(String res) {
    String[] array = res.split("\n");
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null && array[i].length() != 0) {
        int last = -1;
        int currentTime = -1;
        for (String tmp : array[i].split(" ")) {
          int site = Integer.parseInt(tmp);
          if (currentTime == -1) {
            currentTime = beginMinute[site][i + 1] + desiredTime[site];
            last = site;
          } else {
            int earliestArriveTime = currentTime + Math.abs(ave[site] - ave[last]) +
                    Math.abs(street[site] - street[last]);
            if (earliestArriveTime + desiredTime[site] > endMinute[site][i + 1]) {
              return false;
            }
            currentTime = Math.max(earliestArriveTime, beginMinute[site][i + 1]) + desiredTime[site];
            last = site;
          }
        }
      }
    }
    return true;
  }

  public double calculateValue(String res) {
    double sum = 0;
    HashSet<Integer> hs = new HashSet<Integer>();
    for (String s1 : res.split("\n")) {
      for (String site : s1.split(" ")) {
        if (hs.add(Integer.parseInt(site))) {
          sum += value[Integer.parseInt(site)];
        }
      }
    }
    return sum;
  }
}
