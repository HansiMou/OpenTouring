import java.util.*;

public abstract class AStar {
  State result;

  int lastMax;
  Queue<State>[] states;


  public AStar(int maxValue) {
    lastMax = maxValue;
    states = new Queue[maxValue + 1];
    for (int i = 0; i <= maxValue; i++) {
      states[i] = new LinkedList<>();
    }
  }

  protected abstract State getInitState();

  public void addState(State state) {
    int gh = (int) state.gh;
    lastMax = Math.max(lastMax, gh);
    states[gh].add(state);
  }

  private State pollFirst() {
    for (int i = lastMax; i >= 0; i--) {
      if (!states[i].isEmpty()) {
        lastMax = i;
        return states[i].poll();
      }
    }
    return null;
  }

  private State getFirst() {
    for (int i = lastMax; i >= 0; i--) {
      if (!states[i].isEmpty()) {
        lastMax = i;
        return states[i].peek();
      }
    }
    return null;
  }


  public String startSearch(long timeLimit) {
    long start = System.currentTimeMillis();

    State initState = getInitState();
    addState(initState);

    long count = 0;
    long time;
    while (true) {
      count++;
      if (count % 10 ==0) {
        time = System.currentTimeMillis() - start;
        if (time > timeLimit) {
          return pollFirst().toString();
        }
      }
      State maxState = pollFirst();

      Set<State> adjStates = maxState.getAdjStates();

      if (adjStates.size() == 0) {
        return maxState.toString();
      }

      for (State state : adjStates) {
        addState(state);
      }
    }
  }

  public void output() {
    System.out.println("result: " + result);
    System.out.println("gh value: " + result.g);
  }
}
