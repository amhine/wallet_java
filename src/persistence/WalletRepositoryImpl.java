package persistence;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import com.wallet.metier.Wallet;
import com.wallet.metier.WalletBitcoin;
import com.wallet.metier.WalletEthereum;

import config.DatabaseConfig;
import config.LoggerConfig;
import repository.WalletRepository;

public class WalletRepositoryImpl implements WalletRepository {
    private static final Logger logger = LoggerConfig.getLogger(WalletRepositoryImpl.class);

    public WalletRepositoryImpl() {
        LoggerConfig.init();
    }

    @Override
    public void save(Wallet wallet) {
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
        	String sql = "INSERT INTO wallet (id, address, solde) VALUES (?, ?, ?) " +
                    "ON CONFLICT (id) DO UPDATE SET address = EXCLUDED.address, solde = EXCLUDED.solde";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, wallet.getId().toString());
                stmt.setString(2, wallet.getAdress());
                stmt.setDouble(3, wallet.getSolde());
                stmt.executeUpdate();
            }

            if (wallet instanceof WalletBitcoin) {
            	String btSql = "INSERT INTO walletbitcoin (wallet_id, satoshi_byte) VALUES (?, ?) " +
                        "ON CONFLICT (wallet_id) DO UPDATE SET satoshi_byte = EXCLUDED.satoshi_byte";

                try (PreparedStatement btsmt = connection.prepareStatement(btSql)) {
                    btsmt.setString(1, wallet.getId().toString());
                    btsmt.setDouble(2, ((WalletBitcoin) wallet).getSatoshi_byte());
                    btsmt.executeUpdate();
                }
            } else if (wallet instanceof WalletEthereum) {
            	String ethSql = "INSERT INTO walletethereum (wallet_id, prix_gas, limite_gas) VALUES (?, ?, ?) " +
                        "ON CONFLICT (wallet_id) DO UPDATE SET prix_gas = EXCLUDED.prix_gas, limite_gas = EXCLUDED.limite_gas";

                try (PreparedStatement ethStmt = connection.prepareStatement(ethSql)) {
                    ethStmt.setString(1, wallet.getId().toString());
                    ethStmt.setDouble(2, ((WalletEthereum) wallet).getPrix_gas());
                    ethStmt.setInt(3, ((WalletEthereum) wallet).getLimite_gas());
                    ethStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to save wallet", e);
        }
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        String sql = "SELECT w.id, w.address, w.solde, " +
                "b.satoshi_byte, e.prix_gas, e.limite_gas " +
                "FROM wallet w " +
                "LEFT JOIN walletbitcoin b ON w.id = b.wallet_id " +
                "LEFT JOIN walletethereum e ON w.id = e.wallet_id " +
                "WHERE w.id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            UUID walletId = UUID.fromString(rs.getString("id"));
            String address = rs.getString("address");
            double solde = rs.getDouble("solde");

            Wallet wallet = null;
            if (rs.getObject("satoshi_byte") != null) {
                wallet = new WalletBitcoin(walletId, address, solde, new HashSet<>());
            } else if (rs.getObject("prix_gas") != null) {
                WalletEthereum eth = new WalletEthereum(walletId, address, solde, new HashSet<>());
                eth.setPrix_gas(rs.getDouble("prix_gas"));
                eth.setLimite_gas(rs.getInt("limite_gas"));
                wallet = eth;
            }

            return Optional.ofNullable(wallet);

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to find wallet", e);
        }
    }

    @Override
    public Optional<Wallet> findWalletByAddress(String address) {
        String sql = "SELECT w.id, w.address, w.solde, " +
                "b.satoshi_byte, e.prix_gas, e.limite_gas " +
                "FROM Wallet w " +
                "LEFT JOIN WalletBitcoin b ON w.id = b.wallet_id " +
                "LEFT JOIN WalletEthereum e ON w.id = e.wallet_id " +
                "WHERE w.address = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            UUID walletId = UUID.fromString(rs.getString("id"));
            double solde = rs.getDouble("solde");

            Wallet wallet = null;
            if (rs.getObject("satoshi_byte") != null) {
                wallet = new WalletBitcoin(walletId, address, solde, new HashSet<>());
            } else if (rs.getObject("prix_gas") != null) {
                WalletEthereum eth = new WalletEthereum(walletId, address, solde, new HashSet<>());
                eth.setPrix_gas(rs.getDouble("prix_gas"));
                eth.setLimite_gas(rs.getInt("limite_gas"));
                wallet = eth;
            }

            return Optional.ofNullable(wallet);

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to find wallet by address", e);
        }
    }

    @Override
    public List<Wallet> findAll() {
        List<Wallet> wallets = new ArrayList<>();

        String sql = "SELECT w.id, w.address, w.solde, " +
                "b.satoshi_byte, e.prix_gas, e.limite_gas " +
                "FROM Wallet w " +
                "LEFT JOIN WalletBitcoin b ON w.id = b.wallet_id " +
                "LEFT JOIN WalletEthereum e ON w.id = e.wallet_id";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UUID walletId = UUID.fromString(rs.getString("id"));
                String address = rs.getString("address");
                double solde = rs.getDouble("solde");

                Wallet wallet = null;
                if (rs.getObject("satoshi_byte") != null) {
                    wallet = new WalletBitcoin(walletId, address, solde, new HashSet<>());
                } else if (rs.getObject("prix_gas") != null) {
                    WalletEthereum eth = new WalletEthereum(walletId, address, solde, new HashSet<>());
                    eth.setPrix_gas(rs.getDouble("prix_gas"));
                    eth.setLimite_gas(rs.getInt("limite_gas"));
                    wallet = eth;
                }

                if (wallet != null) {
                    wallets.add(wallet);
                }
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to fetch all wallets", e);
        }

        return wallets;
    }
}
