package com.wallet.utilitaire;

public class TransactionValidator {

	 public static void validateAdresses(String source, String destination) {
	        if(source == null || destination == null || source.trim().isEmpty() || destination.trim().isEmpty()) {
	            throw new IllegalArgumentException("Sender address and Receiver address cannot be null");
	        }

	        if(source.equalsIgnoreCase(destination)) {
	            throw new IllegalArgumentException("Sender address and Receiver address cannot be the same");
	        }
	        String senderType = source.startsWith("0x") ? "Ethereum" : "Bitcoin";
	        String receiverType = destination.startsWith("0x") ? "Ethereum" : "Bitcoin";

	        if(!senderType.equalsIgnoreCase(receiverType)) {
	            throw new IllegalArgumentException("Sender address and Receiver address must be of same type");
	        }
	    }
	 
	 
	 public static void validateMontant(double montant) {
	        if(montant<= 0) {
	            throw new IllegalArgumentException("Amount cannot be negative");
	        }
	    }

}
