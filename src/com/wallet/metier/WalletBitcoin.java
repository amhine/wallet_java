package com.wallet.metier;

import java.util.HashSet;
import java.util.UUID;

public class WalletBitcoin extends Wallet {

	private final static double satoshi_byte= 10.0;
	
	public WalletBitcoin(String adress, double solde,double satoshi_byte) {
		super(UUID.randomUUID(), adress, solde, new HashSet<Transaction>()); 
	}
	
	public double getSatoshi_byte() {
		return satoshi_byte;
	}
	
	@Override
	public double calculerFrais(Transaction transaction) {
		double multiplier;
        switch (transaction.getPriority()) {
            case ECONOMIQUE: multiplier = 0.5; break;
            case STANDARD:   multiplier = 1.0; break;
            case RAPIDE:     multiplier = 2.0; break;
            default:         multiplier = 1.0;
        }
        return transaction.getTaille_bytes()*satoshi_byte*multiplier/100_000_000.0;
	 
	}
}
