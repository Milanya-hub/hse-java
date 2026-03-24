package hse.java.lectures.lecture5;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Main {

    @FunctionalInterface
    interface Foo {

        void run();

        default void lol() {
            System.out.println("LOL");
        }
    }

    @FunctionalInterface
    interface Consume {
        void consume(String arg);
    }

    static void foo(Foo foo) {
        foo.run();
        System.out.println(foo.getClass());
    }

    static void foo(Consume foo, String arg) {
        foo.consume(arg);
        System.out.println(foo.getClass());
    }

    public static void main(String[] args) {
        foo(() -> System.out.println("Hello from lambda Foo"));

        foo(System.out::println, "Hello from lambda");
        foo(new Foo() {
            @Override
            public void run() {
                System.out.println("Hello from Foo");
            }
        });

        Consumer<String> printer = x -> System.out.println(x);
        Consumer<String> printerRef = System.out::println;

        Predicate<Integer> odd = x -> x % 2 == 1;

        Supplier<String> stringer = () -> "Hello world";
        Supplier<List<Map<String, Set<String>>>> mainSupplier = ArrayList::new;

        Function<String, Integer> length = String::length;

//        User user = User.builder()
//                .name("Ivan")
//                .surname("Ivanov")
//                .age(15)
//                .address("Avenue 1")
//                .build();

//        System.out.println(user);

        List<Integer> list = List.of(1, 2, 3, 4, 5);

        list.stream()
                .map(x -> IntStream.rangeClosed(0, x))
                .peek(x -> System.out.println())
                .forEach(x -> x.forEach(y -> System.out.print(y + " ")));

        System.out.println();

        list.stream()
                .map(x -> IntStream.rangeClosed(0, x))
                .flatMapToInt(x -> x)
                .forEach(System.out::println);




    }

}
