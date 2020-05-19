package gov.usgs.wma.waterdata.features;

import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FeaturesDao extends SqlSessionDaoSupport {

	private static final String NAMESPACE = "features.";

	@Autowired
	public FeaturesDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public String getCollectionFeaturesJson(Map<String, Object> params) {
		return getSqlSession().selectOne(NAMESPACE + "getFeaturesJson", params);
	}

	public String getCollectionFeatureJson(Map<String, Object> params) {
		return getSqlSession().selectOne(NAMESPACE + "getFeatureJson", params);
	}

	public int getCollectionFeatureCount(Map<String, Object> params) {
		return getSqlSession().selectOne(NAMESPACE + "getFeatureCount", params);
	}
}
