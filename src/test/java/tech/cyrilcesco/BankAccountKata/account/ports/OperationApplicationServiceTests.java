package tech.cyrilcesco.BankAccountKata.account.ports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.cyrilcesco.BankAccountKata.account.adapters.repository.ListOperationRepository;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Account;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Deposit;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Withdrawal;
import tech.cyrilcesco.BankAccountKata.account.domain.ports.OperationApplicationService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationApplicationServiceTests {

    private static final int ID_DEPOSIT_1 = 1;
    private static final int ID_DEPOSIT_2 = 2;
    private static final int ID_DEPOSIT_3 = 3;

    private static final int ID_COMPTE_1 = 1258;
    private static final int ID_COMPTE_2 = 987654;

    private static final int ID_WITHDRAWAL_1 = 4;
    private static final int ID_WITHDRAWAL_2 = 5;
    private static final int ID_WITHDRAWAL_3 = 6;


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
            .account(Account.builder().id(ID_COMPTE_2).build())
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

    private static final Withdrawal WITHDRAWAL_TO_SAVE_1 = Withdrawal
            .builder()
            .id(ID_WITHDRAWAL_1)
            .account(Account.builder().id(ID_COMPTE_1).build())
            .date(LocalDate.of(2021, 6, 12))
            .amount(BigDecimal.valueOf(100))
            .build();

    private static final Withdrawal WITHDRAWAL_TO_SAVE_2 = Withdrawal
            .builder()
            .id(ID_WITHDRAWAL_2)
            .account(Account.builder().id(ID_COMPTE_2).build())
            .date(LocalDate.of(2020, 2, 1))
            .amount(BigDecimal.valueOf(150))
            .build();

    private static final Withdrawal WITHDRAWAL_TO_SAVE_3 = Withdrawal
            .builder()
            .id(ID_WITHDRAWAL_3)
            .account(Account.builder().id(ID_COMPTE_1).build())
            .date(LocalDate.of(2020, 11, 20))
            .amount(BigDecimal.valueOf(140))
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

    @Test
    void makeWithdrawalInEmptyAccountList() {
        Operation result = operationApplicationService.withdrawal(WITHDRAWAL_TO_SAVE_1); // 0 - 100 = -100
        // car la liste est vide donc l'account balance vide
        assertEquals(result.getAccountBalanceAfterOperation(), BigDecimal.valueOf(-100));
    }

    @Test
    void makeWithdrawalWithDifferentAccount() {
        operationApplicationService.deposit(WITHDRAWAL_TO_SAVE_2);
        Operation result = operationApplicationService.withdrawal(WITHDRAWAL_TO_SAVE_1); // 0 - 100 = - 100
        // car la liste est vide donc l'account balance vide
        assertEquals(result.getAccountBalanceAfterOperation(), BigDecimal.valueOf(-100));
    }

    @Test
    void makeWithdrawalWithTwoDepositSameAccount() {
        Operation tempResult = operationApplicationService.withdrawal(WITHDRAWAL_TO_SAVE_3); // 0 - 140 = -140
        // car la liste est vide donc l'account balance vide
        assertEquals(tempResult.getAccountBalanceAfterOperation(), BigDecimal.valueOf(-140));

        Operation finalResult = operationApplicationService.withdrawal(WITHDRAWAL_TO_SAVE_1); // -140 - 100 = -240
        assertEquals(finalResult.getAccountBalanceAfterOperation(), BigDecimal.valueOf(-240));
    }
}
