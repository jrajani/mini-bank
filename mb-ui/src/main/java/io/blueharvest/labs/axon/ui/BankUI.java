package io.blueharvest.labs.axon.ui;

import com.vaadin.annotations.Push;
import com.vaadin.server.ClassResource;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import io.blueharvest.labs.axon.common.command.CreateAccountCommand;
import io.blueharvest.labs.axon.common.command.DepositMoneyCommand;
import io.blueharvest.labs.axon.common.command.WithdrawMoneyCommand;
import io.blueharvest.labs.axon.common.model.AccountSummary;
import io.blueharvest.labs.axon.common.model.TransactionHistory;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;

import java.util.UUID;

@SpringUI
@Push
public class BankUI extends UI {

    private final QueryGateway queryGateway;

    private final CommandGateway commandGateway;

    private AccountSummaryDataProvider accountSummaryDataProvider;
    private TransactionHistoryDataProvider transactionHistoryDataProvider;

    public BankUI(QueryGateway queryGateway, CommandGateway commandGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(addAccountPanel(),depositMoneyPanel(), withdrawMoneyPanel());
        horizontalLayout.setSizeFull();

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.addComponents(accountSummaryGrid(), transactionHistoryGrid());
        horizontalLayout2.setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponents(horizontalLayout, horizontalLayout2);

        setContent(verticalLayout);
        setSizeFull();


        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Throwable cause = event.getThrowable();
                while(cause.getCause() != null) cause = cause.getCause();
                Notification.show("Error", cause.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
    }

    private Component depositMoneyPanel() {
        TextField id = new TextField("Account Id");
        TextField noOfStudents = new TextField("Amount");
        Button btnDeposit = new Button("Deposit");

        btnDeposit.addClickListener(evt -> {
            commandGateway.sendAndWait(new DepositMoneyCommand(Integer.parseInt(id.getValue()), UUID.randomUUID().toString(), Integer.parseInt(noOfStudents.getValue())));
            Notification.show("Success", Notification.Type.HUMANIZED_MESSAGE)
                    .addCloseListener(e -> accountSummaryDataProvider.refreshAll());
        });

        FormLayout form = new FormLayout();
        form.addComponents(id, noOfStudents, btnDeposit);
        form.setMargin(true);

        Panel panel = new Panel("Deposit Money");
        panel.setContent(form);
        return panel;
    }

    private Component withdrawMoneyPanel() {
        TextField id = new TextField("Account Id");
        TextField noOfStudents = new TextField("Amount");
        Button btnDeposit = new Button("Withdraw");

        btnDeposit.addClickListener(evt -> {
            commandGateway.sendAndWait(new WithdrawMoneyCommand(Integer.parseInt(id.getValue()), UUID.randomUUID().toString(), Integer.parseInt(noOfStudents.getValue())));
            Notification.show("Success", Notification.Type.HUMANIZED_MESSAGE)
                    .addCloseListener(e -> accountSummaryDataProvider.refreshAll());
        });

        FormLayout form = new FormLayout();
        form.addComponents(id, noOfStudents, btnDeposit);
        form.setMargin(true);

        Panel panel = new Panel("Withdraw Money");
        panel.setContent(form);
        return panel;
    }

    private Component addAccountPanel() {
        TextField id = new TextField("ID");
        TextField name = new TextField("Name");
        Button btnCreate = new Button("Create");

        btnCreate.addClickListener(evt -> {
            commandGateway.sendAndWait(new CreateAccountCommand(Integer.parseInt(id.getValue()), name.getValue()));
            Notification.show("Success", Notification.Type.HUMANIZED_MESSAGE)
                    .addCloseListener(e -> accountSummaryDataProvider.refreshAll());
        });

        FormLayout form = new FormLayout();
        form.addComponents(id, name, btnCreate);
        form.setMargin(true);

        Panel panel = new Panel("Create new account");
        panel.setContent(form);
        return panel;
    }

    private Grid accountSummaryGrid() {
        accountSummaryDataProvider = new AccountSummaryDataProvider(queryGateway);
        Grid<AccountSummary> grid = new Grid<>();
        grid.addColumn(AccountSummary::getId).setCaption("Acoount ID");
        grid.addColumn(AccountSummary::getName).setCaption("Holder Name");
        grid.addColumn(AccountSummary::getBalance).setCaption("Balance");
        grid.setSizeFull();
        grid.setDataProvider(accountSummaryDataProvider);
        return grid;
    }

    private Grid transactionHistoryGrid() {
        transactionHistoryDataProvider = new TransactionHistoryDataProvider(queryGateway);
        Grid<TransactionHistory> grid = new Grid<>();
        grid.addColumn(TransactionHistory::getAccountId).setCaption("Acoount ID");
        grid.addColumn(TransactionHistory::getTransactionId).setCaption("Transaction Id");
        grid.addColumn(TransactionHistory::getAmount).setCaption("Amount");
        grid.setSizeFull();
        grid.setDataProvider(transactionHistoryDataProvider);
        return grid;
    }
}
