package gov.usgs.wma.waterdata.timeseries;

import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.usgs.wma.waterdata.collections.CollectionParams;

@Repository
public class TimeSeriesDao extends SqlSessionDaoSupport {

	private CollectionParams collectionsParams;

	@Autowired
	public TimeSeriesDao(SqlSessionFactory sqlSessionFactory, CollectionParams collectionsParams) {
		setSqlSessionFactory(sqlSessionFactory);
		this.collectionsParams = collectionsParams;
	}

	public String getTimeSeries(String featureId, String timeSeriesId) {
		return getTimeSeries(CollectionParams.DEFAULT_COLLECTION_ID, featureId, timeSeriesId);
	}

	public String getTimeSeries(String collectionId, String featureId, String timeSeriesId) {
		Map<String,Object> params = collectionsParams.builder().collectionId(collectionId)
		.featureId(featureId).timeSeriesId(timeSeriesId).build();
		return getSqlSession().selectOne("groundwaterDailyValue.getGeoJson", params);
	}

	public String getTimeSeriesWaterML(String featureId, String bestTS) {
		Map<String, Object> params = collectionsParams.builder().collectionId(CollectionParams.DEFAULT_COLLECTION_ID)
				.featureId(featureId).timeSeriesId(CollectionParams.PARAM_MATCH_ANY).bestTS(bestTS).build();
		return getSqlSession().selectOne("groundwaterDailyValueWaterML.getWaterML", params);
	}

	public String getTimeSeriesWaterML(String collectionId, String featureId, String timeSeriesId) {
		Map<String,Object> params = collectionsParams.builder().collectionId(collectionId)
		.featureId(featureId).timeSeriesId(timeSeriesId).bestTS(CollectionParams.PARAM_MATCH_ANY).build();
		return getSqlSession().selectOne("groundwaterDailyValueWaterML.getWaterML", params);
	}

	public String getStatisticalTimeSeries(String collectionId, String featureId) {
		Map<String,Object> params = collectionsParams.builder().collectionId(collectionId)
		.featureId(featureId).build();
		return getSqlSession().selectOne("timeSeries.getStatisticalTimeSeriesJson", params);
	}
}
