package gov.usgs.wma.waterdata.collections;

import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CollectionsDao extends SqlSessionDaoSupport {
	public static final String NULL_TIME_SERIES = "\"timeSeries\":null";
	public static final String EMPTY_TIME_SERIES= "\"timeSeries\":[]";

	private CollectionParams collectionsParams;
	
	
	@Autowired
	public CollectionsDao(SqlSessionFactory sqlSessionFactory, CollectionParams collectionsParams) {
		setSqlSessionFactory(sqlSessionFactory);
		this.collectionsParams = collectionsParams;
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
		Map<String,Object> params = collectionsParams.buildParams(collectionId, featureId);
		
		String json = getSqlSession().selectOne("collections.getStatisticalTimeSeriesJson", params);
		
		if (json != null && json.contains(NULL_TIME_SERIES)) {
			json = json.replace(NULL_TIME_SERIES, EMPTY_TIME_SERIES);
		}
		return json;
	}
}
