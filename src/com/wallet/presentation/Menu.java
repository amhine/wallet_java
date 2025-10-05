package com.wallet.presentation;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import Controller.MempoolController;
import Controller.WalletController;
import Dto.MempoolRequestDTO;
import Dto.MempoolResponseDTO;
import Dto.MempoolTransactionDTO;
import Dto.TransactionRequestDTO;
import Dto.TransactionResponseDTO;
import Dto.WalletRequestDTO;
import Dto.WalletResponseDTO;
import enums.Priority;

public class Menu {
    private final Scanner scanner;
    private final WalletController walletController;
    private final MempoolController mempoolController;
    private UUID lastTransactionId = null;

    public Menu(Scanner scanner, WalletController walletController, MempoolController mempoolController) {
        this.scanner = scanner;
        this.walletController = walletController;
        this.mempoolController = mempoolController;
    }

    public void start() {
        int choice = -1;

        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Créer un wallet");
            System.out.println("2. Créer une transaction");
            System.out.println("3. Vérifier une transaction");
            System.out.println("4. Comparer les frais");
            System.out.println("5. Voir l'état du mempool");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez saisir un nombre.");
                continue;
            }

            switch (choice) { 
            case 1: createWallet(); 
            break; 
            case 2: createTransaction(); 
            break;
            case 3: checkTransactionPosition();
            break; 
            case 4: compareFeeLevels();
            break; 
            case 5: displayMempoolState(); 
            break; 
            case 0: System.out.println("\n👋 Merci d'avoir utilisé Crypto Wallet Simulator. Au revoir !"); 
            break;
            default: System.out.println("❌ Choix invalide, réessayez."); 
            }
            }
        while (choice != 0);
        }

    private void createWallet() {
        System.out.println("\n=== CRÉATION D'UN WALLET ===");
        System.out.print("Entrez le type de wallet (BITCOIN / ETHEREUM): ");
        String type = scanner.nextLine().trim().toUpperCase();

        try {
            WalletRequestDTO request = new WalletRequestDTO(type);
            WalletResponseDTO response = walletController.createWallet(request);

            System.out.println("Wallet créé avec succès !");
            System.out.println("ID       : " + response.getId());
            System.out.println("Type     : " + response.getType());
            System.out.println("Adresse  : " + response.getAddress());
            System.out.println("Solde    : " + response.getSolde());
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void createTransaction() {
        System.out.println("\n=== CRÉATION D'UNE TRANSACTION ===");

        try {
            System.out.print("Adresse source : ");
            String source = scanner.nextLine().trim();

            System.out.print("Adresse destination : ");
            String dest = scanner.nextLine().trim();

            System.out.print("Montant : ");
            double montant = Double.parseDouble(scanner.nextLine());

            System.out.print("Taille (en bytes) : ");
            int taille = Integer.parseInt(scanner.nextLine());

            System.out.print("Priorité (ECONOMIQUE / STANDARD / RAPIDE) : ");
            String priorityInput = scanner.nextLine().trim().toUpperCase();

            Priority priority;
            try {
                priority = Priority.valueOf(priorityInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Priorité invalide, utilisation de STANDARD par défaut.");
                priority = Priority.STANDARD;
            }

            TransactionRequestDTO request = new TransactionRequestDTO(source, dest, montant, taille, priority);
            TransactionResponseDTO response = walletController.createTransaction(request);
            lastTransactionId = response.getId();

            System.out.println("Transaction créée avec succès !");
            System.out.println("ID          : " + response.getId());
            System.out.println("Source      : " + response.getSource());
            System.out.println("Destination : " + response.getDestination());
            System.out.println("Montant     : " + response.getMontant());
            System.out.println("Priorité    : " + priority);

        } catch (NumberFormatException e) {
            System.out.println("Erreur : Montant ou taille invalide.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void checkTransactionPosition() {
        System.out.println("\n=== VÉRIFICATION D'UNE TRANSACTION ===");

        try {
            UUID txId;
            if (lastTransactionId != null) {
                System.out.println("Dernière transaction : " + lastTransactionId);
                System.out.print("Utiliser cette transaction ? (O/N) : ");
                String useLast = scanner.nextLine().trim().toUpperCase();
                txId = (useLast.equals("O") || useLast.equals("OUI")) ? lastTransactionId
                        : UUID.fromString(scanner.nextLine().trim());
            } else {
                System.out.print("Entrez l'ID de transaction : ");
                txId = UUID.fromString(scanner.nextLine().trim());
            }

            MempoolResponseDTO response = mempoolController.checkTransactionPosition(new MempoolRequestDTO(txId));

            if (response.isFound()) {
                System.out.println("Transaction trouvée !");
                System.out.println("Position : " + response.getPosition() + "/" + response.getTotal());
                System.out.printf("Temps estimé : %.1f minutes%n", response.getDate());
            } else {
                System.out.println("Transaction introuvable.");
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void compareFeeLevels() {
        System.out.println("\n=== COMPARAISON DES FRAIS ===");
        System.out.println("Fonctionnalité en cours de développement...");
    }

    private void displayMempoolState() {
        System.out.println("\n=== ÉTAT DU MEMPOOL ===");
        try {
            UUID highlight = lastTransactionId != null ? lastTransactionId : UUID.randomUUID();
            List<MempoolTransactionDTO> mempool = mempoolController.getMempool(highlight);

            System.out.println("Nombre de transactions en attente : " + mempool.size());
            for (MempoolTransactionDTO tx : mempool) {
                String id = tx.getId().toString();
                System.out.println("- " + id + " | Frais : " + tx.getFrais());
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
