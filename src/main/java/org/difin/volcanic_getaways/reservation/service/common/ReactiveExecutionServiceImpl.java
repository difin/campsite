package org.difin.volcanic_getaways.reservation.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.function.Supplier;

@Service
public class ReactiveExecutionServiceImpl implements ReactiveExecutionService {

    private TransactionTemplate transactionTemplate;
    private Scheduler scheduler;

    @Autowired
    public ReactiveExecutionServiceImpl(TransactionTemplate transactionTemplate,
                                        Scheduler scheduler){
        this.transactionTemplate = transactionTemplate;
        this.scheduler = scheduler;

        transactionTemplate.setTimeout(2);
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
