package com.wallet.metier;

import java.util.Set;
import java.util.UUID;

public class WalletEthereum extends Wallet{

	private double prix_gas=50;
	private int limite_gas=21000;
	
	public WalletEthereum(UUID id,String adress, double solde,Set<Transaction> transactions) {
		super(id, adress, solde, transactions); 
		
	}
	
	public double getPrix_gas() {
		return prix_gas;
	}
	public int getLimite_gas() {
		return limite_gas;
	}
	public void setPrix_gas(double prix_gas) {
		this.prix_gas=prix_gas;
	}
	public void setLimite_gas(int limite_gas) {
		this.limite_gas=limite_gas;
	}
	@Override
	public double calculerFrais(Transaction transaction) {
		 double gasPrice;

	        switch (transaction.getPriority()) {
	            case ECONOMIQUE:
	                gasPrice = prix_gas * 0.5;
	                break;
	            case STANDARD:
	                gasPrice = prix_gas;
	                break;
	            case RAPIDE:
	                gasPrice = prix_gas * 2;
	                break;
	            default:
	                gasPrice = prix_gas;
	        }
	        return limite_gas * gasPrice * 1e-9;
		}
}
