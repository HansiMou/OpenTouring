import java.util.*;

public abstract class AStar {
  List<State> stateList = new ArrayList<>(100000);
  List<State> results = new ArrayList<>();

  int start = 0;
  int MAX = 1;
  int currentResult = 0;

  protected abstract State getInitState();

  public void addState(State state) {
    for (int i = start; i < stateList.size(); i++) {
      if (state.gh > stateList.get(i).gh) {
        stateList.add(i, state);
        return;
      }
    }
    stateList.add(stateList.size(), state);
  }

  private State getFirst() {
    State state = stateList.get(start);
    start++;
    return state;
  }


  public void startSearch() {
    State initState = getInitState();
    addState(initState);

    while (true) {
      State maxState = getFirst();

      Set<State> adjStates = maxState.getAdjStates();
      if (adjStates.size() == 0) {
        results.add(maxState);
        currentResult ++;
        if (currentResult >= MAX) {
          return;
        }
      }

      for (State state : adjStates) {
        addState(state);
      }
    }
  }

  public void output() {
    System.out.println(results.get(0));
  }
}
