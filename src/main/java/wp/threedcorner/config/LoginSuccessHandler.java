package wp.threedcorner.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import wp.threedcorner.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        User userDetails = (User) authentication.getPrincipal();

        String redirectURL = request.getContextPath();

        if (userDetails.hasRole("ROLE_USER")) {
            redirectURL = "/home";
        } else if (userDetails.hasRole("ROLE_ADMIN")) {
            redirectURL = "/admin";
        }
        response.sendRedirect(redirectURL);

    }

}