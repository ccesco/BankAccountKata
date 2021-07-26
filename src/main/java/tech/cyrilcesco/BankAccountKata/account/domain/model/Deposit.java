package tech.cyrilcesco.BankAccountKata.account.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder(toBuilder = true)
public class Deposit extends Operation {

    @Builder.Default
    @Setter(AccessLevel.NONE)
    OperationType operationType = OperationType.DEPOSIT;

}
