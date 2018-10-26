package io.blueharvest.labs.axon.common.command;

import lombok.*;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountCommand {

    @TargetAggregateIdentifier
    Integer accountId;

    String accountHolderName;
}
