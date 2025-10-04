package Controller;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;

import com.wallet.metier.Transaction;
import com.wallet.metier.Wallet;
import com.wallet.service.MempoolService;

import Dto.FraisComparisonResponseDTO;
import Dto.MempoolRequestDTO;
import Dto.MempoolResponseDTO;
import Dto.MempoolTransactionDTO;

public class MempoolController {
    private final MempoolService mempoolService;

    public MempoolController(MempoolService mempoolService) {
        this.mempoolService = mempoolService;
    }

    public MempoolResponseDTO checkTransactionPosition(MempoolRequestDTO request) {
        List<Transaction> pending = mempoolService.getPendingTransactions();

        OptionalInt position = mempoolService.getTransactionPosition(request.getTransactionId());
        OptionalDouble estimatedTime = mempoolService.estimateTime(request.getTransactionId(), 5);

        if (position.isPresent() && estimatedTime.isPresent()) {
            return new MempoolResponseDTO(
                    true,
                    position.getAsInt(),
                    pending.size(),
                    estimatedTime.getAsDouble()
            );
        } else {
            return new MempoolResponseDTO(false, -1, pending.size(), -1);
        }
    }

    public List<FraisComparisonResponseDTO> compareFeeLevels(Transaction tx, Wallet wallet, int txPerBlock) {
        List<Map<String, Object>> rawList = mempoolService.compareFeeLevels(tx, wallet, txPerBlock);

        return rawList.stream()
                .map(map -> new FraisComparisonResponseDTO(
                        (String) map.get("priority"),
                        ((Number) map.get("frais")).doubleValue(),
                        ((Number) map.get("position")).intValue(),
                        ((Number) map.get("date")).doubleValue()
                ))
                .collect(Collectors.toList());
    }


    public List<MempoolTransactionDTO> getMempool(UUID myTxId) {
        List<Transaction> mempool = mempoolService.getMempoolWithRandomTransactions(myTxId);

        return mempool.stream()
                .map(tx -> new MempoolTransactionDTO(
                        tx.getId(),
                        tx.getSource(),
                        tx.getFrais(),
                        tx.getId().equals(myTxId)
                ))
                .collect(Collectors.toList());
    }
}
