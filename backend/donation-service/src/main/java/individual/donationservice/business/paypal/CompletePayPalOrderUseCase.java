package individual.donationservice.business.paypal;

public interface CompletePayPalOrderUseCase {
    String completePayment(String accessToken, String token, String payerId);
}
