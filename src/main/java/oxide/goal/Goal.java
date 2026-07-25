package oxide.goal;

public interface Goal {

  double heuristic(int x, int y, int z);

  boolean isAtGoal(int x, int y, int z);

}
