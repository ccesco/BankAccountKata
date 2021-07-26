package tech.cyrilcesco.BankAccountKata.account.adapter.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.cyrilcesco.BankAccountKata.account.adapters.repository.ListOperationRepository;
import tech.cyrilcesco.BankAccountKata.account.domain.model.*;
import tech.cyrilcesco.BankAccountKata.account.domain.ports.OperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ListOperationRepositoryTests {

    private static final int ID_DEPOSIT_1 = 1;
    private static final int ID_DEPOSIT_2 = 2;
    private static final int ID_DEPOSIT_3 = 3;
    private static final int ID_WITHDRAWAL_1 = 4;
    private static final int ID_WITHDRAWAL_2 = 5;
    private static final int ID_WITHDRAWAL_3 = 6;
    private static final int ID_COMPTE_1 = 1258;
    private static final int ID_COMPTE_2 = 987654;


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
            .account(Account.builder().id(ID_COMPTE_2).build())
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

    private static final Withdrawal WITHDRAWAL_TO_SAVE_1 = Withdrawal
            .builder()
            .id(ID_WITHDRAWAL_1)
            .account(Account.builder().id(ID_COMPTE_1).build())
            .date(LocalDate.of(2021, 6, 12))
            .amount(BigDecimal.valueOf(100))
            .accountBalanceAfterOperation(BigDecimal.valueOf(152))
            .build();

    private static final Withdrawal WITHDRAWAL_TO_SAVE_2 = Withdrawal
            .builder()
            .id(ID_WITHDRAWAL_2)
            .account(Account.builder().id(ID_COMPTE_2).build())
            .date(LocalDate.of(2020, 2, 1))
            .amount(BigDecimal.valueOf(150))
            .accountBalanceAfterOperation(BigDecimal.valueOf(450))
            .build();

    private static final Withdrawal WITHDRAWAL_TO_SAVE_3 = Withdrawal
            .builder()
            .id(ID_WITHDRAWAL_3)
            .account(Account.builder().id(ID_COMPTE_1).build())
            .date(LocalDate.of(2020, 11, 20))
            .amount(BigDecimal.valueOf(140))
            .accountBalanceAfterOperation(BigDecimal.valueOf(240))
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

    @Test
    void getAllOperationsTypeDepositEmpty() {
        assertEquals(
                operationRepository.getAllOperationsType(ID_COMPTE_1, OperationType.DEPOSIT),
                new ArrayList<>()
        );
    }

    @Test
    void getAllOperationsTypeDepositOneElement() {
        operationRepository.save(DEPOSIT_TO_SAVE);
        assertEquals(
                operationRepository.getAllOperationsType(ID_COMPTE_1, OperationType.DEPOSIT),
                Arrays.asList(DEPOSIT_TO_SAVE)
        );
    }

    @Test
    void getAllOperationsTypeDepositTwoElement() {
        operationRepository.save(DEPOSIT_TO_SAVE);
        operationRepository.save(DEPOSIT_TO_SAVE_3);
        assertEquals(
                operationRepository.getAllOperationsType(ID_COMPTE_1, OperationType.DEPOSIT),
                Arrays.asList(DEPOSIT_TO_SAVE, DEPOSIT_TO_SAVE_3)
        );
    }

    @Test
    void getAllOperationsTypeDepositDifferentAccount() {
        operationRepository.save(DEPOSIT_TO_SAVE);
        operationRepository.save(DEPOSIT_TO_SAVE_2);
        operationRepository.save(DEPOSIT_TO_SAVE_3);
        assertEquals(
                operationRepository.getAllOperationsType(ID_COMPTE_1, OperationType.DEPOSIT),
                Arrays.asList(DEPOSIT_TO_SAVE, DEPOSIT_TO_SAVE_3)
        );
    }

    @Test
    void makeWithdrawalInEmptyAccountList() {
        operationRepository.save(WITHDRAWAL_TO_SAVE_1);
        Operation operationToCheck = operationRepository.getOperation(ID_WITHDRAWAL_1).get();
        assertEquals(operationToCheck, WITHDRAWAL_TO_SAVE_1);
    }

    @Test
    void makeWithdrawalWithDifferentAccount() {
        operationRepository.save(WITHDRAWAL_TO_SAVE_1);
        operationRepository.save(WITHDRAWAL_TO_SAVE_2);
        Operation operationToCheck = operationRepository.getOperation(ID_WITHDRAWAL_1).get();
        assertEquals(operationToCheck, WITHDRAWAL_TO_SAVE_1);
    }

    @Test
    void makeWithdrawalWithSameAccount() {
        operationRepository.save(WITHDRAWAL_TO_SAVE_3);
        operationRepository.save(WITHDRAWAL_TO_SAVE_1);
        Operation operationToCheck = operationRepository.getOperation(ID_WITHDRAWAL_3).get();
        assertEquals(operationToCheck, WITHDRAWAL_TO_SAVE_3);
    }

    @Test
    void makeWithdrawalWithDeposit() {
        operationRepository.save(WITHDRAWAL_TO_SAVE_3);
        operationRepository.save(DEPOSIT_TO_SAVE_3);
        Operation withdrawalToCheck = operationRepository.getOperation(ID_WITHDRAWAL_3).get();
        assertEquals(withdrawalToCheck, WITHDRAWAL_TO_SAVE_3);
        Operation depositToCheck = operationRepository.getOperation(ID_DEPOSIT_3).get();
        assertEquals(depositToCheck, DEPOSIT_TO_SAVE_3);
    }
}
