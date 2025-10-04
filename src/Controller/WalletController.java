package Controller;

import com.wallet.metier.Transaction;
import com.wallet.metier.Wallet;
import com.wallet.metier.WalletBitcoin;
import com.wallet.service.WalletService;

import Dto.TransactionRequestDTO;
import Dto.TransactionResponseDTO;
import Dto.WalletRequestDTO;
import Dto.WalletResponseDTO;

public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    public WalletResponseDTO createWallet(WalletRequestDTO requestDTO) {
        Wallet wallet = walletService.createWallet(requestDTO.getType());

        return new WalletResponseDTO(
                wallet.getId(),
                wallet instanceof WalletBitcoin ? "bitcoin" : "ethereum",
                wallet.getAdress(),
                wallet.getSolde()
        );
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO) {
        Transaction transaction = walletService.createTransaction(requestDTO.getSource(), requestDTO.getDestination(), requestDTO.getMontant(), requestDTO.getPriority(), requestDTO.getTaille_bytes());

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getSource(),
                transaction.getDestination(),
                transaction.getMontant(),
                transaction.getSource().startsWith("0x") ? "Ethereum" : "Bitcoin"
        );
    }
}