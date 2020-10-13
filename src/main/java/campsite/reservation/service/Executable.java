package campsite.reservation.service;

@FunctionalInterface
public interface Executable<T> {

    T execute();
}
