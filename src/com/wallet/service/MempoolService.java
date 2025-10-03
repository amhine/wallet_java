package com.wallet.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.wallet.metier.Transaction;
import com.wallet.metier.Wallet;

import config.LoggerConfig;
import enums.Priority;
import enums.Status;
import repository.TransactionRepository;

public class MempoolService {

	private final TransactionRepository transactionRepository;
    private final Random rand = new Random();
    private final static int BLOCK_TIME_MINS = 10;
    private static final Logger logger = LoggerConfig.getLogger(WalletService.class);
    
    public MempoolService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        LoggerConfig.init();
    }
    
    public List<Transaction> getPendingTransactions() {
        return transactionRepository.findAll().stream()
                .filter(tx -> tx.getStatus() == Status.PENDING)
                .sorted((t1, t2) -> Double.compare(t2.getFrais(), t1.getFrais()))
                .collect(Collectors.toList());
    }
    
    public OptionalInt getTransactionPosition(UUID txId) {
        List<Transaction> pending = getPendingTransactions();
        int position = 1;
        for (Transaction tx : pending) {
            if (tx.getId().equals(txId)) {
                logger.info("Transaction found in position "+position+" out of "+pending.size()+" in getTransactionPosition");
                return OptionalInt.of(position);
            }
            position++;
        }
        logger.info("Transaction not found in getTransactionPosition");
        return OptionalInt.empty();
    }
    
    public OptionalDouble estimateTime(UUID txId, int transactionsPerBlock) {
        List<Transaction> pending = getPendingTransactions();
        int position = 0;
        for (Transaction tx : pending) {
            position++;
            if (tx.getId().equals(txId)) {
                double estimatedBlocks = (double) position / transactionsPerBlock;
                return OptionalDouble.of(estimatedBlocks * BLOCK_TIME_MINS);
            }
        }
        logger.info("Transaction not found, in estimateTime");
        return OptionalDouble.empty();
    }
    
    public List<FeeComparisonResponseDTO> compareFeeLevels(Transaction tx, Wallet wallet, int txPerBlock) {
        List<FeeComparisonResponseDTO> result = new ArrayList<>();

        for (Priority priority : Priority.values()) {
            Transaction tempTx = new Transaction(
                    tx.getId(),
                    tx.getMontant(),  
                    tx.getSource(), 
                    tx.getDestination(),
                    null,
                    0.0,
                    tx.getTaille_bytes(),
                    priority,
                    Status.PENDING 
            );

            double fee = wallet.calculerFrais(tempTx);

            List<Transaction> pending = getPendingTransactions();
            int position = 1;
            for (Transaction t : pending) {
                if (t.getFrais() > fee) position++;
            }

            double estTime = ((double) position / txPerBlock) * BLOCK_TIME_MINS;

            result.add(new FeeComparisonResponseDTO(priority.name(), fee, position, estTime));
        }

        return result;
    }
    
    private String randomEthereumAddress() {
        String hex = UUID.randomUUID().toString().replace("-", "");
        hex += UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "0x" + hex;
    }
    private String randomBitcoinAddress() {
        String[] prefixes = {"1", "3", "bc1"};
        String prefix = prefixes[rand.nextInt(prefixes.length)];
        String body = UUID.randomUUID().toString().replace("-", "").substring(0, 25);
        return prefix + body;
    }
    
    public Transaction generateRandomTransaction() {
        UUID id = UUID.randomUUID();

        boolean isEth = rand.nextBoolean();
        String source = isEth ? randomEthereumAddress() : randomBitcoinAddress();
        String destination = isEth ? randomEthereumAddress() : randomBitcoinAddress();

        double montant = Math.round((rand.nextDouble() * 4.99 + 0.01) * 100.0) / 100.0;

        double minFrais = 0.00001;
        double maxFrais = 0.001;
        double frais= minFrais + (maxFrais - minFrais) * rand.nextDouble();
        frais = Math.round(frais * 100_000_000.0) / 100_000_000.0;

        Priority priority = Priority.values()[rand.nextInt(Priority.values().length)];
        Status status = Status.PENDING;
        LocalDateTime date = LocalDateTime.now();
        int taille_bytes = rand.nextInt(301) + 200;

        return new Transaction(id,montant,source,destination,date,frais,taille_bytes,priority,status);
    }
    
    public List<Transaction> getMempoolWithRandomTransactions(UUID myTxId) {
        int count = rand.nextInt(11) + 10;

        List<Transaction> mempool = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            mempool.add(generateRandomTransaction());
        }

        transactionRepository.findById(myTxId).ifPresent(mempool::add);

        mempool.sort((t1, t2) -> Double.compare(t2.getFrais(), t1.getFrais()));
        logger.info("Mempool found "+mempool.size()+" transactions");
        return mempool;
    }


    
}
