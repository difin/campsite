package campsite.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class TransactionService {

    private final TransactionTemplate transactionTemplate;
    private final Scheduler scheduler;

    @Autowired
    public TransactionService(TransactionTemplate transactionTemplate,
                              Scheduler scheduler){
        this.transactionTemplate = transactionTemplate;
        this.scheduler = scheduler;
    }

    public Mono execTransaction(Transactional transactional){

        return Mono.defer(() -> transactionTemplate.execute(transactionStatus -> transactional.execute()))
                .subscribeOn(scheduler);
    }
}
