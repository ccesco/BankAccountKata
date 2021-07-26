package tech.cyrilcesco.BankAccountKata.account.domain.ports;

import org.springframework.stereotype.Service;
import tech.cyrilcesco.BankAccountKata.account.domain.model.Operation;

@Service
public class OperationApplicationService {

    private final OperationRepository operationRepository;

    public OperationApplicationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public Operation deposit(Operation operation) {
        return null;
    }
}
