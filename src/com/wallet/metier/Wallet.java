package com.wallet.metier;

  import java.util.Set;
  import java.util.UUID;

public abstract class Wallet {
	
	private UUID id;
	private String adress;
	private double solde;
	private Set<Transaction> transactions;
	
	public Wallet(UUID id,String adress,Double solde, Set<Transaction> transactions) {
		this.id= id != null ? id:UUID.randomUUID();
		this.adress=adress;
		this.solde=solde;
		this.transactions=transactions;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getAdress() {
		return adress;
	}
	
	public double getSolde() {
		return solde;
	}
	
	public Set<Transaction> getTransactions(){
		return transactions;
	}
	
	public void setId(UUID id) {
		this.id=id;
	}
	
	public void setAdress(String adress) {
		this.adress=adress;
	}
	
	public void setSolde(double solde) {
		this.solde=solde;
	}
	public void setTransaction(Set<Transaction> transactions) {
		this.transactions=transactions;
	}

}
