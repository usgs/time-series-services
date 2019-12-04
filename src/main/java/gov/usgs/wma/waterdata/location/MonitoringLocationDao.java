package gov.usgs.wma.waterdata.location;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.usgs.wma.waterdata.location.geojson.GeoJSON;

@Repository
public class MonitoringLocationDao extends SqlSessionDaoSupport {

	@Autowired
	public MonitoringLocationDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public GeoJSON getLocation(String id) {
		return getSqlSession().selectOne("monitoringLocation.getGeoJson", id);
	}
}
