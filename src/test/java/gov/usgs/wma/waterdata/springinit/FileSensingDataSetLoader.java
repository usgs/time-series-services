package gov.usgs.wma.waterdata.springinit;

import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.Resource;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;

import static gov.usgs.wma.waterdata.springinit.BaseIT.getReplacementTokens;

public class FileSensingDataSetLoader extends AbstractDataSetLoader {

	@Override
	protected IDataSet createDataSet(Resource resource) throws Exception {
		if (resource.getFilename().endsWith("xml")) {
			return createXmlDataSet(resource);
		} else {
			return createCsvDataset(resource);
		}
	}

	private IDataSet createCsvDataset(Resource resource) throws Exception {
		return createReplacementDataSet(new CsvURLDataSet(resource.getURL()));
	}

	private IDataSet createXmlDataSet(Resource resource) throws Exception {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		try (InputStream inputStream = resource.getInputStream()) {
			return createReplacementDataSet(builder.build(inputStream));
		}
	}

	private IDataSet createReplacementDataSet(IDataSet iDataSet) {
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(iDataSet);

		List<AbstractMap.SimpleEntry<String, String>> replacements = BaseIT.getReplacementTokens();
		for (AbstractMap.SimpleEntry<String, String> replacement: replacements) {
			String orgText = replacement.getKey();
			String newText = replacement.getValue();

			replacementDataSet.addReplacementSubstring(orgText, newText);
		}

		return replacementDataSet;
	}
}
