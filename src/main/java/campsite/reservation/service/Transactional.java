package campsite.reservation.service;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface Transactional<T> {

    Mono<T> execute();
}
