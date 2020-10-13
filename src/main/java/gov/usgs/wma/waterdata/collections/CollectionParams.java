package gov.usgs.wma.waterdata.collections;

import java.util.HashMap;
import java.util.List;
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
	public static final String PARAM_SITE_ACTIVE = "active";
	public static final String PARAM_COUNTRIES = "countries";
	public static final String PARAM_COUNTIES = "counties";
	public static final String PARAM_STATES = "states";
	public static final String PARAM_HYDROLOGIC_UNITS = "hydrologicUnits";
	public static final String PARAM_NATIONAL_AQUIFER_CODES = "nationalAquiferCodes";
	public static final String PARAM_AGENCY_CODES = "agencyCodes";
	public static final String PARAM_MONITORING_LOCATION_NUMBER = "monitoringLocationNumbers";
	public static final String PARAM_MONITORING_LOCATION_TYPE = "monitoringLocationType";
	public static final String PARAM_FILTER_OPTIONS = "filterOptions";

	public static final String  DEFAULT_COLLECTION_ID = "monitoring-locations";
	public static final Integer DEFAULT_START_INDEX   = 0;
	public static final Integer MAX_LIMIT     = 10000;

	protected ConfigurationService configurationService;

	@Autowired
	public CollectionParams(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public Builder builder() {
		return new Builder(configurationService);
	}

	public static class Builder {
		private String collectionId;
		private String featureId;
		private String timeSeriesId;
		private ConfigurationService configurationService;
		private BoundingBox bbox;
		private int limitParam = MAX_LIMIT;
		private int count = 0;
		private int startIndex = 0;
		private Boolean active; 
		private List<String> countries;
		private List<String> counties;
		private List<String> states;
		private List<String> hydrologicUnits;
		private List<String> nationalAquiferCodes;
		private List<String> agencyCodes;
		private List<String> monitoringLocationType;
		private List<String> monitoringLocationNumbers;
		private boolean isPaging = false;
		private String filterOptions = "";
		
		public Builder(ConfigurationService configurationService) {
			this.configurationService = configurationService;
		}
		
		public Builder countries(List<String> countries) {
			this.countries = countries;
			return this;
		}

		public Builder counties(List<String> counties) {
			this.counties = counties;
			return this;
		}

		public Builder states(List<String> states) {
			this.states = states;
			return this;
		}

		public Builder active(Boolean active) {
			this.active = active;
			return this;
		}

		public Builder hydrologicUnits(List<String> hydrologicUnits) {
			this.hydrologicUnits = hydrologicUnits;
			return this;
		}
		
		public Builder agencyCodes(List<String> agencyCodes) {
			this.agencyCodes = agencyCodes;
			return this;
		}
		
		public Builder nationalAquiferCodes(List<String> nationalAquiferCodes) {
			this.nationalAquiferCodes = nationalAquiferCodes;
			return this;
		}
		
		public Builder monitoringLocationNumbers(List<String> monitoringLocationNumbers) {
			this.monitoringLocationNumbers = monitoringLocationNumbers;
			return this;
		}


		public Builder monitoringLocationType(List<String> monitoringLocationType) {
			this.monitoringLocationType = monitoringLocationType;
			return this;
		}

		public Builder collectionId(String collectionId) {
			this.collectionId = collectionId;
			return this;
		}

		public Builder featureId(String featureId) {
			this.featureId = featureId;
			return this;
		}

		public Builder timeSeriesId(String timeSeriesId) {
        		this.timeSeriesId = timeSeriesId;
        		return this;
		}

		public Builder bbox(BoundingBox bbox) {
        		this.bbox = bbox;
        		return this;
		}

		public Builder paging(int limit, int startIndex, int count) {
        		this.isPaging = true;
        		this.limitParam = limit;
			if (limit > MAX_LIMIT) {
				this.limitParam = MAX_LIMIT;
			}
			this.startIndex = startIndex;
			this.count = count;
			return this;
		}

		private Map<String, Object> buildNullableItem(Map<String, Object> params, String key, Object value) {
        		if (value != null) {
        			params.put(key, value);
				}
        		return params;
		}

		public Map<String, Object> build() {
			Map<String, Object> params = new HashMap<>();
			params.put(PARAM_SERVER_URL, configurationService.getServerUrl());
			params.put(PARAM_MON_lOC_TITLE, configurationService.getMonLocTitle());
			params.put(PARAM_MON_LOC_DESCRIPTION, configurationService.getMonLocDescription());
			params.put(PARAM_MON_LOC_CONTACT_NAME, configurationService.getMonLocContactName());
			params.put(PARAM_MON_LOC_CONTACT_EMAIL, configurationService.getMonLocContactEmail());

			params = buildNullableItem(params, PARAM_COLLECTION_ID, collectionId);
			params = buildNullableItem(params, PARAM_FEATURE_ID, featureId);
			params = buildNullableItem(params, PARAM_TIME_SERIES_ID, timeSeriesId);

			if (isPaging) {
				params.put(PARAM_LIMIT, limitParam);
				params.put(PARAM_START_INDEX, startIndex);
				if (startIndex > 0) {
					int prevStartIndex = Math.max(0, startIndex - limitParam);
					params.put(PARAM_PREV_START_INDEX, String.format("&startIndex=%d&limit=%d", prevStartIndex, limitParam));
				}

				int nextStartIndex = startIndex + limitParam;
				if (nextStartIndex <= count - 1) {
					params.put(PARAM_NEXT_START_INDEX, String.format("&startIndex=%d&limit=%d", nextStartIndex, limitParam));
				}
			}

			if (bbox != null) {
				params.put(PARAM_POINT_LOW_lEFT, String.format("Point(%s %s)", bbox.getWest(), bbox.getSouth()));
				params.put(PARAM_POINT_UP_RIGHT, String.format("Point(%s %s)", bbox.getEast(), bbox.getNorth()));
			}

			params = buildFilterableList(params, PARAM_COUNTRIES, countries, "&country=");
			params = buildFilterableList(params, PARAM_COUNTIES, counties, "&county=");
			params = buildFilterableList(params, PARAM_STATES, states, "&state=");
			params = buildFilterableList(params, PARAM_HYDROLOGIC_UNITS, hydrologicUnits, "&hydrologicUnit=");
			params = buildFilterableList(params, PARAM_NATIONAL_AQUIFER_CODES, nationalAquiferCodes, "&nationalAquiferCode=");
			params = buildFilterableList(params, PARAM_AGENCY_CODES, agencyCodes, "&agencyCode=");
			params = buildFilterableBool(params, PARAM_SITE_ACTIVE, active, "&active=");
			params = buildFilterableList(params, PARAM_MONITORING_LOCATION_NUMBER, monitoringLocationNumbers, "&monitoringLocationNumber=");
			params = buildFilterableList(params, PARAM_MONITORING_LOCATION_TYPE, monitoringLocationType, "&monitoringLocationType=");

			params.put(PARAM_FILTER_OPTIONS, filterOptions);
			return params;
		}

		private Map<String, Object> buildFilterableList(
			Map<String, Object> params, String key, List<String> myList,
			String urlJoiner) {

			if (myList != null) {
				params.put(key, myList);
				for (String item: myList) {
					String addition = urlJoiner + item;
					if (!filterOptions.contains(addition)) {
						filterOptions += addition;
					}
				}
			}
			return params;
		}


		private Map<String, Object> buildFilterableBool(
			Map<String, Object> params, String key, Boolean flag,
			String urlJoiner) {

			if (flag != null) {
				params.put(key, (Object) flag);
				
					String addition = urlJoiner + flag.toString();
					if (!filterOptions.contains(addition)) {
						filterOptions += addition;
					}
					
				
			}
			
			return params;
		}
	}
}

