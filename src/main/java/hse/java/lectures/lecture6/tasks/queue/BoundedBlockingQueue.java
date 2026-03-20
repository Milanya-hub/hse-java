package hse.java.lectures.lecture6.tasks.queue;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedBlockingQueue<T> {

    private final Queue<T> q;
    private final int capacity;
    private int size;

    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        q = new LinkedList<T>();
        size = 0;
    }

    public void put(T item) throws InterruptedException {
        if (item == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            while (size == capacity) {
                wait();
            }
            q.add(item);
            size++;
            notifyAll();
        }
    }

    public T take() throws InterruptedException {
        synchronized (this) {
            while (size == 0) {
                wait();
            }
            T item = q.poll();
            size--;
            notifyAll();
            return item;
        }
    }

    public synchronized int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }
}
