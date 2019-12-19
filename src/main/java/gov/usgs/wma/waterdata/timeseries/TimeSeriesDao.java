package gov.usgs.wma.waterdata.timeseries;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TimeSeriesDao extends SqlSessionDaoSupport {

	@Autowired
	public TimeSeriesDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public String getTimeSeries(String monitoringLocationId, String timeSeriesId) {
		return getSqlSession().selectOne("groundwaterDailyValue.getGeoJson", timeSeriesId);
	}

}
