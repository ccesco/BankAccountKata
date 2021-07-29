package tech.cyrilcesco.BankAccountKata.account.domain.ports;

import org.springframework.stereotype.Service;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class OperationApplicationService {

    private final OperationRepository operationRepository;

    public OperationApplicationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public Operation deposit(Operation operation) {
        return operationRepository.save(getOperationWithLastAccountBalanceAmountAdded(getLastOperation(operation), operation));
    }

    public Operation withdrawal(Operation operation) {
        return operationRepository.save(getOperationWithLastAccountBalanceAmountSubstract(getLastOperation(operation), operation));
    }

    public List<Operation> getOperationListOrdered(int accountId) {
        return null;
    }

    private Operation getOperationWithLastAccountBalanceAmountAdded(Operation lastOperation, Operation operationToAdd) {
        BigDecimal accountBalanceAfterOperation = operationToAdd.getAmount().add(lastOperation.getAccountBalanceAfterOperation());
        return operationToAdd.toBuilder().accountBalanceAfterOperation(accountBalanceAfterOperation).build();
    }

    private Operation getOperationWithLastAccountBalanceAmountSubstract(Operation lastOperation, Operation operationToSubsrtract) {
        BigDecimal accountBalanceAfterOperation = lastOperation.getAccountBalanceAfterOperation().subtract(operationToSubsrtract.getAmount());
        return operationToSubsrtract.toBuilder().accountBalanceAfterOperation(accountBalanceAfterOperation).build();
    }

    private Operation getLastOperation(Operation operation) {
        return
                operationRepository.getAllOperations(operation.getAccount().getId()).stream()
                        .filter(Objects::nonNull)
                        .max(Comparator.comparing(Operation::getDate))
                        .orElseGet(() -> Operation.builder().accountBalanceAfterOperation(BigDecimal.ZERO).build());
    }
}
