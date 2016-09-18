package ee.smkv.scheduler.executors;

import ee.smkv.scheduler.model.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellTaskExecutor extends TaskExecutor {
    public ShellTaskExecutor(Long executionId , Task task) {
        super(executionId, task);
    }

    @Override
    protected void executeCommand(String command) throws IOException, InterruptedException {
        logger.info(command);
        Process process = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                output(line);
            }
            int exitCode = process.waitFor();
            output("ExitCode: " + exitCode);
        }

    }
}
