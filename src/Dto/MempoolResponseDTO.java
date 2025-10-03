package Dto;

public class MempoolResponseDTO {

	private final boolean found;
    private final int position;
    private final int total;
    private final double date;

    public MempoolResponseDTO(boolean found, int position, int total, double date) {
        this.found = found;
        this.position = position;
        this.total = total;
        this.date = date;
    }

    public boolean isFound() {
        return found;
    }

    public int getPosition() {
        return position;
    }

    public int getTotal() {
        return total;
    }

    public double getDate() {
        return date;
    }
}
