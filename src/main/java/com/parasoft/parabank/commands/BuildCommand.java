package com.parasoft.parabank.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds and executes a queue of Command objects.
 */
public class BuildCommand {
    private final List<Command> commandQueue = new ArrayList<>();

    /**
     * Adds a command to the execution queue.
     *
     * @param command the Command to add
     */
    public void addCommand(Command command) {
        commandQueue.add(command);
    }

    /**
     * Executes all commands in the queue.
     * If any command fails, logs the error and continues with the next command.
     * Clears the queue after execution.
     *
     * @throws Exception if any command throws an exception (aggregates all exceptions)
     */
    public void executeAll() throws Exception {
        List<Exception> exceptions = new ArrayList<>();
        for (Command command : commandQueue) {
            try {
                command.execute();
            } catch (Exception e) {
                // Optionally log here using LogController if available
                exceptions.add(e);
            }
        }
        commandQueue.clear();
        if (!exceptions.isEmpty()) {
            Exception aggregate = new Exception("One or more commands failed.");
            for (Exception e : exceptions) {
                aggregate.addSuppressed(e);
            }
            throw aggregate;
        }
    }
}
