package repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.wallet.metier.Wallet;

public interface WalletRepository {

	 void save(Wallet wallet);
	    Optional<Wallet> findById(UUID id);
	    Optional<Wallet> findWalletByAddress(String address);
	    List<Wallet> findAll();
	    
}
