package hse.java.lectures.lecture6.tasks;

public class ThreadPrinter {

    public static synchronized void foo() {

    }

    public static void bar() {
        synchronized (ThreadPrinter.class) {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Object monitor = new Object();
        Thread t1 = new Thread(() -> {
            while (true) {
                synchronized (monitor) {
                    System.out.print(1 + " ");
                    monitor.notify();
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread t2 = new Thread(() -> {
            while (true) {
                synchronized (monitor) {
                    System.out.print(2 + " ");
                    monitor.notify();
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.setPriority(10);
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}
