package hse.java.lectures.lesson7.limiter;

import java.time.temporal.ChronoUnit;

/**
 * Скользящий рейтлимитер: не больше заданного числа успешных {@link #check()} за последнюю секунду или минуту.
 */
public class RateLimiter {

    /**
     * @param unit        длина окна — только {@link ChronoUnit#SECONDS} или {@link ChronoUnit#MINUTES}
     *                    (скользящее окно 1 секунда или 1 минута)
     * @param maxRequests максимум успешных {@link #check()} за окно (должно быть > 0)
     */
    public RateLimiter(ChronoUnit unit, int maxRequests) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Регистрирует попытку и возвращает, разрешена ли она в пределах лимита.
     */
    public boolean check() {
        throw new UnsupportedOperationException("Not implemented");
    }

}
