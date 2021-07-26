package tech.cyrilcesco.BankAccountKata.account.domain.ports;

import org.springframework.stereotype.Service;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

@Service
public class OperationApplicationService {

    private final OperationRepository operationRepository;

    public OperationApplicationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public Operation deposit(Operation operation) {
        Operation lastOperationToGetAccountBalance =
                operationRepository.getAllOperations(operation.getAccount().getId()).stream()
                        .filter(Objects::nonNull)
                        .max(Comparator.comparing(Operation::getDate))
                        .orElseGet(() -> Operation.builder().accountBalanceAfterOperation(BigDecimal.ZERO).build());
        return operationRepository.save(getOperationWithLastAccountBalanceAmountAdded(lastOperationToGetAccountBalance, operation));
    }

    private Operation getOperationWithLastAccountBalanceAmountAdded(Operation lastOperation, Operation operationToAdd) {
        BigDecimal accountBalanceAfterOperation = operationToAdd.getAmount().add(lastOperation.getAccountBalanceAfterOperation());
        return operationToAdd.toBuilder().accountBalanceAfterOperation(accountBalanceAfterOperation).build();
    }
}
