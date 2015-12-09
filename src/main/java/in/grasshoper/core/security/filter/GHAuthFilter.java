package in.grasshoper.core.security.filter;

import in.grasshoper.core.security.service.SessionService;
import in.grasshoper.user.domain.User;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

@Service
public class GHAuthFilter extends GenericFilterBean {
	private final static Logger logger = LoggerFactory.getLogger(GHAuthFilter.class);
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private RememberMeServices rememberMeServices = new NullRememberMeServices();
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
	
	private final AuthenticationManager authenticationManager;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final SessionService sessionService;
	
    @Autowired
    public GHAuthFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint,
            final SessionService sessionService) {
        this.sessionService = sessionService;
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		final boolean debug = logger.isDebugEnabled();
		final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        
       // if(request.getPathInfo() == null)
    	//	return;
        response.setHeader("Cache-Control", "no-store");
        String header = request.getHeader("Authorization");
        String authInQuery = request.getParameter("authKey");
        
        if ((header == null || !header.startsWith("Custom ")) && (authInQuery == null || request.getMethod().equalsIgnoreCase("OPTIONS"))) {
        	chain.doFilter(request, response);
            return;
        }
        
        
        try {
	            String token = null;
	            String otpToken = null;
	            User user = null;
	            User otpUser = null;
	            if (header != null && authInQuery != null) {
	            	token = header.replaceFirst("Custom ", "");
	            	user = sessionService.retrieveUserFromToken(token);
	            }else if(header != null) {
		        	token = header.replaceFirst("Custom ", "");
		            user = sessionService.retrieveUserFromToken(token);
		        }
	            
	            if (user != null) {
                	constructAuth(request, response, user);
                } else {
                    throw new BadCredentialsException("Session expired");
                }
        }catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            if (debug) {
                logger.debug("Authentication request for failed: " + failed);
            }

            rememberMeServices.loginFail(request, response);

            onUnsuccessfulAuthentication(request, response, failed);
            boolean ignoreFailure = false;
            if (ignoreFailure) {
                chain.doFilter(request, response);
            } else {
                authenticationEntryPoint.commence(request, response, failed);
            }
            return;
        }
        chain.doFilter(request, response);
	}
	 private void constructAuth(HttpServletRequest request, HttpServletResponse response,
	    		User user) throws IOException {
		 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
                 authoritiesMapper.mapAuthorities(user.getAuthorities()));
		 auth.setDetails(authenticationDetailsSource.buildDetails(request));
		 SecurityContextHolder.getContext().setAuthentication(auth);
         rememberMeServices.loginSuccess(request, response, auth);
         onSuccessfulAuthentication(request, response, auth);
		 
	 }
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult)
            throws IOException {}

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException {}
}
