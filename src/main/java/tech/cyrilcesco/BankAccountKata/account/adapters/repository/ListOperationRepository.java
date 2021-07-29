package tech.cyrilcesco.BankAccountKata.account.adapters.repository;

import org.springframework.stereotype.Service;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;
import tech.cyrilcesco.BankAccountKata.account.domain.model.OperationType;
import tech.cyrilcesco.BankAccountKata.account.domain.ports.OperationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListOperationRepository implements OperationRepository {

    private List<Operation> operations = new ArrayList<>();

    @Override
    public Operation save(Operation operation) {
        operations.add(operation);
        return operation;
    }

    @Override
    public Optional<Operation> getOperation(int operationId) {
        return operations.stream().filter((Operation operation) -> operation.getId() == operationId).findFirst();
    }

    @Override
    public List<Operation> getAllOperations(int accountNumber) {
        return getAllOperationForAccountId(accountNumber);
    }

    @Override
    public List<Operation> getAllOperationsType(int accountNumber, OperationType operationType) {
        return getAllOperationForAccountId(accountNumber).stream()
                .filter((Operation operation) -> operation.getOperationType() == operationType)
                .collect(Collectors.toList());
    }

    private List<Operation> getAllOperationForAccountId(int accountNumber) {
        return operations.stream()
                .filter((Operation operation) -> accountNumber == operation.getAccount().getId())
                .collect(Collectors.toList());
    }
}
