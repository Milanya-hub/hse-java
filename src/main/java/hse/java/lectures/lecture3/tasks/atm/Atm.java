package hse.java.lectures.lecture3.tasks.atm;

import java.util.*;

public class Atm {
    public enum Denomination {
        D50(50),
        D100(100),
        D500(500),
        D1000(1000),
        D5000(5000);

        private final int value;

        Denomination(int value) {
            this.value = value;
        }

        int value() {
            return value;
        }

        public static Denomination fromInt(int value) {
            return Arrays.stream(values()).filter(v -> v.value == value)
                    .findFirst()
                    .orElse(null);
        }
    }

    private final Map<Denomination, Integer> banknotes = new EnumMap<>(Denomination.class);

    public Atm() {
    }

    public void deposit(Map<Denomination, Integer> banknotes) {
        if (banknotes == null || banknotes.isEmpty()) {
            throw new InvalidDepositException("Deposit is empty");
        }

        for (Map.Entry<Denomination, Integer> entry : banknotes.entrySet()) {
            if (entry.getKey() == null) {
                throw new InvalidDepositException("Invalid denomination");
            }
            if (entry.getValue() == null || entry.getValue() <= 0) {
                throw new InvalidDepositException("Invalid banknote count: " + entry.getValue());
            }
        }

        for (Map.Entry<Denomination, Integer> entry : banknotes.entrySet()) {
            this.banknotes.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    public Map<Denomination, Integer> withdraw(int amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
        int balance = getBalance();
        if (amount > balance) {
            throw new InsufficientFundsException("Not enough money in ATM");
        }

        Map<Denomination, Integer> temp = new EnumMap<>(banknotes);
        Map<Denomination, Integer> res = new EnumMap<>(Denomination.class);

        List<Denomination> denoms = new ArrayList<>(Arrays.asList(Denomination.values()));
        denoms.sort((a, b) -> Integer.compare(b.value(), a.value()));

        int ost = amount;

        for (Denomination denom : denoms) {
            int able = temp.getOrDefault(denom, 0);
            int need = ost / denom.value();
            int take = Math.min(able, need);

            if (take > 0) {
                res.put(denom, take);
                ost -= take * denom.value();
            }
        }
        if (ost != 0) {
            throw new CannotDispenseException("Not able to process");
        }
        for (Map.Entry<Denomination, Integer> entry : res.entrySet()) {
            banknotes.merge(entry.getKey(), -entry.getValue(), Integer::sum);
        }

        return res;
    }

    public int getBalance() {
        int sum = 0;
        for (Map.Entry<Denomination, Integer> entry : banknotes.entrySet()) {
            sum += entry.getKey().value() * entry.getValue();
        }
        return sum;
    }

}
