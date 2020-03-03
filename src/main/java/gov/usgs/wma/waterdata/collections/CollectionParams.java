package gov.usgs.wma.waterdata.collections;

import java.util.HashMap;
import java.util.List;
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

	@Value("${query.limit.max}")
	private int maxLimit;

	public static final Integer START_INDEX_DEFAULT = 0;

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

	public String getServerUrl() {
		return serverUrl;
	}

	public int getMaxLimit() {
		return maxLimit;
	}

	public void setLimit(Map<String, Object> params, int limit) {
		params.put("limit",limit);
	}

	public void setStartIndex(Map<String, Object> params, int startIndex) {
		params.put("startIndex",startIndex);
	}

	/*
	 * a bounding box with four numbers:
     * Lower left corner,  longitude
     * Lower left corner,  latitude
     * Upper right corner, longitude
     * Upper right corner, latitude
	 */
	public void setBbox(Map<String, Object> params, List<Double> bbox) {
		if (bbox != null && bbox.size() == 4) {
			params.put("pointLowLeft", String.format("Point(%f %f)", bbox.get(0), bbox.get(1)));
			params.put("pointUpRight", String.format("Point(%f %f)", bbox.get(2), bbox.get(3)));
		}
	}

	public void setPagingParams(Map<String, Object> params, int count) {
		int startIndex = params.get("startIndex") == null ? START_INDEX_DEFAULT : (Integer) params.get("startIndex");
		int limit = params.get("limit") == null ? maxLimit : (Integer) params.get("limit");

		if (startIndex > 0) {
			int prevStartIndex = startIndex - limit;
			if (prevStartIndex < 0) {
				prevStartIndex = 0;
			}
			params.put("prevStartIndex", String.format("&startIndex=%d&limit=%d", prevStartIndex, limit));
		}

		int nextStartIndex = startIndex + limit;
		if (nextStartIndex <= count - 1) {
			params.put("nextStartIndex", String.format("&startIndex=%d&limit=%d", nextStartIndex, limit));
		}
	}
}
