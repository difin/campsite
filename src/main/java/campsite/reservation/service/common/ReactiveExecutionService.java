package campsite.reservation.service.common;

import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public interface ReactiveExecutionService {

    <T> Mono<T> execTransaction(Supplier<T> executable);
    <T> Mono<T> exec(Supplier<T> executable);
}
