package gov.usgs.wma.waterdata.discrete;

import gov.usgs.wma.waterdata.collections.CollectionParams;
import gov.usgs.wma.waterdata.domain.WaterMLPoint;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DiscreteDao extends SqlSessionDaoSupport {

	private CollectionParams collectionsParams;

	@Autowired
	public DiscreteDao(SqlSessionFactory sqlSessionFactory, CollectionParams collectionsParams) {
		setSqlSessionFactory(sqlSessionFactory);
		this.collectionsParams = collectionsParams;
	}

	public List<WaterMLPoint> getDiscreteGWMLPoint(String featureId) {
		Map<String, Object> params = collectionsParams.builder().featureId(featureId).build();
		return getSqlSession().selectList("discreteGWMLPoint.getDiscreteGWMLPoint", params);
	}

	public void getDiscreteGWMLPoint(String featureId, ResultHandler<WaterMLPoint> rowHandler) {

		Map<String, Object> params = collectionsParams.builder().featureId(featureId).build();
		getSqlSession().select("discreteGWMLPoint.getDiscreteGWMLPoint", params, rowHandler);
	}

}
