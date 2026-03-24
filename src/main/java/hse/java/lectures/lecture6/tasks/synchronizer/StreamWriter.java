package hse.java.lectures.lecture6.tasks.synchronizer;

import lombok.Getter;

import java.io.PrintStream;

public class StreamWriter implements Runnable {

    private final String message;
    @Getter
    private final int id;
    private final PrintStream output;
    private final Runnable onTick;
    private volatile StreamingMonitor monitor;

    public StreamWriter(int id, String message, PrintStream output, Runnable onTick) {
        this.message = message;
        this.id = id;
        this.output = output;
        this.onTick = onTick;
    }

    public void attachMonitor(StreamingMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Writer threads are intentionally infinite for the task contract.
        while (true) {
            output.print(message);
            onTick.run();
        }
    }

}
