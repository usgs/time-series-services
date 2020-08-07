package gov.usgs.wma.waterdata.collections;

import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CollectionsDao extends SqlSessionDaoSupport {
	@Autowired
	public CollectionsDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
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
		Set<String> keys = params.keySet();
		if (keys.contains("nationalAquiferCode")) {
			for (String key: keys) {
				System.err.println("COUNT " + key + " " + params.get(key));
			}
		}

		return getSqlSession().selectOne("features.getCollectionFeatureCount", params);
	}

	public String getObsverationsJson(Map<String, Object> params) {
		return getSqlSession().selectOne("observations.getObsverationsJson", params);
	}

	public String getDiscreteDataObsverationsJson(Map<String, Object> params) {
		return getSqlSession().selectOne("observations.getDiscreteDataObsverationsJson", params);
	}

}
