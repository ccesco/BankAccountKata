package tech.cyrilcesco.BankAccountKata.account.domain.ports;

import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;
import tech.cyrilcesco.BankAccountKata.account.domain.model.OperationType;

import java.util.List;
import java.util.Optional;

public interface OperationRepository {

    Operation save(Operation operation);

    Optional<Operation> getOperation(int operationId);

    List<Operation> getAllOperations(int accountNumber);

    List<Operation> getAllOperationsType(int accountNumber, OperationType operationType);

}
