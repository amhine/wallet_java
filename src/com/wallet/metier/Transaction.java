package com.wallet.metier;

import java.util.UUID;
import java.time.LocalDateTime;

public class Transaction {

	private UUID id;
	private double montant;
	private String source;
	private String destination ;
	private LocalDateTime date;
	private double frais;
	private int taille_bytes;
	
	public Transaction (UUID id , double montant, String source,String destination,
			LocalDateTime date,double frais,int taille_bytes) {
		this.id = id != null ? id : UUID.randomUUID();
		this.montant=montant;
		this.source=source;
		this.destination=destination;
		this.date=(date != null) ? date : LocalDateTime.now();
		this.frais=frais;
		this.taille_bytes=taille_bytes;
		
	}
	
	public UUID getId() {
		return id;
	}
	
	public double getMontant() {
		return montant;
	}
	public String getSource() {
		return source;
	}
	public String getDestination() {
		return destination;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public double getFrais() {
		return frais;
	}
	public int getTaille_bytes() {
		return taille_bytes;
	}
	
	public void setId(UUID id) {
		this.id=id;
	}
	
	public void setMontant(double montant) {
		this.montant=montant;
	}
	
	public void setSource(String source) {
		this.source=source;
	}
	
	public void setDestination(String destination) {
		this.destination=destination;
	}
	
	public void setDate(LocalDateTime date) {
		this.date=date;
	}
	
	public void setFrais(double frais) {
		this.frais=frais;
	}
	
	public void setTaille_bytes(int taille_bytes) {
		this.taille_bytes=taille_bytes;
	}
	
}
