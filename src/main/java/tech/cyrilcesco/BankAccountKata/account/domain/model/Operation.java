package tech.cyrilcesco.BankAccountKata.account.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@FieldDefaults(level= AccessLevel.PRIVATE)
@SuperBuilder
@Getter
public class Operation {
    int id;
    LocalDate date;
    Account account;
    BigDecimal amount;
    BigDecimal accountBalanceAfterOperation;
    OperationType operationType;
}
