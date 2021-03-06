package help.utils;

public interface ThrowingSupplier<T, E extends Throwable> {
    T get() throws E;
}
