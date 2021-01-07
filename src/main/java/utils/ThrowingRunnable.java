package utils;

/**
 * Runnable that may throw checked throwable
 * @param <E> throwable type
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {
    void run() throws E;
}
