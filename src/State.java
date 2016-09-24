import java.util.Set;

public abstract class State {
  public double g = 0;
  public double h = 0;
  public double gh = 0;

  public abstract Set<State> getAdjStates();
}
