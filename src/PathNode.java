import java.util.*;

public class PathNode extends State {

  int day;
  int site;
  int finishTime;
  // including this site
  List<Integer> exploredSites;

  private PathNode() {
    day = 1;
    this.site = 0;
    exploredSites = new ArrayList<>();
  }

  public PathNode(PathNode originState, int site, Null data) throws Exception {
    day = 1;
    double[] mValue = SLAlgo.mValue;
    this.site = site;
    int originSite = originState.site;
    finishTime = Math.max(originState.finishTime + data.dist[site][originSite], data.beginMinute[site][1])
            + data.desiredTime[site];

    if (finishTime > data.endMinute[site][day]) {
      throw new Exception("finish time of first site exceeds end hour");
    }
    exploredSites = new ArrayList<>(originState.exploredSites);
    exploredSites.add(site);
    g = originState.g + data.value[originSite];
    h = mValue[finishTime];
    gh = g + h;
  }

  public static State initState() {
    return new PathNode();
  }

  @Override
  public Set<State> getAdjStates() {
    Null data = Null.data;
    Set<State> nextSet = new HashSet<>();
    for (int i = 1; i <= data.sites; i++) {
      if (exploredSites.contains(i)) {
        continue;
      }
      try {
        PathNode node = new PathNode(this, i, data);
        nextSet.add(node);
      } catch (Exception ignore) {
      }
    }
    return nextSet;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    exploredSites.forEach((site) -> builder.append(site+" "));
    double sum=0;
    for (int site: exploredSites) {
      sum += Null.data.value[site];
    }
    builder.append("Value: ");
    builder.append(sum);
    return builder.toString();
  }
}
