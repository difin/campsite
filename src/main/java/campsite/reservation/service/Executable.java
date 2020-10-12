package campsite.reservation.service;

@FunctionalInterface
public interface Executable<T> {

    Object execute();
}
