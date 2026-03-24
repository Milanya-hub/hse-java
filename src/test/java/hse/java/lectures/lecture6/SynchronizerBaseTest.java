package hse.java.lectures.lecture6;

import hse.java.lectures.lecture6.tasks.synchronizer.StreamWriter;
import hse.java.lectures.lecture6.tasks.synchronizer.Synchronizer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@Tag("synchronizer")
public class SynchronizerBaseTest {

    @Test
    void testPipeline() {
        int ticksPerWriter = 10;
        ByteArrayOutputStream raw = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(raw, true, StandardCharsets.UTF_8);

        AtomicInteger aCalls = new AtomicInteger();
        AtomicInteger bCalls = new AtomicInteger();
        AtomicInteger cCalls = new AtomicInteger();

        List<StreamWriter> tasks = List.of(
                new StreamWriter(1, "A", stream, aCalls::incrementAndGet),
                new StreamWriter(2, "B", stream, bCalls::incrementAndGet),
                new StreamWriter(3, "C", stream, cCalls::incrementAndGet)
        );

        Synchronizer synchronizer = new Synchronizer(tasks, ticksPerWriter);

        assertTimeoutPreemptively(Duration.ofSeconds(3), synchronizer::execute);

        assertEquals("ABC".repeat(ticksPerWriter), raw.toString(StandardCharsets.UTF_8));
        assertEquals(ticksPerWriter, aCalls.get());
        assertEquals(ticksPerWriter, bCalls.get());
        assertEquals(ticksPerWriter, cCalls.get());
    }

    @Test
    void testPipelineDifferentTicksAndTaskCount() {
        int ticksPerWriter = 4;
        ByteArrayOutputStream raw = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(raw, true, StandardCharsets.UTF_8);

        AtomicInteger aCalls = new AtomicInteger();
        AtomicInteger bCalls = new AtomicInteger();
        AtomicInteger cCalls = new AtomicInteger();
        AtomicInteger dCalls = new AtomicInteger();

        List<StreamWriter> tasks = List.of(
                new StreamWriter(4, "D", stream, dCalls::incrementAndGet),
                new StreamWriter(2, "B", stream, bCalls::incrementAndGet),
                new StreamWriter(1, "A", stream, aCalls::incrementAndGet),
                new StreamWriter(3, "C", stream, cCalls::incrementAndGet)
        );

        Synchronizer synchronizer = new Synchronizer(tasks, ticksPerWriter);

        assertTimeoutPreemptively(Duration.ofSeconds(3), synchronizer::execute);

        assertEquals("ABCD".repeat(ticksPerWriter), raw.toString(StandardCharsets.UTF_8));
        assertEquals(ticksPerWriter, aCalls.get());
        assertEquals(ticksPerWriter, bCalls.get());
        assertEquals(ticksPerWriter, cCalls.get());
        assertEquals(ticksPerWriter, dCalls.get());
    }

}
