package Dto;

import enums.Priority;

public class TransactionRequestDTO {

	 private  String source;
	    private  String destination;
	    private double montant;
	    private Priority priority;
	    private int taille_bytes;

	    public TransactionRequestDTO(String source, String destination, double montant, int taille_bytes, Priority priority) {
	        this.source = source;
	        this.destination = destination;
	        this.montant = montant;
	        this.taille_bytes = taille_bytes;
	        this.priority = priority;
	    }

	    public String getSource() {
	        return source;
	    }

	    public String getDestination() {
	        return destination;
	    }

	    public double getMontant() {
	        return montant;
	    }

	    public Priority getPriority() {
	        return priority;
	    }

	    public int getTaille_bytes()  {
	        return taille_bytes;
	    }

}
