package Dto;

import java.util.UUID;

public class MempoolTransactionDTO {

	 private UUID id;
	    private String source;
	    private double frais;
	    private boolean isUserTx;

	    public MempoolTransactionDTO(UUID id, String source, double frais, boolean isUserTx) {
	        this.id = id;
	        this.source= source;
	        this.frais = frais;
	        this.isUserTx = isUserTx;
	    }

	    public UUID getId() {
	        return id;
	    }

	    public void setId(UUID id) {
	        this.id = id;
	    }

	    public String getSource() {
	        return source;
	    }

	    public void setSource(String source) {
	        this.source= source;
	    }

	    public double getFrais() {
	        return frais;
	    }

	    public void setFrais(double frais) {
	        this.frais = frais;
	    }

	    public boolean isUserTx() {
	        return isUserTx;
	    }

	    public void setUserTx(boolean userTx) {
	        isUserTx = userTx;
	    }

}
