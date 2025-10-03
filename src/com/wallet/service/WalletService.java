package com.wallet.service;

import java.util.UUID;
import java.util.logging.Logger;

import com.wallet.metier.Transaction;
import com.wallet.metier.Wallet;
import com.wallet.metier.WalletBitcoin;
import com.wallet.metier.WalletEthereum;
import com.wallet.utilitaire.TransactionValidator;
import com.wallet.utilitaire.WalletValidator;

import config.LoggerConfig;
import enums.Priority;
import repository.TransactionRepository;
import repository.WalletRepository;

public class WalletService {
	
	 private final WalletRepository walletRepository;
	    private final TransactionRepository transactionRepository;
	    private static final Logger logger = LoggerConfig.getLogger(WalletService.class);

	    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
	        this.walletRepository = walletRepository;
	        this.transactionRepository = transactionRepository;
	        LoggerConfig.init();
	    }

	    
	    public Wallet createWallet(String type) {
	        WalletValidator.validateWalletType(type);

	        UUID walletId = UUID.randomUUID();
	        String address;
	        Wallet wallet = null;

	        switch (type.trim().toLowerCase()) {
	            case "bitcoin":
	                address = "1" + UUID.randomUUID().toString().replace("-", "").substring(0, 25);
	                wallet = new WalletBitcoin(walletId, address, 0.0, null);
	                break;

	            case "ethereum":
	                String ethAddress = "0x" + java.util.UUID.randomUUID().toString()
	                        .replace("-", "")
	                        + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	                address = ethAddress.substring(0, 42);
	                wallet = new WalletEthereum(walletId, address, 0.0, null);
	                break;

	            default:
	                logger.severe("Unsupported wallet type: " + type);
	                throw new IllegalArgumentException("Unsupported wallet type: " + type);
	        }

	        walletRepository.save(wallet);
	        logger.info("Wallet created: " + wallet.getId());
	        return wallet;
	    }
	    public Transaction createTransaction(String source, String destination, double montant, Priority priority, int taille_bytes) {
	        TransactionValidator.validateAdresses(source, destination);
	        TransactionValidator.validateMontant(montant);

	        Wallet wallet = walletRepository.findWalletByAddress(source)
	                .orElseThrow(() -> new IllegalArgumentException("Wallet not found with this source address"));
	        
	        Transaction transaction = wallet.effectuerTransaction(destination, montant, priority, taille_bytes);
	        transactionRepository.save(wallet.getId(), transaction);
	        logger.info("Transaction created: " + transaction.getId());
	        return transaction;
	    }

}
