package individual.donationservice.business.paypal;

public interface CreatePayPalOrderUseCase {
    String createOrder(String accessToken, String paypalEmail, double amount);
}
