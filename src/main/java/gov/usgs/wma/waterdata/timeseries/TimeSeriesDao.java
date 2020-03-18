package gov.usgs.wma.waterdata.timeseries;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TimeSeriesDao extends SqlSessionDaoSupport {

	private static final String COLLECTION_KEY = "collectionId"; // network ID
	private static final String FEATURE_KEY    = "featureId";    // location ID
	private static final String TIMESERIES_KEY = "timeseriesId"; // GUID

	@Autowired
	public TimeSeriesDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public String getTimeSeries(String featureId, String timeSeriesId) {
		return getTimeSeries("monitoring-locations", featureId, timeSeriesId);
	}

	public String getTimeSeries(String collectionId, String featureId, String timeSeriesId) {
		Map<String,String> params = new HashMap<>();
		params.put(COLLECTION_KEY, collectionId);
		params.put(FEATURE_KEY, featureId);
		params.put(TIMESERIES_KEY, timeSeriesId);
		return getSqlSession().selectOne("groundwaterDailyValue.getGeoJson", params);
	}

}
