package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;

@SpringBootTest
public class GlobalDefaultExceptionHandlerTest {

	@MockBean
	private WebRequest request;
	@MockBean
	private MockHttpInputMessage inputMessage;
	private GlobalDefaultExceptionHandler controller = new GlobalDefaultExceptionHandler();

	@Test
	public void handleMissingServletRequestParameterException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "Required String parameter 'parm' is not present";
		Map<String, String> actual = controller.handleUncaughtException(new MissingServletRequestParameterException("parm", "String"), request, response);
		assertEquals(expected, actual.get(GlobalDefaultExceptionHandler.ERROR_MESSAGE_KEY));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleHttpMediaTypeNotSupportedException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "no way";
		Map<String, String> actual = controller.handleUncaughtException(new HttpMediaTypeNotSupportedException(expected), request, response);
		assertEquals(expected, actual.get(GlobalDefaultExceptionHandler.ERROR_MESSAGE_KEY));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleHttpMediaTypeNotAcceptableException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "no way";
		Map<String, String> actual = controller.handleUncaughtException(new HttpMediaTypeNotAcceptableException(expected), request, response);
		assertEquals(expected, actual.get(GlobalDefaultExceptionHandler.ERROR_MESSAGE_KEY));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleHttpMessageNotReadableException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "Some123$Mes\tsage!!.";
		Map<String, String> actual = controller.handleUncaughtException(new HttpMessageNotReadableException(expected, inputMessage), request, response);
		assertEquals(expected, actual.get(GlobalDefaultExceptionHandler.ERROR_MESSAGE_KEY));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleMultilineHttpMessageNotReadableException() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "ok to see";
		Map<String, String> actual = controller.handleUncaughtException(new HttpMessageNotReadableException("ok to see\nhide this\nand this", inputMessage), request, response);
		assertEquals(expected, actual.get(GlobalDefaultExceptionHandler.ERROR_MESSAGE_KEY));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void handleUncaughtExceptionTest() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		String expected = "Unexpected error occurred. Contact us with Reference Number: ";
		Map<String, String> actual = controller.handleUncaughtException(new RuntimeException(), request, response);
		assertEquals(expected, actual.get(GlobalDefaultExceptionHandler.ERROR_MESSAGE_KEY).substring(0, expected.length()));
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
	}
}
