package hse.java.lectures.lecture3.examples;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Main {
    public static void main(String[] args) {
        Box<Integer> box = new Box<>();
        Box<Double> dBox = new Box<>();
        Callable<Integer> callable;

        int x = new Integer(3);
        Integer y = 5;

        Box<? extends Number> nBox = box;
        new Methods().get(new ArrayList<Integer>()).add(1);

    }
}
