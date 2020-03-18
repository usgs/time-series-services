package gov.usgs.wma.waterdata.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.usgs.wma.waterdata.ConfigurationService;

@Component
public class CollectionParams {
	@Autowired
	protected ConfigurationService configurationService;

	public static final int START_INDEX_DEFAULT = 0;
	public static final int MAX_LIMIT = 100000;

	public Map<String, Object> buildParams(String collectionId) {
		Map<String, Object> params = buildCommonParams();
		if (collectionId != null) {
			params.put("collectionId", collectionId);
		}

		return params;
	}

	public Map<String, Object> buildParams(String collectionId, String featureId) {
		Map<String, Object> params = buildCommonParams();
		if (collectionId != null) {
			params.put("collectionId", collectionId);
		}
		if (featureId != null) {
			params.put("featureId", featureId);
		}

		return params;
	}

	public Map<String, Object> buildParams(String collectionId, int limit, int startIndex,
			List<String> bbox, int count) {
		Map<String, Object> params = buildCommonParams();
		if (collectionId != null) {
			params.put("collectionId", collectionId);
		}
		int limitParam = limit;
		if (limit > MAX_LIMIT) {
			limitParam = MAX_LIMIT;
		}
		params.put("limit", limitParam);
		/*
		 * a bounding box with four numbers: latitude Lower left corner, longitude Lower left
		 * corner, latitude Upper right corner, longitude Upper right corner, latitude
		 */
		if (bbox != null && bbox.size() == 4) {
			params.put("pointLowLeft", String.format("Point(%s %s)", bbox.get(0), bbox.get(1)));
			params.put("pointUpRight", String.format("Point(%s %s)", bbox.get(2), bbox.get(3)));
		}
		params.put("startIndex", startIndex);
		if (startIndex > 0) {
			int prevStartIndex = Math.max(0, startIndex - limit);
			params.put("prevStartIndex", String.format("&startIndex=%d&limit=%d", prevStartIndex, limit));
		}

		int nextStartIndex = startIndex + limit;
		if (nextStartIndex <= count - 1) {
			params.put("nextStartIndex", String.format("&startIndex=%d&limit=%d", nextStartIndex, limit));
		}

		return params;
	}

	private Map<String, Object> buildCommonParams() {
		Map<String, Object> params = new HashMap<>();
		params.put("serverUrl", configurationService.getServerUrl());
		params.put("monLocTitle", configurationService.getMonLocTitle());
		params.put("monLocDescription", configurationService.getMonLocDescription());
		params.put("monLocContactName", configurationService.getMonLocContactName());
		params.put("monLocContactEmail", configurationService.getMonLocContactEmail());

		return params;
	}
}