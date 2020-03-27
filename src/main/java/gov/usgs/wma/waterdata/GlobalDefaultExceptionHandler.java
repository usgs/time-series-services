package gov.usgs.wma.waterdata;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);
	public static final String CODE_KEY = "code";
	public static final String DESCRIPTION_KEY = "description";

	@ExceptionHandler(Exception.class)
	public @ResponseBody Map<String, String> handleUncaughtException(Exception ex, WebRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> responseMap = new LinkedHashMap<>();
		if (ex instanceof MissingServletRequestParameterException
				|| ex instanceof HttpMediaTypeNotSupportedException
				|| ex instanceof HttpMediaTypeNotAcceptableException) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			responseMap.put(CODE_KEY, Integer.toString(HttpStatus.BAD_REQUEST.value()));
			responseMap.put(DESCRIPTION_KEY, ex.getLocalizedMessage());
		} else if (ex instanceof ConstraintViolationException) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			responseMap.put(CODE_KEY, Integer.toString(HttpStatus.BAD_REQUEST.value()));
			ConstraintViolationException ce = (ConstraintViolationException) ex;
			String message = ce.getConstraintViolations() == null || ce.getConstraintViolations().isEmpty() ? "Constraint Violation: [No message available]"
					: ce.getConstraintViolations().iterator().next().getMessage();
			responseMap.put(DESCRIPTION_KEY, message);
		} else if (ex instanceof HttpMessageNotReadableException) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			responseMap.put(CODE_KEY, Integer.toString(HttpStatus.BAD_REQUEST.value()));
			if (ex.getLocalizedMessage().contains("\n")) {
				//This exception's message contains implementation details after the new line, so only take up to that.
				responseMap.put(DESCRIPTION_KEY, ex.getLocalizedMessage().substring(0, ex.getLocalizedMessage().indexOf("\n")));
			} else {
				responseMap.put(DESCRIPTION_KEY, ex.getLocalizedMessage().replaceAll("([a-zA-Z]+\\.)+",""));
			}
		} else {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseMap.put(CODE_KEY, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			int hashValue = response.hashCode();
			//Note: we are giving the user a generic message.  
			//Server logs can be used to troubleshoot problems.
			String msgText = "Unexpected error occurred. Contact us with Reference Number: " + hashValue;
			LOG.error(msgText, ex);
			responseMap.put(DESCRIPTION_KEY, msgText);
		}
		return responseMap;
	}

}
