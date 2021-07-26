package tech.cyrilcesco.BankAccountKata.account.adapter.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.cyrilcesco.BankAccountKata.account.adapters.repository.ListOperationRepository;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Account;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Deposit;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;
import tech.cyrilcesco.BankAccountKata.account.domain.ports.OperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ListOperationRepositoryTests {

    private static final int ID_DEPOSIT_1 = 1;
    private static final int ID_DEPOSIT_2 = 2;
    private static final int ID_DEPOSIT_3 = 3;
    private static final int ID_COMPTE_1 = 1258;


    private static final Deposit DEPOSIT_TO_SAVE = Deposit
            .builder()
            .id(ID_DEPOSIT_1)
            .account(Account.builder().id(ID_COMPTE_1).build())
            .date(LocalDate.of(2021, 1, 1))
            .amount(BigDecimal.valueOf(200))
            .accountBalanceAfterOperation(BigDecimal.valueOf(300))
            .build();

    private static final Deposit DEPOSIT_TO_SAVE_2 = Deposit
            .builder()
            .id(ID_DEPOSIT_2)
            .account(Account.builder().id(987654).build())
            .date(LocalDate.of(2021, 2, 3))
            .amount(BigDecimal.valueOf(182))
            .accountBalanceAfterOperation(BigDecimal.valueOf(182))
            .build();

    private static final Deposit DEPOSIT_TO_SAVE_3 = Deposit
            .builder()
            .id(ID_DEPOSIT_3)
            .account(Account.builder().id(ID_COMPTE_1).build())
            .date(LocalDate.of(2021, 3, 2))
            .amount(BigDecimal.valueOf(80))
            .accountBalanceAfterOperation(BigDecimal.valueOf(100))
            .build();

    private OperationRepository operationRepository;

    @BeforeEach
    void initialiser() {
        operationRepository = new ListOperationRepository();
    }

    @Test
    void makeDepositInEmptyAccountList() {
        operationRepository.save(DEPOSIT_TO_SAVE);
        Operation operationToCheck = operationRepository.getOperation(ID_DEPOSIT_1).get();
        assertEquals(operationToCheck, DEPOSIT_TO_SAVE);
    }

    @Test
    void makeDepositInAccountListWithoutAccountID() {
        operationRepository.save(DEPOSIT_TO_SAVE_2);
        operationRepository.save(DEPOSIT_TO_SAVE);
        Operation operationToCheck = operationRepository.getOperation(ID_DEPOSIT_1).get();
        assertEquals(operationToCheck, DEPOSIT_TO_SAVE);
    }

    @Test
    void makeDepositInAccountListWithAccountID() {
        operationRepository.save(DEPOSIT_TO_SAVE_3);
        operationRepository.save(DEPOSIT_TO_SAVE);
        Operation operationToCheck = operationRepository.getOperation(ID_DEPOSIT_1).get();
        assertEquals(operationToCheck, DEPOSIT_TO_SAVE);
        assertEquals(operationToCheck.getAccountBalanceAfterOperation(),
                DEPOSIT_TO_SAVE_3.getAccountBalanceAfterOperation().add(DEPOSIT_TO_SAVE.getAmount()));
    }
}
