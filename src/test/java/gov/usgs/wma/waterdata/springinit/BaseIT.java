package gov.usgs.wma.waterdata.springinit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@ActiveProfiles("it")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader=FileSensingDataSetLoader.class)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Transactional(propagation=Propagation.NOT_SUPPORTED)
@Import({DBTestConfig.class})
@DirtiesContext
public abstract class BaseIT {
	protected String ogc404Payload = "{\"code\":\"404\", \"description\":\"Requested data not found\"}";


	/**
	 * Returns a file from the the testResult directory on the classpath,
	 * and does token replacement in it, as spec'ed in getReplacementTokens().
	 *
	 * @param file File name/path under testResult.
	 * @return
	 * @throws IOException
	 */
	public String getCompareFile(String file) throws IOException {
		String text = new String(FileCopyUtils.copyToByteArray(new ClassPathResource("testResult/" + file).getInputStream()));
		return doReplacements(text);
	}

	public void assertJsonEquals(String expected, String actual) {
		try {
			assertThat(new JSONObject(actual),
					sameJSONObjectAs(new JSONObject(expected)));
		} catch (JSONException e) {
			fail("Unexpected JSONException during test", e);
		}
	}

	/**
	 * A yyyy-MM-dd formated date, offset by the spec'ed number of days.
	 *
	 * @param offset Positive for future days, negative for past days, zero for today.
	 * @return A yyyy-MM-dd formated date.
	 */
	public static String getDateStr(int offset) {
		LocalDate now = LocalDate.now();
		return now.plusDays(offset).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	/**
	 * The list of tokens that are replaced in test resources, both comparison file
	 * (via getCompareFile) and dataset cvs file (via FileSensingDataSetLoader).
	 * @return
	 */
	public static List<SimpleEntry<String, String>> getReplacementTokens() {
		List<SimpleEntry<String, String>> replace = new ArrayList();

		//Used for testing the rule that unapproved data older than 1095 days should not be displayed.
		replace.add(new SimpleEntry("[NOW-1095DAYS]", getDateStr(-1095)));
		replace.add(new SimpleEntry("[NOW-1096DAYS]", getDateStr(-1096)));

		return replace;
	}

	/**
	 * Replace all the recognized tokens w/ their replacements in the passed String.
	 * Tokens taken from getReplacementTokens.
	 * @param source To be replaced
	 * @return The string w/ all replacements applied.
	 */
	public static String doReplacements(String source) {
		List<SimpleEntry<String, String>> replacements = getReplacementTokens();

		for (SimpleEntry<String, String> replacement: replacements) {
			String orgText = replacement.getKey();
			String newText = replacement.getValue();

			//Most files don't need replacement
			if (source.contains(orgText))
				source = source.replace(orgText, newText);
		}

		return source;
	}
}
