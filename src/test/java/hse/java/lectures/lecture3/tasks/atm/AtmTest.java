package hse.java.lectures.lecture3.tasks.atm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static hse.java.lectures.lecture3.tasks.atm.Atm.Denomination.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Disabled
class AtmTest {

    @Test
    void initialBalanceIsZero() {
        Atm atm = new Atm();
        assertEquals(0, atm.getBalance());
    }

    @Test
    void depositIncreasesBalance() {
        Atm atm = new Atm();
        atm.deposit(Map.of(D100, 10, D500, 5));
        assertEquals(3500, atm.getBalance());
    }

    
    @Test
    void depositRejectsNullMap() {
        Atm atm = new Atm();
        assertThrows(InvalidDepositException.class, () -> atm.deposit(null));
    }

    @Test
    void depositRejectsNonPositiveCountAndKeepsState() {
        Atm atm = new Atm();
        atm.deposit(Map.of(D100, 1));
        assertThrows(InvalidDepositException.class, () -> atm.deposit(Map.of(D100, 0)));
        assertEquals(100, atm.getBalance());
    }

    @Test
    void withdrawGreedyAndUpdatesBalance() {
        Atm atm = new Atm();
        // 2000
        atm.deposit(Map.of(D1000, 1, D500, 1, D100, 5));

        Map<Atm.Denomination, Integer> result = atm.withdraw(1700);

        assertEquals(Map.of(D1000, 1, D500, 1, D100, 2), result);
        assertEquals(300, atm.getBalance());
    }

    @Test
    void withdrawRejectsInvalidAmount() {
        Atm atm = new Atm();
        assertThrows(InvalidAmountException.class, () -> atm.withdraw(0));
        assertThrows(InvalidAmountException.class, () -> atm.withdraw(-50));
    }

    @Test
    void withdrawRejectsInsufficientFunds() {
        Atm atm = new Atm();
        atm.deposit(Map.of(D100, 2));
        assertThrows(InsufficientFundsException.class, () -> atm.withdraw(300));
    }

    @Test
    void withdrawRejectsUnmakeableAmountAndKeepsState() {
        Atm atm = new Atm();
        atm.deposit(Map.of(D500, 1, D100, 1));
        assertThrows(CannotDispenseException.class, () -> atm.withdraw(150));
        assertEquals(600, atm.getBalance());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("jsonCases")
    void additionalTests(AtmCase atmCase) {
        Atm atm = new Atm();

        for (Map<String, Integer> deposit : atmCase.deposits) {
            atm.deposit(toMap(deposit));
        }

        if (atmCase.expect.exception != null) {
            assertThrows(exceptionClass(atmCase.expect.exception),
                    () -> atm.withdraw(atmCase.withdraw),
                    "Case: " + atmCase.name);
            assertEquals(atmCase.expect.balance, atm.getBalance(), "Case: " + atmCase.name);
        } else {
            Map<Atm.Denomination, Integer> result = atm.withdraw(atmCase.withdraw);
            assertEquals(toMap(atmCase.expect.dispense), result, "Case: " + atmCase.name);
            assertEquals(atmCase.expect.balance, atm.getBalance(), "Case: " + atmCase.name);
        }
    }

    static Stream<AtmCase> jsonCases() throws IOException {
        List<AtmCase> cases = loadCases();
        return cases.stream();
    }

    private static List<AtmCase> loadCases() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = AtmTest.class.getClassLoader().getResourceAsStream("atm/atm_cases.json")) {
            if (input == null) {
                throw new IOException("Resource not found: atm/atm_cases.json");
            }
            return mapper.readValue(input, new TypeReference<>() {});
        }
    }

    private Map<Atm.Denomination, Integer> toMap(Map<String, Integer> source) {
        Map<Atm.Denomination, Integer> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            result.put(Atm.Denomination.fromInt(Integer.parseInt(entry.getKey())), entry.getValue());
        }
        return result;
    }

    private Class<? extends RuntimeException> exceptionClass(String name) {
        return switch (name) {
            case "InvalidAmountException" -> InvalidAmountException.class;
            case "InsufficientFundsException" -> InsufficientFundsException.class;
            case "CannotDispenseException" -> CannotDispenseException.class;
            case "InvalidDepositException" -> InvalidDepositException.class;
            default -> throw new IllegalArgumentException("Unknown exception: " + name);
        };
    }

    private static class AtmCase {
        public String name;
        public List<Map<String, Integer>> deposits = new ArrayList<>();
        public int withdraw;
        public Expect expect;

        @Override
        public String toString() {
            return "AtmCase{" +
                    "name='" + name + '\'' +
                    ", deposits=" + deposits +
                    ", withdraw=" + withdraw +
                    ", expect=" + expect +
                    '}';
        }
    }

    private static class Expect {
        public Map<String, Integer> dispense = new HashMap<>();
        public String exception;
        public int balance;

        @Override
        public String toString() {
            return "Expect{" +
                    "dispense=" + dispense +
                    ", exception='" + exception + '\'' +
                    ", balance=" + balance +
                    '}';
        }
    }
}
