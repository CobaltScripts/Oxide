package oxide.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Command {

  private final String name;
  private final String description;
  private final String usage;

  public abstract void execute(String[] args);

}
