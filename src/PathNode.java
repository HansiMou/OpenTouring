import java.util.*;

public class PathNode extends State {

  int day;
  int site;
  int finishTime;
  // including this site
  List<Integer> exploredSites;

  PathNode previousNode;

  private PathNode() {
    day = 0;
    this.site = 0;
    this.finishTime = 0;
    exploredSites = new ArrayList<>();
    this.g = 0;
    this.h = SLAlgo.mValue[0];
    this.gh = g + h;
    previousNode = null;
  }


  public PathNode(PathNode previousNode, int site, int day, int finishTime, List<Integer> exploredSites, double g, double h) {
    this.previousNode = previousNode;
    this.day = day;
    this.exploredSites = exploredSites;
    this.finishTime = finishTime;
    this.site = site;
    this.g = g;
    this.h = h;
    gh = g + h;
  }

  public static State initState() {
    return new PathNode();
  }

  @Override
  public Set<State> getAdjStates() {
    Null data = SLAlgo.data;
    Set<State> nextSet = goNextSiteToday();
    if (!nextSet.isEmpty()) {
      return nextSet;
    }

    if (day < data.days) {
      for (int nextSite = 1; nextSite <= data.sites; nextSite++) {
        if (exploredSites.contains(nextSite)) {
          continue;
        }
        PathNode node = goNextSiteTomorrow(nextSite);
        if (node != null) {
          nextSet.add(node);
        }
      }
    }

    return nextSet;
  }

  @Override
  public String toString() {
    if (previousNode == null) {
      return "empty node";
    }
    if (previousNode.day == 0) {
      return "" + site;
    }
    if (previousNode.day == day) {
      return previousNode.toString() + " " + site;
    } else {
      return previousNode.toString() + "\n" + site;
    }
  }

  private Set<State> goNextSiteToday() {
    Set<State> nextSet = new HashSet<>();
    if (day == 0) {
      return nextSet;
    }
    Null data = SLAlgo.data;
    double[] mValue = SLAlgo.mValue;
    int[][] beginMinute = SLAlgo.beginMinute;
    int[][] endMinute = SLAlgo.endMinute;

    for (int nextSite = 1; nextSite <= data.sites; nextSite++) {
      if (exploredSites.contains(nextSite)) {
        continue;
      }
      int nextFinishTime = Math.max(finishTime + data.dist[site][nextSite], beginMinute[nextSite][day])
              + data.desiredTime[nextSite];

      if (nextFinishTime > endMinute[nextSite][day]) {
        continue;
      }
      List<Integer> nextExploredSites = new ArrayList<>(exploredSites);
      nextExploredSites.add(nextSite);
      double gNext = g + data.value[nextSite];
      double hNext = mValue[nextFinishTime];

      nextSet.add(new PathNode(this, nextSite, day, nextFinishTime, nextExploredSites, gNext, hNext));
    }
    return nextSet;
  }

  private PathNode goNextSiteTomorrow(int nextSite) {

    int tomorrow = day + 1;
    double[] mValue = SLAlgo.mValue;
    int[][] beginMinute = SLAlgo.beginMinute;
    int[][] endMinute = SLAlgo.endMinute;
    Null data = SLAlgo.data;
    int nextFinishTime = beginMinute[nextSite][tomorrow] + data.desiredTime[nextSite];

    if (nextFinishTime > endMinute[nextSite][tomorrow]) {
      return null;
    }
    List<Integer> nextExploredSites = new ArrayList<>(exploredSites);
    nextExploredSites.add(nextSite);
    double gNext = g + data.value[nextSite];
    double hNext = mValue[nextFinishTime];

    return new PathNode(this, nextSite, tomorrow, nextFinishTime, nextExploredSites, gNext, hNext);
  }
}
