package mat.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @GetMapping("/oauth_login")
    public ModelAndView getLoginPage(HttpServletRequest request) {
        if(request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) {
            AuthenticationException authenticationException = (AuthenticationException)request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            return new ModelAndView("oauth_login", "error", authenticationException.getMessage());
        } else {
            return new ModelAndView("oauth_login");
        }
    }

    @GetMapping("/forgot_login_id")
    public ModelAndView getResetUserIdPage(HttpServletRequest request) {
        return new ModelAndView("forgot_user_id");
    }

    @GetMapping("/forgot_password")
    public ModelAndView getResetPasswordPage(HttpServletRequest request) {
        return new ModelAndView("reset_password");
    }

    @GetMapping("/submit_forgot_user_id")
    public ModelAndView getSubmitForgotUserId(HttpServletRequest request) {
        System.err.println("submit forgot user id");
        //TODO
        return new ModelAndView("oauth_login");
    }

    @GetMapping("/submit_reset_password")
    public ModelAndView getSubmitResetPassword(HttpServletRequest request) {
        System.err.println("submit reset password");
        //TODO
        return new ModelAndView("oauth_login");
    }
}
