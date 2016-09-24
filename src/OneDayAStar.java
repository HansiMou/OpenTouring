import java.util.Set;

public class OneDayAStar extends AStar {

  public OneDayAStar() {

  }

  @Override
  protected State getInitState() {
    return PathNode.initState();
  }
}
