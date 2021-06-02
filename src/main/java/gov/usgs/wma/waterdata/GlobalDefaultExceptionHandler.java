package gov.usgs.wma.waterdata;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);
	private static final String NESTED_EXCEPTION_REGEX = ".*nested exception is.*:";

	@ExceptionHandler(Exception.class)
	public @ResponseBody OgcException handleUncaughtException(Exception ex, WebRequest request,
			HttpServletResponse response) throws IOException {
		OgcException ogcException = new OgcException();
		if (ex instanceof MissingServletRequestParameterException || ex instanceof HttpMediaTypeNotSupportedException
				|| ex instanceof HttpMediaTypeNotAcceptableException) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			ogcException.setCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
			ogcException.setDescription(ex.getLocalizedMessage());
		} else if (ex instanceof ConstraintViolationException) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			ConstraintViolationException ce = (ConstraintViolationException) ex;
			String message = ce.getConstraintViolations() == null || ce.getConstraintViolations().isEmpty()
					? "Constraint Violation: [No message available]"
					: ce.getConstraintViolations().iterator().next().getMessage();
			ogcException.setCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
			ogcException.setDescription(message);
		} else if (ex instanceof BindException && hasOnlyFieldErrors((BindException) ex)) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			ogcException.setCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
			ogcException.setDescription(formatErrorMessage((BindException) ex));
		} else if (ex instanceof MethodArgumentTypeMismatchException) {
			MethodArgumentTypeMismatchException argEx = (MethodArgumentTypeMismatchException) ex;
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			ogcException.setCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
			ogcException.setDescription(String.format("Error in parameter %s: %s", argEx.getName(),
					argEx.getMessage().replaceAll(".*IllegalArgumentException:", "")));
		} else if (ex instanceof HttpMessageNotReadableException) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			ogcException.setCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
			if (ex.getLocalizedMessage().contains("\n")) {
				// This exception's message contains implementation details after the new line,
				// so only take up to that.
				ogcException
						.setDescription(ex.getLocalizedMessage().substring(0, ex.getLocalizedMessage().indexOf("\n")));
			} else {
				ogcException.setDescription(ex.getLocalizedMessage().replaceAll("([a-zA-Z]+\\.)+", ""));
			}
		} else {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			ogcException.setCode(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			int hashValue = response.hashCode();
			// Note: we are giving the user a generic message.
			// Server logs can be used to troubleshoot problems.
			String msgText = "Unexpected error occurred. Contact us with Reference Number: " + hashValue;
			LOG.error(msgText, ex);
			ogcException.setDescription(msgText);
		}
		return ogcException;
	}

	// return true if the bind exception only has field (data) errors, typically
	// caused by bad input
	private boolean hasOnlyFieldErrors(BindException ex) {
		return ex.hasFieldErrors() && !ex.hasGlobalErrors();
	}

	// parse out a user friendly field message from the field error in the binding
	// exception
	private String formatErrorMessage(BindException ex) {
		StringBuilder errorMess = new StringBuilder(100);
		String sep = "";

		for (FieldError err : ex.getFieldErrors()) {
			String mess = err.getDefaultMessage() == null ? " Error processing field" : err.getDefaultMessage();
			mess = mess.replaceAll(NESTED_EXCEPTION_REGEX, "");
			String prefix = "Error in parameter " + err.getField() + ":";
			errorMess.append(sep).append(prefix).append(mess);
			sep = " -- ";
		}

		return errorMess.toString();
	}

}
