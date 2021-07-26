package tech.cyrilcesco.BankAccountKata.account.adapters.repository;

import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;
import tech.cyrilcesco.BankAccountKata.account.domain.model.OperationType;
import tech.cyrilcesco.BankAccountKata.account.domain.ports.OperationRepository;

import java.util.List;
import java.util.Optional;

public class ListOperationRepository implements OperationRepository {

    @Override
    public Operation save(Operation operation) {
        return null;
    }

    @Override
    public Optional<Operation> getOperation(int operationId) {
        return null;
    }

    @Override
    public List<Operation> getAllOperations(int accountNumber) {
        return null;
    }

    @Override
    public List<Operation> getAllOperationsType(int accountNumber, OperationType operationType) {
        return null;
    }
}
