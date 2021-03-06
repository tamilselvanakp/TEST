package com.dob.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.exception.handler.CustomException;

import oracle.jdbc.OracleTypes;

public class GetSubscriberinfoDob {
	static Logger log = Logger.getLogger(GetSubscriberinfoDob.class.getName());
	String resultopxml;
	CustomException o_customexception = new CustomException();
	HashMap<String, String> l_map = new HashMap<>(5);

	public HashMap<String, String> get_mboss_iface(Connection l_conn, String p_request_type, String XML_input,
			String User, String TransRefId, String BulkTransRef) throws SQLException {

		log.debug(" IP p_request_type" + p_request_type);
		// log.debug(" IP XML_input" + XML_input);
		// log.debug(" IP User" + User);
		// log.debug(" IP TransRefId" + TransRefId);
		// log.debug(" IP BulkTransRef" + BulkTransRef);

		CallableStatement l_clable = l_conn.prepareCall(
				"{call hlr_pkg_xml_request.mboss_iface(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		log.debug("CallableStatement ");
		l_clable.setString(1, p_request_type);
		l_clable.setString(2, XML_input);
		l_clable.setString(3, User);
		l_clable.setString(4, TransRefId);
		l_clable.setString(5, BulkTransRef);
		l_clable.registerOutParameter(6, Types.NUMERIC);// p_xml_cur_count
		l_clable.registerOutParameter(7, OracleTypes.CURSOR);// p_xml_cur

		l_clable.registerOutParameter(8, Types.VARCHAR);// p_xml_response
		l_clable.registerOutParameter(9, Types.VARCHAR);// p_subs_identity_imsi

		l_clable.registerOutParameter(10, Types.VARCHAR);
		// p_subs_identity_msisdn
		l_clable.registerOutParameter(11, Types.VARCHAR);// p_subs_identity_flag

		l_clable.registerOutParameter(12, Types.VARCHAR);// p_free_format_tdr_data
		l_clable.registerOutParameter(13, Types.NUMERIC);// p_continue_op_code

		l_clable.registerOutParameter(14, Types.NUMERIC);// p_subs_count
		l_clable.registerOutParameter(15, Types.VARCHAR);// p_primary_imsi

		l_clable.registerOutParameter(16, Types.VARCHAR);// p_primary_msisdn
		l_clable.registerOutParameter(17, Types.VARCHAR);// p_trap_code

		l_clable.registerOutParameter(18, Types.VARCHAR);// p_trap_desc
		l_clable.registerOutParameter(19, Types.NUMERIC);// p_network_id

		l_clable.registerOutParameter(20, Types.NUMERIC);// p_error_code
		l_clable.registerOutParameter(21, Types.NUMERIC);// p_helperOpCode

		l_clable.registerOutParameter(22, Types.VARCHAR);// p_helperxml
		l_clable.registerOutParameter(23, Types.VARCHAR);// p_account_id

		l_clable.registerOutParameter(24, Types.VARCHAR);// p_icc_id
		l_clable.registerOutParameter(25, Types.VARCHAR);// p_imei

		l_clable.registerOutParameter(26, Types.VARCHAR);// p_error_msg
		l_clable.registerOutParameter(27, Types.VARCHAR);// p_log_message

		Boolean execte = l_clable.execute();
		log.debug("Is Procedure Called " + execte);

		/*log.debug(
				"p_xml_cur_count  p_xml_response p_subs_identity_imsi p_subs_identity_msisdn p_subs_identity_flag");
		
		System.out.print(" p_free_format_tdr_data p_continue_op_code \n");*/
		int error_code = l_clable.getInt(20);
		l_map.put("error_code", Integer.toString(error_code));

		String error_desc = l_clable.getString(26);
		l_map.put("error_desc", error_desc);
		log.debug("Result is" + l_clable.getInt(6));
		if (l_clable.getString(8) != null) {

			/*
			resultopxml = "<" + p_request_type + "_RESPONSE>" + l_clable.getString(8) + "</" + p_request_type
					+ "_RESPONSE>";*/

			resultopxml = "<Body>" + l_clable.getString(8) + "</Body>";

		} else {
			/*resultopxml = "<" + p_request_type + "_RESPONSE>" + l_clable.getString(8) + "<" + p_request_type
					+ "_RESPONSE>";*/

			/*resultopxml = "<" + p_request_type + "_RESPONSE><ERROR><ERROR_CODE>" + l_clable.getInt(20)
					+ "</ERROR_CODE><ERROR_DEC>" + l_clable.getString(26) + "</ERROR_DEC></ERROR></"
					+ p_request_type + "_RESPONSE>";*/
			/*resultopxml = "<ERROR><ERROR_CODE>" + error_code + "</ERROR_CODE><ERROR_DEC>" + error_desc
					+ "</ERROR_DEC></ERROR>";*/
			resultopxml = "";

		}
		l_map.put("Body", resultopxml);

		log.info("XML Response [" + resultopxml + "]");

		log.debug("TDR data  is [" + l_clable.getString(12) + "]");
		if (l_clable.getString(17) != null) {

			log.error("Trap code data  is [" + l_clable.getString(17) + "]");

			log.error("Trap desc  is [" + l_clable.getString(18) + "]");
		}
		log.error("error log is [" + l_clable.getString(27) + "]");
		log.debug("Map data is :" + l_map);
		l_clable.close();
		return l_map;

	}
}
