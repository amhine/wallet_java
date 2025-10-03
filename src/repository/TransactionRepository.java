package repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.wallet.metier.Transaction;

public interface TransactionRepository {
    void save(UUID walletId, Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findAll();
}
