package Dto;

public class FraisComparisonResponseDTO {

	private final String priority;
    private final double frais;
    private final int position;
    private final double date;

    public FraisComparisonResponseDTO(String priority, double frais, int position, double date) {
        this.priority = priority;
        this.frais = frais;
        this.position = position;
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public double getFrais() {
        return frais;
    }

    public int getPosition() {
        return position;
    }

    public double getDate() {
        return date;
    }

}
