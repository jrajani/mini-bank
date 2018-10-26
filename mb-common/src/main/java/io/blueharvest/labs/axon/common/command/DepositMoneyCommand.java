package io.blueharvest.labs.axon.common.command;

import lombok.*;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositMoneyCommand {

    @TargetAggregateIdentifier
    Integer accountId;

    @TargetAggregateIdentifier
    String transactionId;

    Integer amount;
}
