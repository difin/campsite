package campsite.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.function.Supplier;

@Service
public class ReactiveExecutionServiceImpl implements ReactiveExecutionService {

    private final TransactionTemplate transactionTemplate;
    private final Scheduler scheduler;

    @Autowired
    public ReactiveExecutionServiceImpl(TransactionTemplate transactionTemplate,
                                        Scheduler scheduler){
        this.transactionTemplate = transactionTemplate;
        this.scheduler = scheduler;
    }

    public <T> Mono<T> execTransaction(Supplier<T> executable){

         return Mono.defer(() -> transactionTemplate.execute(transactionStatus -> Mono.just(executable.get())))
                 .subscribeOn(scheduler);
    }

    public <T> Mono<T> exec(Supplier<T> executable){

        return Mono.defer(() -> Mono.just(executable.get()))
                .subscribeOn(scheduler);
    }
}
