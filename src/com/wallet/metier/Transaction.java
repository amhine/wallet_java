package com.wallet.metier;

import java.util.UUID;

import enums.Priority;
import enums.Status;

import java.time.LocalDateTime;

public class Transaction {

	private UUID id;
	private double montant;
	private String source;
	private String destination ;
	private LocalDateTime date;
	private double frais;
	private int taille_bytes;
	private Priority priority;
    private Status status;
	
	public Transaction (UUID id , double montant, String source,String destination,
			LocalDateTime date,double frais,int taille_bytes,Priority priority, Status status) {
		this.id = id != null ? id : UUID.randomUUID();
		this.montant=montant;
		this.source=source;
		this.destination=destination;
		this.date=(date != null) ? date : LocalDateTime.now();
		this.frais=frais;
		this.taille_bytes=taille_bytes;
		this.priority = priority;
        this.status = status;
		
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
	public Priority getPriority() {
        return priority;
    }
	public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

	
}
