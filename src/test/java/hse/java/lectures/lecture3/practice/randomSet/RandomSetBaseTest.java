package hse.java.lectures.lecture3.practice.randomSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomSetBaseTest {

    @Test
    void insertRemoveContains() {
        RandomSet<Integer> set = new RandomSet<>();

        assertTrue(set.insert(10));
        assertTrue(set.insert(20));
        assertFalse(set.insert(10));

        assertTrue(set.contains(10));
        assertTrue(set.contains(20));
        assertFalse(set.contains(30));

        assertTrue(set.remove(10));
        assertFalse(set.remove(10));

        assertFalse(set.contains(10));
        assertTrue(set.contains(20));
    }

}
