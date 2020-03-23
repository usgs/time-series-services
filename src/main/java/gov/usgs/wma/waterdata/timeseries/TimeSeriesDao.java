package gov.usgs.wma.waterdata.timeseries;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.usgs.wma.waterdata.collections.CollectionsDao;

@Repository
public class TimeSeriesDao extends SqlSessionDaoSupport {

	public static final String TIMESERIES_KEY = "timeseriesId"; // GUID

	@Autowired
	public TimeSeriesDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public String getTimeSeries(String featureId, String timeSeriesId) {
		return getTimeSeries("monitoring-locations", featureId, timeSeriesId);
	}

	public String getTimeSeries(String collectionId, String featureId, String timeSeriesId) {
		Map<String,String> params = new HashMap<>();
		params.put(CollectionsDao.COLLECTION_KEY, collectionId);
		params.put(CollectionsDao.FEATURE_KEY, featureId);
		params.put(TIMESERIES_KEY, timeSeriesId);
		return getSqlSession().selectOne("groundwaterDailyValue.getGeoJson", params);
	}

}
