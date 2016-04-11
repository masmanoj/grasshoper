package in.grasshoper.core.exceptionmappers;

import in.grasshoper.core.exception.AbstractPlatformRuleException;
import in.grasshoper.core.exception.PlatformApiDataValidationException;
import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.PlatformException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.exception.UnsupportedParameterException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ch.qos.logback.classic.spi.PlatformInfo;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<Object> handleControllerException(@SuppressWarnings("unused") HttpServletRequest req, Throwable ex) {
		Map<String,Object> errorResponse = new HashMap<>();
        if(ex instanceof ResourceNotFoundException) {
        	populateErrorResponseMessage(errorResponse,(PlatformException) ex);
            return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
        } else if (ex instanceof AbstractPlatformRuleException){
        	populateErrorResponseMessage(errorResponse,(AbstractPlatformRuleException) ex);
        	return new ResponseEntity<Object>(errorResponse,HttpStatus.BAD_REQUEST);
        }else if(ex instanceof PlatformApiDataValidationException){
        	populateErrorResponseMessage(errorResponse,(PlatformApiDataValidationException) ex);
        	return new ResponseEntity<Object>(errorResponse,HttpStatus.BAD_REQUEST);
        }else if(ex instanceof UnsupportedParameterException){
        	populateErrorResponseMessage(errorResponse,(UnsupportedParameterException) ex);
        	return new ResponseEntity<Object>(errorResponse,HttpStatus.BAD_REQUEST);
        }else if(ex instanceof PlatformDataIntegrityException){
        	populateErrorResponseMessage(errorResponse,(PlatformDataIntegrityException) ex);
        	return new ResponseEntity<Object>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }else if(ex instanceof DataAccessException) {
        	ex.printStackTrace();
        	return new ResponseEntity<Object>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
        }else if(ex instanceof BadCredentialsException){
        	ex.printStackTrace();
        	populateErrorResponseMessage(errorResponse,(BadCredentialsException) ex);
            return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
        }else {
        	ex.printStackTrace();
            return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   

	private void populateErrorResponseMessage(
			Map<String, Object> errorResponse, BadCredentialsException exception) {
		errorResponse.put("globalisationMessageCode", "error.msg.authentiocation.failed");
    	errorResponse.put("defaultUserMessage", "Authentication Failed, Bad Credentials.");
    	errorResponse.put("errors" , "Bad Credentials.");
		
	}



	@Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@SuppressWarnings("unused") NoHandlerFoundException ex, 
    		@SuppressWarnings("unused") HttpHeaders headers, @SuppressWarnings("unused") HttpStatus status, WebRequest request) {
        Map<String,String> responseBody = new HashMap<>();
        responseBody.put("path",request.getContextPath());
        responseBody.put("message","The URL you have reached is not in service at this time (404).");
        return new ResponseEntity<Object>(responseBody,HttpStatus.NOT_FOUND);
    }
    
    private void populateErrorResponseMessage(Map<String,Object> errorResponse,
    		final PlatformException exception) {
    	errorResponse.put("globalisationMessageCode", exception.getGlobalisationMessageCode());
    	errorResponse.put("defaultUserMessage", exception.getDefaultUserMessage());
    	for(int i=0; i < exception.getDefaultUserMessageArgs().length; i++)
    		errorResponse.put("defaultUserMessageArgs" + i,
    				exception.getDefaultUserMessageArgs()[i].toString());
    }
    
    private void populateErrorResponseMessage(Map<String,Object> errorResponse,
    		final AbstractPlatformRuleException exception) {
    	errorResponse.put("globalisationMessageCode", exception.getGlobalisationMessageCode());
    	errorResponse.put("defaultUserMessage", exception.getDefaultUserMessage());
    	for(int i=0; i < exception.getDefaultUserMessageArgs().length; i++)
    		errorResponse.put("defaultUserMessageArgs" + i,
    				exception.getDefaultUserMessageArgs()[i].toString());
    }
    
    private void populateErrorResponseMessage(Map<String,Object> errorResponse,
    		final PlatformApiDataValidationException exception) {
    	errorResponse.put("globalisationMessageCode", exception.getGlobalisationMessageCode());
    	errorResponse.put("defaultUserMessage", exception.getDefaultUserMessage());
    	errorResponse.put("errors" , exception.getErrors());
    }
    private void populateErrorResponseMessage(Map<String,Object> errorResponse,
    		final UnsupportedParameterException exception) {
    	errorResponse.put("globalisationMessageCode", exception.getGlobalisationMessageCode());
    	errorResponse.put("defaultUserMessage", exception.getDefaultUserMessage());
    	errorResponse.put("defaultUserMessageArgs" , exception.getUnsupportedParameters());
    }
    
    private void populateErrorResponseMessage(
			Map<String, Object> errorResponse, PlatformDataIntegrityException exception) {
    	errorResponse.put("globalisationMessageCode", exception.getGlobalisationMessageCode());
    	errorResponse.put("defaultUserMessage", exception.getDefaultUserMessage());
    	errorResponse.put("defaultUserMessageArgs" , exception.getParameterName());
	}
}
