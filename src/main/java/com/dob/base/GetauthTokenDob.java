package com.dob.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.exception.handler.CustomException;
import com.utility.Utilities;

import demo1.date14.Baseclass;
import oracle.jdbc.OracleTypes;

public class GetauthTokenDob extends Baseclass {
	public static Logger log = Logger.getLogger(GetauthTokenDob.class.getName());
	CustomException o_customexception = new CustomException();

	public boolean validateauthtoken(String authtoken) {

		if (authtoken.length() >= 20)
			return true;

		else {
			Response e_response = o_customexception.riseexceptionwithURI("Invalid Auth token", 403,
					"Please generate New auth token", "abc.com");

			throw new WebApplicationException(e_response);
		}

	}

	public static Map<String, String> validate_user(Connection l_conn, int p_networkid, String p_userid,
			String p_passwd, Date p_curr_date, int p_max_reset_attempt_count, int p_reset_attempt_time,
			int p_validate_passwd, int p_encrypt_type, int p_enable_nw, String p_deployment_flag) throws SQLException {

		p_enable_nw = 1;

		CallableStatement l_clable = l_conn.prepareCall(
				"{call hlr_pkg_user_module.validate_user(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)}");

		// PROCEDURE validate_user(
		// p_networkid IN OUT NUMBER,
		l_clable.setInt(1, p_networkid);

		// p_userid IN VARCHAR2,
		l_clable.setString(2, p_userid);
		// p_passwd IN VARCHAR2,
		l_clable.setString(3, p_passwd);
		// p_curr_date IN DATE,
		l_clable.setDate(4, p_curr_date);
		// p_max_reset_attempt_count IN NUMBER,
		l_clable.setInt(5, p_max_reset_attempt_count);
		// p_reset_attempt_time IN NUMBER,
		l_clable.setInt(6, p_reset_attempt_time);
		// p_validate_passwd IN NUMBER,
		l_clable.setInt(7, p_validate_passwd);

		// p_encrypt_type IN NUMBER,
		l_clable.setInt(8, p_encrypt_type);
		// p_enable_nw IN NUMBER,
		l_clable.setInt(9, p_enable_nw);
		// p_deployment_flag IN VARCHAR2,
		l_clable.setString(10, p_deployment_flag);
		// p_user_role OUT VARCHAR2,
		l_clable.registerOutParameter(11, Types.VARCHAR);
		// p_oss_ip_address OUT VARCHAR2,
		l_clable.registerOutParameter(12, Types.VARCHAR);

		// p_oss_ip_port OUT NUMBER,
		l_clable.registerOutParameter(13, Types.NUMERIC);
		// p_sec_access OUT NUMBER,
		l_clable.registerOutParameter(14, Types.NUMERIC);
		// p_elu_flag OUT NUMBER,

		l_clable.registerOutParameter(15, Types.NUMERIC);
		// p_user_type OUT NUMBER,
		l_clable.registerOutParameter(16, Types.NUMERIC);
		// p_cur_out OUT SYS_REFCURSOR,
		l_clable.registerOutParameter(17, OracleTypes.CURSOR);
		// p_network_name OUT VARCHAR2,
		l_clable.registerOutParameter(18, Types.VARCHAR);
		// p_rtps_ip_address OUT VARCHAR2,
		l_clable.registerOutParameter(19, Types.VARCHAR);
		// p_rtps_port OUT NUMBER,
		l_clable.registerOutParameter(20, Types.NUMERIC);
		// p_area_code_based_alloc_flag OUT NUMBER,
		l_clable.registerOutParameter(21, Types.NUMERIC);
		// p_oss_sec_ip_address OUT VARCHAR2,
		l_clable.registerOutParameter(22, Types.VARCHAR);
		// p_oss_sec_ip_port OUT NUMBER,
		l_clable.registerOutParameter(23, Types.NUMERIC);
		// p_error_code OUT NUMBER,
		l_clable.registerOutParameter(24, Types.NUMERIC);
		// p_error_msg OUT VARCHAR2);
		l_clable.registerOutParameter(25, Types.VARCHAR);
		l_clable.execute();

		int output = l_clable.getInt(24);
		String db_Result = String.format("Db op is p_network_name [%s] p_error_code [%d] p_error_msg[%s]",
				l_clable.getString(18), output, l_clable.getString(25));
		log.info(db_Result);
		Map<String, String> l_map = new HashMap<String, String>();
		l_map.put("network_name", l_clable.getString(18));
		l_map.put("error_code", Integer.toString(output));
		l_map.put("error_msg", l_clable.getString(25));
		l_clable.close();
		return l_map;

	}

	public static Map<String, String> IMG_TEST_GETAUTH(Connection l_conn, int p_client_id, String p_auth_token)
			throws SQLException {
		CallableStatement l_clable = l_conn.prepareCall("{call IMG_TEST_GETAUTH(?,?,?,?,?,?)}");
		l_clable.setInt(1, p_client_id);
		l_clable.setString(2, p_auth_token);
		l_clable.registerOutParameter(3, Types.VARCHAR);
		l_clable.registerOutParameter(4, Types.NUMERIC);
		l_clable.registerOutParameter(5, Types.NUMERIC);
		l_clable.registerOutParameter(6, Types.VARCHAR);
		l_clable.execute();
		String date = l_clable.getString(3);
		int network_id = l_clable.getInt(4);
		int errcode = l_clable.getInt(5);
		String error_msg = l_clable.getString(6);
		String db_Result = String.format(
				"Db op is ModifiedDate[%s] p_network_id [%d] p_error_code [%d] p_error_msg[%s]", date, network_id,
				errcode, error_msg);
		System.out.println(db_Result);
		Map<String, String> l_map = new HashMap<String, String>();
		l_map.put("Date", date);
		l_map.put("network_id", Integer.toString(network_id));
		l_map.put("error_code", Integer.toString(errcode));
		l_map.put("error_msg", error_msg);
		l_clable.close();
		return l_map;

	}

	/*public static void updateAuthTokeninDb(Connection l_conn, int Client_id, int Network_id, String authtoken)
			throws SQLException {
	
		String queryStr = String.format("update TEST_IMG set CLIENT_ID='" + Client_id + "', AUTH_TOKEN='" + authtoken
				+ "' where NETWORK_ID='" + 30 + "'");
		String Currentdate = Utilities.getCurrentDateTime("dd/MM/yyyy HH:mm:ss");
		String queryStr1 = String.format(
				"update TEST_IMG set  AUTH_TOKEN='%s',MODIFIED_DATE='%s' where NETWORK_ID='%d' and CLIENT_ID='%d'",
				authtoken, Currentdate, Network_id, Client_id);
		log.debug(queryStr1);
	
		log.debug("Query Str :" + queryStr);
		Statement l_Statement = l_conn.createStatement();
		int oprst = l_Statement.executeUpdate(queryStr);
	
		log.debug("No of rows Updated [" + oprst + "]");
		if (oprst == 0) {
			queryStr = String.format(
					"INSERT INTO TEST_IMG  (NETWORK_ID,CLIENT_ID, AUTH_TOKEN,MODIFIED_DATE)  VALUES  (%d, %d,%s,%s)",
					Client_id, Network_id, authtoken, Currentdate);
	
			String querystr1 = "INSERT INTO TEST_IMG  (NETWORK_ID,CLIENT_ID, AUTH_TOKEN,MODIFIED_DATE)  VALUES(?,?,?,?)";
	
			PreparedStatement stmt = l_conn.prepareStatement(querystr1);
	
			stmt.setInt(1, Network_id);
			stmt.setInt(2, Client_id);
			stmt.setString(3, authtoken);
	
			stmt.setDate(4, Utilities.getCurrentSqlDateTime());
			log.info("Query to execute" + querystr1);
	
			int res = stmt.executeUpdate();
			log.info("No of Rows Inserted :" + res);
		}
	}*/
	public static boolean updateAuthTokeninDb(Connection l_conn, int Client_id, int Network_id, String authtoken)
			throws SQLException {

		// String queryStr = String.format("update TEST_IMG set CLIENT_ID='" +
		// Client_id + "', AUTH_TOKEN='" + authtoken
		// + "' where NETWORK_ID='" + 30 + "'");

		String queryStr = "update TEST_IMG set AUTH_TOKEN=?,MODIFIED_DATE=? where NETWORK_ID=? and CLIENT_ID=?";

		/*String Currentdate = App.getCurrentDateTime("dd/MM/yyyy HH:mm:ss");
		String queryStr1 = String.format(
				"update TEST_IMG set  AUTH_TOKEN='%s',MODIFIED_DATE='%s' where NETWORK_ID='%d' and CLIENT_ID='%d'",
				authtoken, Currentdate, Network_id, Client_id);
		System.out.println(queryStr1);*/
		java.sql.Date db_sqlDate = Utilities.getCurrentSqlDateTime();
		System.out.println("UPDATING WITH authtoken, Network_id,Client_id,Date [" + authtoken + "] [" + Network_id
				+ "] [ " + Client_id + "] [" + db_sqlDate + "]");
		PreparedStatement PRPstmt = l_conn.prepareStatement(queryStr);

		PRPstmt.setString(1, authtoken);

		PRPstmt.setDate(2, db_sqlDate);
		PRPstmt.setInt(3, Network_id);
		PRPstmt.setInt(4, Client_id);

		int oprst = PRPstmt.executeUpdate();
		System.out.println("No of rows Updated [" + oprst + "]");

		if (oprst == 0) {
			/*queryStr = String.format(
					"INSERT INTO TEST_IMG  (NETWORK_ID,CLIENT_ID, AUTH_TOKEN,MODIFIED_DATE)  VALUES  (%d, %d,%s,%s)",
					Client_id, Network_id, authtoken, Currentdate);*/

			String querystr1 = "INSERT INTO TEST_IMG  (NETWORK_ID,CLIENT_ID, AUTH_TOKEN,MODIFIED_DATE)  VALUES(?,?,?,?)";

			PreparedStatement stmt = l_conn.prepareStatement(querystr1);

			stmt.setInt(1, Network_id);
			stmt.setInt(2, Client_id);
			stmt.setString(3, authtoken);

			stmt.setDate(4, db_sqlDate);
			System.out.println("Query to execute" + querystr1);
			System.out.println("Inserting WITH authtoken, Network_id,Client_id [" + authtoken + "] [" + Network_id
					+ "] [ " + Client_id + "]");
			oprst = stmt.executeUpdate();
			System.out.println("No of Rows Inserted :" + oprst);
			PRPstmt.close();
		}

		if (oprst > 0) {
			return true;
		} else {
			return false;
		}
	}

}
