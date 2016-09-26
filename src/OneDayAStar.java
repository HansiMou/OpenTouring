import java.util.Set;

public class OneDayAStar extends AStar {

  public OneDayAStar(int maxValue) {
    super(maxValue);
  }

  @Override
  protected State getInitState() {
    return PathNode.initState();
  }
}
