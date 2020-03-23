package gov.usgs.wma.waterdata.collections;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.usgs.wma.waterdata.ConfigurationService;

@Repository
public class CollectionsDao extends SqlSessionDaoSupport {
	public static final String DEFAULT_COLLECTION_ID = "monitoring-locations";
	public static final String COLLECTION_KEY = "collectionId"; // network ID
	public static final String FEATURE_KEY    = "featureId";    // location ID
	public static final String SERVER_URL_KEY = "serverBaseURL";    // http://server:port/base/path
	public static final String NULL_TIME_SERIES = "\"timeSeries\":null";
	public static final String EMPTY_TIME_SERIES= "\"timeSeries\":[]";

	protected ConfigurationService configurationService;
	
	
	@Autowired
	public CollectionsDao(SqlSessionFactory sqlSessionFactory, ConfigurationService configurationService) {
		setSqlSessionFactory(sqlSessionFactory);
		this.configurationService = configurationService;
	}

	public String getCollectionsJson(Map<String, Object> params) {
		return getSqlSession().selectOne("collections.getCollectionsJson", params);
	}

	public String getCollectionJson(Map<String, Object> params) {
		return getSqlSession().selectOne("collections.getCollectionJson", params);
	}

	public String getCollectionFeaturesJson(Map<String, Object> params) {
		return getSqlSession().selectOne("features.getCollectionFeaturesJson", params);
	}

	public String getCollectionFeatureJson(Map<String, Object> params) {
		return getSqlSession().selectOne("features.getCollectionFeatureJson", params);
	}

	public int getCollectionFeatureCount(Map<String, Object> params) {
		return getSqlSession().selectOne("features.getCollectionFeatureCount", params);
	}

	public String getObsverationsJson(Map<String, Object> params) {
		return getSqlSession().selectOne("observations.getObsverationsJson", params);
	}

	public String getStatisticalTimeSeries(String collectionId, String featureId) {
		Map<String,String> params = new HashMap<>();
		params.put(COLLECTION_KEY, collectionId);
		params.put(FEATURE_KEY, featureId);
		params.put(SERVER_URL_KEY, configurationService.getServerUrl());
		String json = getSqlSession().selectOne("collections.getStatisticalTimeSeriesJson", params);
		if (json != null && json.contains(NULL_TIME_SERIES)) {
			json = json.replace(NULL_TIME_SERIES, EMPTY_TIME_SERIES);
		}
		return json;
	}
}
