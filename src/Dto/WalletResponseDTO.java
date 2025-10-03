package Dto;

import java.util.UUID;

public class WalletResponseDTO {
	private UUID id;
    private String type;
    private String address;
    private double solde;

    public WalletResponseDTO(UUID id, String type, String address, double solde) {
        this.id = id;
        this.type = type;
        this.address = address;
        this.solde = solde;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public double getSolde() {
        return solde;
    }
}
