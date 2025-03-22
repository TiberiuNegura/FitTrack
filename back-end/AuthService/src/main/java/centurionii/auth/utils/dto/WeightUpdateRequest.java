package centurionii.auth.utils.dto;

public record WeightUpdateRequest(Long userId, float weight, int month, int day, int year) {
}
