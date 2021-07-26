package tech.cyrilcesco.BankAccountKata.account.ports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.cyrilcesco.BankAccountKata.account.adapters.repository.ListOperationRepository;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Account;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Deposit;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;
import tech.cyrilcesco.BankAccountKata.account.domain.ports.OperationApplicationService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OperationApplicationServiceTests {

    private static final int ID_DEPOSIT_1 = 1;
    private static final int ID_DEPOSIT_2 = 2;
    private static final int ID_DEPOSIT_3 = 3;
    private static final int ID_COMPTE_1 = 1258;


    private static final Deposit DEPOSIT_1 = Deposit
            .builder()
            .id(ID_DEPOSIT_1)
            .account(Account.builder().id(ID_COMPTE_1).build())
            .date(LocalDate.of(2021, 1, 1))
            .amount(BigDecimal.valueOf(200))
            .build();

    private static final Deposit DEPOSIT_2 = Deposit
            .builder()
            .id(ID_DEPOSIT_2)
            .account(Account.builder().id(987654).build())
            .date(LocalDate.of(2021, 2, 3))
            .amount(BigDecimal.valueOf(182))
            .build();

    private static final Deposit DEPOSIT_3 = Deposit
            .builder()
            .id(ID_DEPOSIT_3)
            .account(Account.builder().id(ID_COMPTE_1).build())
            .date(LocalDate.of(2021, 3, 2))
            .amount(BigDecimal.valueOf(80))
            .build();

    private OperationApplicationService operationApplicationService;

    @BeforeEach
    void initialiser() {
        operationApplicationService = new OperationApplicationService(new ListOperationRepository());
    }

    @Test
    void makeDepositInEmptyAccountList() {
        Operation result = operationApplicationService.deposit(DEPOSIT_1);
        // car la liste est vide donc l'account balance vide
        assertEquals(result.getAccountBalanceAfterOperation(),
                DEPOSIT_1.toBuilder().accountBalanceAfterOperation(DEPOSIT_1.getAmount()).build().getAccountBalanceAfterOperation());
    }

    @Test
    void makeDepositWithDifferentAccount() {
        operationApplicationService.deposit(DEPOSIT_2);
        Operation result = operationApplicationService.deposit(DEPOSIT_1);
        // car la liste est vide donc l'account balance vide
        assertEquals(result.getAccountBalanceAfterOperation(),
                DEPOSIT_1.toBuilder().accountBalanceAfterOperation(DEPOSIT_1.getAmount()).build().getAccountBalanceAfterOperation());
    }

    @Test
    void makeDepositWithTwoDepositSameAccount() {
        Operation tempResult = operationApplicationService.deposit(DEPOSIT_3);
        // car la liste est vide donc l'account balance vide
        assertEquals(tempResult.getAccountBalanceAfterOperation(),
                DEPOSIT_3.toBuilder().accountBalanceAfterOperation(DEPOSIT_3.getAmount()).build().getAccountBalanceAfterOperation());

        Operation finalResult = operationApplicationService.deposit(DEPOSIT_1);
        assertEquals(finalResult.getAccountBalanceAfterOperation(), DEPOSIT_1.toBuilder()
                .accountBalanceAfterOperation(DEPOSIT_3.getAmount().add(DEPOSIT_1.getAmount())).build().getAccountBalanceAfterOperation()
        );
    }
}
