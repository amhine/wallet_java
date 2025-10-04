package com.wallet.presentation;

import java.sql.Connection;
import java.util.Scanner;

import Controller.WalletController;
import Controller.MempoolController;
import com.wallet.service.WalletService;
import com.wallet.service.MempoolService;
import connexiondb.DbConnection;
import repository.TransactionRepository;
import repository.WalletRepository;
import persistence.WalletRepositoryImpl;
import persistence.TransactionRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            if (conn != null) {
                System.out.println("Connexion réussie ");

                WalletRepository walletRepository = new WalletRepositoryImpl();
                TransactionRepository transactionRepository = new TransactionRepositoryImpl();

                WalletService walletService = new WalletService(walletRepository, transactionRepository);
                MempoolService mempoolService = new MempoolService(transactionRepository);

                WalletController walletController = new WalletController(walletService);
                MempoolController mempoolController = new MempoolController(mempoolService);

                Menu menu = new Menu(new Scanner(System.in), walletController, mempoolController);
                menu.start();

            } else {
                System.out.println("Connexion échouée ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
