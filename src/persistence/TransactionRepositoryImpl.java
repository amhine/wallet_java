package persistence;


import com.wallet.metier.Transaction;

import enums.Priority;
import enums.Status;
import repository.TransactionRepository;
import connexiondb.DbConnection;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Logger logger = Logger.getLogger(TransactionRepositoryImpl.class.getName());

    @Override
    public void save(UUID walletId, Transaction transaction) {
    	String sql = "INSERT INTO transactions " +
    		    "(id, wallet_id, montant, source, destination, datee, frais, taille_bytes, priority, status) " +
    		    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::transaction_priority, ?::transaction_status)";


        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getId().toString());
            stmt.setString(2, walletId.toString());
            stmt.setDouble(3, transaction.getMontant());
            stmt.setString(4, transaction.getSource());
            stmt.setString(5, transaction.getDestination());
            stmt.setTimestamp(6, Timestamp.valueOf(transaction.getDate()));
            stmt.setDouble(7, transaction.getFrais());
            stmt.setInt(8, transaction.getTaille_bytes());
            stmt.setString(9, transaction.getPriority().name());
            stmt.setString(10, transaction.getStatus().name());

            stmt.executeUpdate();
            logger.info("Transaction saved successfully with id " + transaction.getId());

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Error saving transaction", e);
        }
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = new Transaction(
                            UUID.fromString(rs.getString("id")),
                            rs.getDouble("montant"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getTimestamp("datee").toLocalDateTime(),
                            rs.getDouble("frais"),
                            rs.getInt("taille_bytes"),
                            Priority.valueOf(rs.getString("priority")),
                            Status.valueOf(rs.getString("status"))
                    );
                    return Optional.of(transaction);
                }
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Error finding transaction by id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        UUID.fromString(rs.getString("id")),
                        rs.getDouble("montant"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getTimestamp("datee").toLocalDateTime(),
                        rs.getDouble("frais"),
                        rs.getInt("taille_bytes"),
                        Priority.valueOf(rs.getString("priority")),
                        Status.valueOf(rs.getString("status"))
                );
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Error finding all transactions", e);
        }

        return transactions;
    }
}
