package Dto;

import java.util.UUID;

public class MempoolRequestDTO {
	
	 private final UUID transactionId;

	    public MempoolRequestDTO(UUID transactionId) {
	        this.transactionId = transactionId;
	    }

	    public UUID getTransactionId() {
	        return transactionId;
	    }

}
