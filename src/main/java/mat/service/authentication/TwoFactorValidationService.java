package mat.service.authentication;

import mat.authentication.twofactorauth.DefaultOTPValidatorForUser;
import mat.authentication.twofactorauth.OTPValidatorInterfaceForUser;
import mat.authentication.twofactorauth.vip.OTPValidatorForUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorValidationService {
	@Value("${2FA_AUTH_CLASS}")
	private String twoFactorAuthClass;


	public boolean validateOTPForUser(String loginId, String otp){
		return this.getOtpValidatorInterfaceForUser().validateOTPForUser(loginId, otp);
	}

	@Bean
	public OTPValidatorInterfaceForUser getOtpValidatorInterfaceForUser() {
		if(twoFactorAuthClass.equals("mat.server.twofactorauth.DefaultOTPValidatorForUser")) {
			return new DefaultOTPValidatorForUser();
		} else {
			return new OTPValidatorForUser();
		}
	}
}
