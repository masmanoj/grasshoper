package in.grasshoper.core.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * This entry point is called once the request missing the authentication but if
 * the request dosn't have the cookie then it sends the unauthorized response.
 * 
 * @author malalanayake
 * 
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.info("Unauthorized access. Request URI: '" + request.getRequestURI() + "'");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

    }

}