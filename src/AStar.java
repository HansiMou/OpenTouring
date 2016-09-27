import java.util.*;

public abstract class AStar {
  public static final int N = 10000;

  int maxValue;
  State result;

  int lastMax;
  Queue<State>[] states;

  int[] stateCountInThatDay;

  public AStar(int maxValue) {
    this.maxValue = maxValue;
    lastMax = maxValue;
    states = new Queue[maxValue + 1];
    for (int i = 0; i <= maxValue; i++) {
      states[i] = new LinkedList<>();
    }

    stateCountInThatDay = new int[SLAlgo.data.days + 1];
  }

  protected abstract State getInitState();

  public void addState(State state) {
    stateCountInThatDay[state.day]++;

    int gh = (int) state.gh;
    lastMax = Math.max(lastMax, gh);
    states[gh].add(state);
  }

  private State pollFirst() {
    for (int i = lastMax; i >= 0; i--) {
      if (!states[i].isEmpty()) {
        lastMax = i;
        State state = states[i].poll();
        stateCountInThatDay[state.day]--;
        return state;
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

  private void filterStates() {
    int currentLastMax = lastMax;
    Queue<State>[] newStates = new Queue[maxValue + 1];
    for (int i = 0; i <= maxValue; i++) {
      newStates[i] = new LinkedList<>();
    }

    int maxDay = 0;
    for (maxDay = SLAlgo.data.days; maxDay >= 0; maxDay--) {
      if (stateCountInThatDay[maxDay] > 0) {
        break;
      }
    }

    if (stateCountInThatDay[maxDay] < N) {
      maxDay = maxDay - 1;
    }

    int count = N;
    for (int i = 0; i <= maxValue && count > 0; i++) {
      while (!states[i].isEmpty() && count > 0) {
        State state = states[i].poll();
        if (state.day >= maxDay) {
          newStates[i].add(state);
          count--;
        }
      }
    }
    states = newStates;
    lastMax = currentLastMax;
  }

  public String startSearch(long timeLimit) {
    long start = System.currentTimeMillis();
    int days = SLAlgo.data.days;

    int MAX = days + 5;
    long[] limits = new long[MAX];


    for (int i = 0; i < MAX; i++) {
      limits[i] = timeLimit / MAX * (i+1);
    }

    State initState = getInitState();
    addState(initState);

    long count = 0;
    long time;

    int filterCount = 1;
    while (true) {
      count++;
      if (count % 10 == 0) {
        time = System.currentTimeMillis() - start;
        if (time > limits[filterCount]) {
          filterStates();
          filterCount++;
          if (filterCount == MAX) {
            System.out.println("reached limit");
            return pollFirst().toString();
          }
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
