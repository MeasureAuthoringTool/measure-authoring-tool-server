package mat.authentication.twofactorauth;

public interface OTPValidatorInterfaceForUser {
	
	public boolean validateOTPForUser(String loginId, String otp);

}
