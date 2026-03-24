package hse.java.lectures.lecture3.examples;

import java.util.ArrayList;
import java.util.List;

public class Generics {



    public <T> T getType(T type) {
        System.out.println(type.getClass());
        return type;
    }

    public static <T extends Number> void write(List<T> list, T value) {
        list.add(value);
    }

    public static <T extends Number & Comparable<T>> T get(T value) {
        return value;
    }

    public static void testGenerics() {
        // get(1.).compareTo()
        write(List.of(1), 1);
        write(List.of(), 1.2);
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        // PECS
        List<? extends Number> nums = ints;
        List<? super Integer> si = ints;
        si.add(1);
        si.get(1);
    }

    public static void main(String[] args) {
        List<Integer> il = new ArrayList<>();
        write(il, 5);
        System.out.println(il);
    }

}
