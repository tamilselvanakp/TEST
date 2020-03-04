package com.dob.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.utility.Utilities;

public class ImgDbQueryExecutor {
	static Logger log = Logger.getLogger(Utilities.class.getName());

	public static Map<String, String> querySubscriberdetailsInDb(Connection l_conn, String l_imsi, String l_msisdn,
			String l_iccid) throws SQLException {
		Map<String, String> l_map = new LinkedHashMap<>();
		String l_queryvar = "";
		if (!l_imsi.isEmpty()) {
			log.debug("IMSI is not empty");
			l_queryvar = "IMSI = " + "'" + l_imsi + "'";

		} else if (!l_iccid.isEmpty()) {
			log.debug("l_iccid is not empty");
			l_queryvar = "ICC_ID = " + "'" + l_iccid + "'";

		} else if (!l_msisdn.isEmpty()) {
			log.debug("l_msisdn is not empty");
			l_queryvar = "MSISDN = " + "'" + l_msisdn + "'";
		} else {
			log.debug("Pass Atleast one element");
			return null;
		}

		String queryStr = "SELECT IMSI,ICC_ID,THIRD_PARTY_IMSI,THIRD_PARTY_ICC_ID,NETWORK_ID,STATUS,MSISDN,MSISDN_UPDATED_DATE,LIFE_CYCLE_STATE,ZIP_CODE,CURRENT_PACKAGE,PORTIN_REQUEST_ID,FORMAT_TYPE,WPS,OVERRIDE_WPS_FLAG,OVERRIDE_WPS from IMG_SUBS_ACCOUNT where "
				+ l_queryvar;

		log.debug("Query Str :" + queryStr);
		Statement l_Statement = l_conn.createStatement();
		ResultSet l_restltst = l_Statement.executeQuery(queryStr);

		ResultSetMetaData l_metadata = l_restltst.getMetaData();
		while (l_restltst.next()) {
			for (int i = 1; i <= l_metadata.getColumnCount(); i++) {
				String key = l_metadata.getColumnName(i);
				String value = l_restltst.getString(key);
				l_map.put(key, value);

				log.debug(key + " [" + value + "]");
			}

		}
		log.debug("\n");
		return l_map;

	}

	static String queryForUpdate;

	public static void updateoverRideWPSInDb(Connection l_conn, String l_imsi, int l_OverRideWPSFlag,
			String l_OverRideWPS) throws SQLException {

		// update IMG_SUBS_ACCOUNT set WPS_OVERRIDDEN_DATE =SYSDATE ,
		// OVERRIDE_WPS = null,OVERRIDE_WPS_FLAG='0' where imsi =
		// '985641274200001';

		if (l_OverRideWPSFlag == 1) {

			queryForUpdate = "update IMG_SUBS_ACCOUNT set WPS_OVERRIDDEN_DATE =SYSDATE, OVERRIDE_WPS = '"
					+ l_OverRideWPS + "' ,OVERRIDE_WPS_FLAG = '" + l_OverRideWPSFlag + "'  where imsi = '" + l_imsi
					+ "'";

		} else {
			queryForUpdate = "update IMG_SUBS_ACCOUNT set WPS_OVERRIDDEN_DATE =SYSDATE, OVERRIDE_WPS = " + null
					+ " ,OVERRIDE_WPS_FLAG = '" + l_OverRideWPSFlag + "'  where imsi = '" + l_imsi + "'";
		}
		log.debug(queryForUpdate);
		Statement l_statmt = l_conn.createStatement();

		int oprst = l_statmt.executeUpdate(queryForUpdate);

		log.debug("No of rows affected [" + oprst + "]");

	}

}
