package io.blueharvest.labs.axon.common.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawMoneyCommand {

    @TargetAggregateIdentifier
    Integer accountId;

    @TargetAggregateIdentifier
    String transactionId;

    Integer amount;
}
