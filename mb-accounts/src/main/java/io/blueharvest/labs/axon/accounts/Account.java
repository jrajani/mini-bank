package io.blueharvest.labs.axon.accounts;

import io.blueharvest.labs.axon.common.command.CreateAccountCommand;
import io.blueharvest.labs.axon.common.command.DepositMoneyCommand;
import io.blueharvest.labs.axon.common.command.WithdrawMoneyCommand;
import io.blueharvest.labs.axon.common.event.AccountCreatedEvent;
import io.blueharvest.labs.axon.common.event.BalanceUpdatedEvent;
import io.blueharvest.labs.axon.common.event.MoneyDepositedEvent;
import io.blueharvest.labs.axon.common.event.MoneyWithdrawnEvent;
import io.blueharvest.labs.axon.common.exception.InsufficientBalanceException;
import io.blueharvest.labs.axon.common.exception.InvalidAmountException;
import io.blueharvest.labs.axon.common.exception.InvalidDetailsException;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.StringUtils;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
public class Account {

    @AggregateIdentifier
    private Integer accountId;
    private Integer balance;
    private String accountHolderName;

    @CommandHandler
    public Account(CreateAccountCommand cmd) throws InvalidDetailsException {
        if(cmd.getAccountId() < 0 || StringUtils.isEmpty(cmd.getAccountHolderName())) {
            throw new InvalidDetailsException("Account Id or Name is incorrect.");
        }
        apply(new AccountCreatedEvent(cmd.getAccountId(), cmd.getAccountHolderName()));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand cmd) throws InsufficientBalanceException {
        if (balance < cmd.getAmount()) {
            throw new InsufficientBalanceException("Balance is too low");
        }
        apply(new MoneyWithdrawnEvent(accountId, cmd.getTransactionId(), cmd.getAmount(), balance - cmd.getAmount()));
    }

    @CommandHandler
    public void handle(DepositMoneyCommand cmd) throws InvalidAmountException {
        if (cmd.getAmount() < 0) {
            throw new InvalidAmountException("Invalid Amount.");
        }
        apply(new MoneyDepositedEvent(cmd.getAccountId(), cmd.getTransactionId(), cmd.getAmount(), balance + cmd.getAmount()));
    }

    @EventSourcingHandler
    protected void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.accountHolderName = event.getAccountHolderName();
        this.balance = 0;
    }

    @EventSourcingHandler
    protected void on(BalanceUpdatedEvent event) {
        this.accountId = event.getAccountId();
        this.balance = event.getBalance();
    }
}

