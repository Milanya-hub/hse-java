package hse.java.lectures.lecture6.tasks.synchronizer;

import java.util.List;

public class Synchronizer {

    public static final int DEFAULT_TICKS_PER_WRITER = 10;
    private final List<StreamWriter> tasks;
    private final int ticksPerWriter;

    public Synchronizer(List<StreamWriter> tasks) {
        this(tasks, DEFAULT_TICKS_PER_WRITER);
    }

    public Synchronizer(List<StreamWriter> tasks, int ticksPerWriter) {
        this.tasks = tasks;
        this.ticksPerWriter = ticksPerWriter;
    }

    /**
     * Starts infinite writer threads and waits until each writer prints exactly ticksPerWriter ticks
     * in strict ascending id order.
     */
    public void execute() {
        // add monitor and sync
        for (StreamWriter writer : tasks) {
            Thread worker = new Thread(writer, "stream-writer-" + writer.getId());
            worker.setDaemon(true);
            worker.start();
        }
    }

}
