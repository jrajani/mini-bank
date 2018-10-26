package io.blueharvest.labs.axon.queries.handler;

import io.blueharvest.labs.axon.common.command.CreateAccountCommand;
import io.blueharvest.labs.axon.common.event.AccountCreatedEvent;
import io.blueharvest.labs.axon.common.event.BalanceUpdatedEvent;
import io.blueharvest.labs.axon.common.model.AccountSummary;
import io.blueharvest.labs.axon.common.query.CountAccountSummaryQuery;
import io.blueharvest.labs.axon.common.query.CountAccountSummaryResponse;
import io.blueharvest.labs.axon.common.query.FindAccountSummaryQuery;
import io.blueharvest.labs.axon.common.query.FindAccountSummaryResponse;
import io.blueharvest.labs.axon.queries.repository.AccountSummaryRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class AccountEventHandler {

    @Autowired
    private AccountSummaryRepository repository;


    @EventHandler
    public void on(AccountCreatedEvent event) {
        repository.save(new AccountSummary(event.getAccountId(), event.getAccountHolderName(), 0));
    }

    @EventHandler
    public void on(BalanceUpdatedEvent event) {
        AccountSummary accountSummary = repository.findOne(event.getAccountId());
        accountSummary.setBalance(event.getBalance());
        repository.save(accountSummary);
    }

    @QueryHandler
    public FindAccountSummaryResponse handle(FindAccountSummaryQuery query) {
        List<AccountSummary> accountSummaryList = new ArrayList<>();
        Iterator<AccountSummary> iterator = repository.findAll(new PageRequest(query.getOffset(), query.getLimit())).iterator();
        iterator.forEachRemaining(accountSummaryList::add);
        return new FindAccountSummaryResponse(accountSummaryList);
    }

    @QueryHandler
    public CountAccountSummaryResponse handle(CountAccountSummaryQuery query) {
        return new CountAccountSummaryResponse(repository.findAll().size());
    }

}
