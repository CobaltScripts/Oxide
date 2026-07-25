package oxide.command;

import lombok.Getter;
import oxide.command.impl.GoToCommand;
import oxide.util.ChatUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class CommandManager {

    @Getter
    private static final CommandManager instance = new CommandManager();

    private final String PREFIX = "-";
    private final Map<String, Command> commands = new HashMap<>();

    private CommandManager() {
        register(new GoToCommand());
    }

    public void register(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public boolean execute(String message) {
        if (!message.startsWith(PREFIX)) {
            return false;
        }

        String input = message.substring(1).trim();

        if (input.isEmpty()) {
            return true;
        }

        String[] split = input.split("\\s+");
        Command command = commands.get(split[0].toLowerCase());

        if (command == null) {
            ChatUtils.send("Unknown command.");
            return true;
        }

        command.execute(Arrays.copyOfRange(split, 1, split.length));
        return true;
    }

}
