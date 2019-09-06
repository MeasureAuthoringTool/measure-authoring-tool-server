package mat.authentication;

import mat.model.authentication.LoginModel;
import mat.service.authentication.impl.LoginCredentialServiceImpl;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class MatAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    LoginCredentialServiceImpl loginCredentialService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        setAuthenticationFailureHandler(new ForwardAuthenticationFailureHandler("/oauth_login"));
        UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
        setDetails(request, authRequest);
        return authRequest;
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String twoFactorAuthCode = obtainTwoFactorAuthCode(request);
        String sessionId  = request.getSession().getId();

        LoginModel loginModel = loginCredentialService.isValidUser(username, password, twoFactorAuthCode, sessionId);
        return loginModel.getUsernamePasswordAuthenticationToken();
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,FilterChain filter, Authentication authResult)
            throws IOException, ServletException {
        System.err.println("got to successful authentication");
        super.successfulAuthentication(request, response,filter, authResult);

    }

    private String obtainTwoFactorAuthCode(HttpServletRequest request){
        String twoFactorAuthCode = request.getParameter("twoFactorAuth");
        return twoFactorAuthCode;
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
