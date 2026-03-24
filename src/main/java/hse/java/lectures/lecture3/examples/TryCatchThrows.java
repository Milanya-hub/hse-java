package hse.java.lectures.lecture3.examples;

import java.io.*;

public class TryCatchThrows {

    public void testRuntime() throws RuntimeException {
        throw new RuntimeException("Test runtime");
    }

    public void testException() throws Exception {
        throw new Exception("test exception");
    }

    public void testError() throws Error {
        throw new Error("test error");
    }

    public void testTryRuntime() {
        try {
            testRuntime();
        } catch (RuntimeException e) {
            System.out.println("Catch " + e.getMessage());
        } finally {
            System.out.println("finally");
        }
    }

    public void testTryException() {
        try {
            testException();
        } catch (Exception e) {
            System.out.println("Catch " + e.getMessage());
        } finally {
            System.out.println("finally");
        }
    }

    public void testTryError() {
        // testError();
        try {
            testError();
        } catch (Error e) {
            System.out.println("Catch " + e.getMessage());
        } finally {
            System.out.println("finally");
        }
    }

    public static void foo() {
        throw new RuntimeException("My runtime");
    }

    public static void bar() throws Exception {
        throw new Exception("My exception");
    }

    public static void main(String[] args) {
        // foo();
//        try {
//            bar();
//        } catch (Exception e) {
//            System.err.println("Hello exception");
//        } finally {
//
//        }

//        try (BufferedReader br = new BufferedReader(new FileReader(""))) {
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        // int x = 1/ 0;
        int[] a = new int[1];
        System.out.println(a[2]);
    }

}
