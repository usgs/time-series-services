package gov.usgs.wma.waterdata.collections;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.parameter.BoundingBox;

@Component
public class CollectionParams {

	public static final String  PARAM_COLLECTION_ID   = "collectionId";
	public static final String  PARAM_FEATURE_ID      = "featureId";
	public static final String  PARAM_TIME_SERIES_ID  = "timeSeriesId";
	public static final String  PARAM_SERVER_URL      = "serverUrl";
	public static final String  PARAM_LIMIT      = "limit";
	public static final String  PARAM_START_INDEX      = "startIndex";
	public static final String  PARAM_POINT_LOW_lEFT      = "pointLowLeft";
	public static final String  PARAM_POINT_UP_RIGHT      = "pointUpRight";
	public static final String  PARAM_PREV_START_INDEX      = "prevStartIndex";
	public static final String  PARAM_NEXT_START_INDEX      = "nextStartIndex";
	public static final String  PARAM_MON_lOC_TITLE         = "monLocTitle";
	public static final String  PARAM_MON_LOC_DESCRIPTION   = "monLocDescription";
	public static final String  PARAM_MON_LOC_CONTACT_NAME  = "monLocContactName";
	public static final String  PARAM_MON_LOC_CONTACT_EMAIL = "monLocContactEmail";

	public static final String  DEFAULT_COLLECTION_ID = "monitoring-locations";
	public static final Integer DEFAULT_START_INDEX   = 0;
	public static final Integer MAX_LIMIT     = 10000;

	protected ConfigurationService configurationService;

	@Autowired
	public CollectionParams(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

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
			params.put(PARAM_COLLECTION_ID, collectionId);
		}
		if (featureId != null) {
			params.put(PARAM_FEATURE_ID, featureId);
		}

		return params;
	}

	public Map<String, Object> buildParams(String collectionId, int limit, int startIndex,
			BoundingBox bbox, int count) {
		Map<String, Object> params = buildCommonParams();
		if (collectionId != null) {
			params.put(PARAM_COLLECTION_ID, collectionId);
		}
		int limitParam = limit;
		if (limit > MAX_LIMIT) {
			limitParam = MAX_LIMIT;
		}
		params.put(PARAM_LIMIT, limitParam);
		/*
		 * a bounding box with four numbers: latitude Lower left corner, longitude Lower left
		 * corner, latitude Upper right corner, longitude Upper right corner, latitude
		 */
		if (bbox != null) {
			params.put(PARAM_POINT_LOW_lEFT, String.format("Point(%s %s)", bbox.getWest(), bbox.getSouth()));
			params.put(PARAM_POINT_UP_RIGHT, String.format("Point(%s %s)", bbox.getEast(), bbox.getNorth()));
		}
		params.put(PARAM_START_INDEX, startIndex);
		if (startIndex > 0) {
			int prevStartIndex = Math.max(0, startIndex - limitParam);
			params.put(PARAM_PREV_START_INDEX, String.format("&startIndex=%d&limit=%d", prevStartIndex, limitParam));
		}

		int nextStartIndex = startIndex + limitParam;
		if (nextStartIndex <= count - 1) {
			params.put(PARAM_NEXT_START_INDEX, String.format("&startIndex=%d&limit=%d", nextStartIndex, limitParam));
		}

		return params;
	}

	private Map<String, Object> buildCommonParams() {
		Map<String, Object> params = new HashMap<>();
		params.put(PARAM_SERVER_URL, configurationService.getServerUrl());
		params.put(PARAM_MON_lOC_TITLE, configurationService.getMonLocTitle());
		params.put(PARAM_MON_LOC_DESCRIPTION, configurationService.getMonLocDescription());
		params.put(PARAM_MON_LOC_CONTACT_NAME, configurationService.getMonLocContactName());
		params.put(PARAM_MON_LOC_CONTACT_EMAIL, configurationService.getMonLocContactEmail());

		return params;
	}

	public Map<String, Object> buildParams(String collectionId, String featureId, String timeSeriesId) {
		Map<String, Object> params = buildParams(collectionId, featureId);
		params.put(PARAM_TIME_SERIES_ID, timeSeriesId);
		
		return params;
	}
}