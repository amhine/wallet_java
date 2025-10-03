package Dto;

import java.util.UUID;

public class TransactionResponseDTO {

	private UUID id;
    private String type;
    private String source;
    private String destination;
    private double montant;

    public TransactionResponseDTO(UUID id, String source, String destination, double montant, String type) {
        this.id = id;
        this.source = source;
        this.destination= destination;
        this.montant = montant;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
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
}
