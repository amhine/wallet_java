package com.wallet.presentation;

import java.util.Scanner;
import java.util.UUID;

import Controller.MempoolController;
import Controller.WalletController;
import Dto.TransactionRequestDTO;
import Dto.TransactionResponseDTO;
import Dto.WalletRequestDTO;
import Dto.WalletResponseDTO;
import enums.Priority;
import Dto.MempoolRequestDTO;
import Dto.MempoolResponseDTO;


public class Menu {
    private final Scanner scanner;
    private final WalletController walletController;
    private final MempoolController mempoolController;

    public Menu(Scanner scanner, WalletController walletController, MempoolController mempoolController) {
        this.scanner = scanner;
        this.walletController = walletController;
        this.mempoolController = mempoolController;
    }

    public void start() {
        int choice = -1;
        do {
            printMenu();

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide, veuillez saisir un nombre.");
                continue;
            }

            switch (choice) {
                case 1:
                    createWallet();
                    break;
                case 2:
                    createTransaction();
                    break;
                case 3:
                    checkTransaction();
                    break;
                case 0:
                    System.out.println(" Au revoir !");
                    break;
                default:
                    System.out.println(" Choix invalide, réessayez.");
            }

        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Créer un wallet");
        System.out.println("2. Créer une transaction");
        System.out.println("3. Vérifier une transaction");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private void createWallet() {
        System.out.print("Entrez le type de wallet (BITCOIN / ETHEREUM): ");
        String type = scanner.nextLine().trim();

        WalletRequestDTO request = new WalletRequestDTO(type);
        WalletResponseDTO response = walletController.createWallet(request);

        System.out.println("\n Wallet créé !");
        System.out.println("ID      : " + response.getId());
        System.out.println("Type    : " + response.getType());
        System.out.println("Adresse : " + response.getAddress());
        System.out.println("Solde   : " + response.getSolde());
    }

    private void createTransaction() {
        System.out.print(" Adresse source: ");
        String source = scanner.nextLine().trim();

        System.out.print(" Adresse destination: ");
        String dest = scanner.nextLine().trim();

        System.out.print(" Montant: ");
        double montant = Double.parseDouble(scanner.nextLine());

        System.out.print("Taille (en bytes): ");
        int taille = Integer.parseInt(scanner.nextLine());

        System.out.print("Priorité (ECONOMIQUE / STANDARD / RAPIDE): ");
        String priorityInput = scanner.nextLine().trim().toUpperCase();

        Priority priority;
        try {
            priority = Priority.valueOf(priorityInput);
        } catch (Exception e) {
            System.out.println(" Priorité invalide, utilisation de STANDARD par défaut.");
            priority = Priority.STANDARD;
        }

        TransactionRequestDTO request = new TransactionRequestDTO(source, dest, montant, taille, priority);
        TransactionResponseDTO response = walletController.createTransaction(request);

        System.out.println("\n Transaction créée !");
        System.out.println("ID      : " + response.getId());
        System.out.println("De      : " + response.getSource());
        System.out.println("À       : " + response.getDestination());
        System.out.println("Montant : " + response.getMontant());
    }

    private void checkTransaction() {
        System.out.print(" Entrez l’ID de transaction : ");
        UUID txId = UUID.fromString(scanner.nextLine().trim());

        MempoolResponseDTO response = mempoolController.checkTransactionPosition(new MempoolRequestDTO(txId));
        if (response.isFound()) {
            System.out.println("Votre transaction est en position " + response.getPosition() +
                               " sur " + response.getTotal());
            System.out.printf("Temps estimé de confirmation : %.1f minutes%n", response.getDate());
        } else {
            System.out.println(" Transaction introuvable dans le mempool.");
        }
    }
}
