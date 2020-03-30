package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ch.qos.logback.classic.Level.OFF;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

@SpringBootTest
@DirtiesContext
@TestExecutionListeners(DirtiesContextTestExecutionListener.class)
public class GlobalDefaultExceptionHandlerTest {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(GlobalDefaultExceptionHandlerTest.class);
	
	@MockBean
	private WebRequest request;
	@MockBean
	private MockHttpInputMessage inputMessage;
	private GlobalDefaultExceptionHandler controller = new GlobalDefaultExceptionHandler();

	@Test
	public void handleMissingServletRequestParameterException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "Required String parameter 'parm' is not present";
		OgcException actual = controller.handleUncaughtException(new MissingServletRequestParameterException("parm", "String"), request, response);
		assertNotNull(actual);
		assertEquals(expected, actual.getDescription());
		assertEquals(Integer.toString(HttpStatus.BAD_REQUEST.value()), actual.getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleHttpMediaTypeNotSupportedException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "no way";
		OgcException actual = controller.handleUncaughtException(new HttpMediaTypeNotSupportedException(expected), request, response);
		assertNotNull(actual);
		assertEquals(expected, actual.getDescription());
		assertEquals(Integer.toString(HttpStatus.BAD_REQUEST.value()), actual.getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleHttpMediaTypeNotAcceptableException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "no way";
		OgcException actual = controller.handleUncaughtException(new HttpMediaTypeNotAcceptableException(expected), request, response);
		assertNotNull(actual);
		assertEquals(expected, actual.getDescription());
		assertEquals(Integer.toString(HttpStatus.BAD_REQUEST.value()), actual.getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleHttpMessageNotReadableException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "Some123$Mes\tsage!!.";
		OgcException actual = controller.handleUncaughtException(new HttpMessageNotReadableException(expected, inputMessage), request, response);
		assertNotNull(actual);
		assertEquals(expected, actual.getDescription());
		assertEquals(Integer.toString(HttpStatus.BAD_REQUEST.value()), actual.getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleMultilineHttpMessageNotReadableException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "ok to see";
		OgcException actual = controller.handleUncaughtException(new HttpMessageNotReadableException("ok to see\nhide this\nand this", inputMessage), request, response);
		assertNotNull(actual);
		assertEquals(expected, actual.getDescription());
		assertEquals(Integer.toString(HttpStatus.BAD_REQUEST.value()), actual.getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleConstraintViolationNoMessageException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "Constraint Violation: [No message available]";
		ConstraintViolationException ce = new ConstraintViolationException(null);
		OgcException actual = controller.handleUncaughtException(ce, request, response);
		assertNotNull(actual);
		assertEquals(expected, actual.getDescription());
		assertEquals(Integer.toString(HttpStatus.BAD_REQUEST.value()), actual.getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

		response = new MockHttpServletResponse();
		ce = new ConstraintViolationException(Collections.emptySet());
		actual = controller.handleUncaughtException(ce, request, response);
		assertNotNull(actual);
		assertEquals(expected, actual.getDescription());
		assertEquals(Integer.toString(HttpStatus.BAD_REQUEST.value()), actual.getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleConstraintViolationException() throws IOException {
		ConstraintViolation<?> constraintViolation = Mockito.mock(ConstraintViolation.class);
		Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
		String expected = "Some message saying what the violation was";

		constraintViolations.add(constraintViolation);
		ConstraintViolationException violationException = Mockito.mock(ConstraintViolationException.class);
		Mockito.when(violationException.getConstraintViolations()).thenReturn(constraintViolations);
		Mockito.when(violationException.getConstraintViolations().iterator().next().getMessage()).thenReturn(expected);

		HttpServletResponse response = new MockHttpServletResponse();
		OgcException actual = controller.handleUncaughtException(violationException, request, response);
		assertNotNull(actual);
		assertEquals(expected, actual.getDescription());
		assertEquals(Integer.toString(HttpStatus.BAD_REQUEST.value()), actual.getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleUncaughtExceptionTest() throws IOException {
		// capture logging state prior to turning it off
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		Level level = root.getLevel();

		try {
			log.info("turning logging off -- for clean log and no exceptions in none broken events.");
			root.setLevel(OFF);

			HttpServletResponse response = new MockHttpServletResponse();
			String expected = "Unexpected error occurred. Contact us with Reference Number: ";
			OgcException actual = controller.handleUncaughtException(new RuntimeException(), request, response);
			assertNotNull(actual);
			assertEquals(expected, actual.getDescription().substring(0, expected.length()));
			assertEquals(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), actual.getCode());
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
		} finally {
			log.info("turning logging back on --");
			root.setLevel(level);
		}
	}
}
