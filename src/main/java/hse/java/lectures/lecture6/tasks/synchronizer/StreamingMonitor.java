package hse.java.lectures.lecture6.tasks.synchronizer;

import java.io.PrintStream;
import java.util.*;
public class StreamingMonitor {
    private final List<StreamWriter> writers;
    private final int ticks_per_writer;

    private final Map<Integer, Integer> done_ticks = new HashMap<>();

    private int cur_ind;
    private int cnt_ticks;
    private final int max_cnt_ticks;

    public StreamingMonitor(List<StreamWriter> writers, int ticks_per_writer) {
        this.writers = writers;
        this.ticks_per_writer = ticks_per_writer;
        this.max_cnt_ticks = writers.size() *  ticks_per_writer;

        for (StreamWriter writer : writers) {
            done_ticks.put(writer.getId(), 0);
        }
        cur_ind = 0;
        cnt_ticks = 0;
    }

    private boolean can_run(int id) {
        if (cnt_ticks >= max_cnt_ticks) {
            return true;
        }
        return writers.get(cur_ind).getId() == id && done_ticks.get(id) < ticks_per_writer;
    }

    public synchronized void wait_id(int id) throws InterruptedException {
        while (!can_run(id)) {
            wait();
        }
    }

    private void next() {
        int n = writers.size();

        for (int i = 1; i <= n; i++) {
            int next = (cur_ind + i) % n;
            int nextId = writers.get(next).getId();

            if (done_ticks.get(nextId) < ticks_per_writer) {
                cur_ind = next;
                return;
            }
        }
    }

    public synchronized boolean work_tick(int id, String message, PrintStream output, Runnable onTick) throws InterruptedException {
        while (!can_run(id)) {
            wait();
        }

        if (cnt_ticks >= max_cnt_ticks) {
            notifyAll();
            return false;
        }
        output.print(message);
        onTick.run();
        done_ticks.put(id, done_ticks.get(id) + 1);
        cnt_ticks++;
        next();

        notifyAll();
        return cnt_ticks < max_cnt_ticks;
    }

    public synchronized void wait_all() throws InterruptedException {
        while (cnt_ticks < max_cnt_ticks) {
            wait();
        }
    }
}
