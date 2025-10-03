package com.wallet.metier;

import java.util.Set;
import java.util.UUID;

public class WalletBitcoin extends Wallet {

	private final static double satoshi_byte= 10.0;
	
	public WalletBitcoin(UUID id,String adress, double solde,Set<Transaction> transactions) {
		super(id, adress, solde, transactions); 
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
