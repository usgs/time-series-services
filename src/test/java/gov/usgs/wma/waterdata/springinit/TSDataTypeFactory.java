package gov.usgs.wma.waterdata.springinit;

import java.sql.Types;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TSDataTypeFactory extends PostgresqlDataTypeFactory {
	private static final Logger logger = LoggerFactory.getLogger(TSDataTypeFactory.class);

	public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
		logger.debug("createDataType(sqlType={}, sqlTypeName={})",
				String.valueOf(sqlType), sqlTypeName);

		if (sqlType == Types.OTHER && ("json".equals(sqlTypeName) || "jsonb".equals(sqlTypeName))) {
			return new JsonType(); // support PostgreSQL json/jsonb
		} else {
			return super.createDataType(sqlType, sqlTypeName);
		}
	}

}
