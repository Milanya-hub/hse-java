package hse.java.lectures.lecture4;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.summingLong;

public class FileUtils {


    /**
     * The operator calculates the sum in the given range (inclusively)
     */
    public static final IntBinaryOperator sumOperator = (x, y) -> x + y;

    /**
     * The operator calculates the product in the given range (inclusively)
     */
    public static final IntBinaryOperator productOperator = (x, y) -> x * y;

    void createFileWithAttributes() throws IOException {
        // POSIX
        // USER GROUP OTHER
        Set<PosixFilePermission> perms =
                PosixFilePermissions.fromString("rw-r--r--");

        FileAttribute<Set<PosixFilePermission>> attr =
                PosixFilePermissions.asFileAttribute(perms);

        Files.createFile(Path.of("test.txt"), attr);
    }

    void fileMethods() {
        File file = new File("test.txt");
        System.out.println(file.length());
        System.out.println(file.isFile());
        System.out.println(file.isDirectory());
        // flags
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File li : files) {
                    System.out.println(li.getName());
                }
            }
        }
    }

    static void fileSystem(boolean running) throws Exception {
        Files.createDirectories(Path.of("hello", "test", "one"));
        try (var ws = FileSystems.getDefault().newWatchService()) {
            Path path = Path.of("hello");
            path.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
            while (running) {
                var key = ws.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(event.kind() + " " + event.context());
                }

                key.reset();
            }
        }

    }

    static void pathMethods() throws IOException {
        Path path = Path.of("test");
        Path filePath = path.resolve("test.txt");
        System.out.println(filePath);
        System.out.println(Files.exists(filePath));

        try {
            Files.createFile(filePath);
            System.out.println(Files.exists(filePath));
        } catch (Exception e) {
            System.err.println("File already exist");
        }

        Path garbage = Path.of("test", "..", "test");
        System.out.println(garbage);
        System.out.println(garbage.normalize());
    }

    static void filesMethods() throws IOException {
        Files.createDirectories(Path.of("test", "one", "two"));
    }

    static void foo() {
        try (InputStream is = FileUtils.class.getResourceAsStream("test.txt")) {
            byte[] bytes = is.readAllBytes();
            System.out.println(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void benchMark() {
        long start = System.currentTimeMillis();
        benchMarkWrite();
        long end = System.currentTimeMillis();
        System.out.println("Processing time: " + (end - start) + " ms.");
    }

    static void benchMarkWrite() {
        URL url = FileUtils.class.getResource("test.txt");
        try (OutputStream os = new FileOutputStream(url.getFile())) {
            int bufferSize = 8196;
            byte[] buffer = new byte[bufferSize];
            for (int i = 0; i < 1000000; i++) {
                buffer[i % bufferSize] = (byte) i;
                if ((i + 1) % bufferSize == 0) {
                    os.write(buffer);
                }
            }


            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

    }
}
