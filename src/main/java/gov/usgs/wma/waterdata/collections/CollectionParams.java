package gov.usgs.wma.waterdata.collections;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CollectionParams {
	@Value("${site.url.base}")
	private String serverUrl;

	@Value("${collections.monitoring-locations.title}")
	private String monLocTitle;

	@Value("${collections.monitoring-locations.description}")
	private String monLocDescription;

	public Map<String, Object> getParameters(String collectionId) {
		return getParameters(collectionId, null);
	}

	public Map<String, Object> getParameters(String collectionId, String featureId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("serverUrl", serverUrl);
		if (collectionId != null) {
			paramMap.put("collectionId", collectionId);
		}
		if (featureId != null) {
			paramMap.put("featureId", featureId);
		}
		paramMap.put("monLocTitle", monLocTitle);
		paramMap.put("monLocDescription", monLocDescription);

		return paramMap;
	}

}
