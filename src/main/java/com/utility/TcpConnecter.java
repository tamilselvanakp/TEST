package com.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Formatter;

import org.apache.log4j.Logger;

import com.exception.handler.CustomException;

public class TcpConnecter {
	static Logger log = Logger.getLogger(TcpConnecter.class.getName());

	// static String strRequestString = "";
	// static String newStringFormat = "";
	// static int retry = 1;
	// static String prefix = "";
	static CustomException o_customexception = new CustomException();

	static String Responsestr = "";

	public static String sendTcpClientRequest(final String TcpIP, final int TcpPort, String Channel_name,
			String entityName, final String xml_ip, final boolean isReqcontainRegrequest)
			throws NumberFormatException, SocketTimeoutException, IOException, Exception {

		if (Channel_name.equalsIgnoreCase("HLR")) {
			Responsestr = "";
			log.debug("Inside sendTcpClientRequest");
			/*sendTcpExceuter.execute(new Runnable()*/

			try {

				return Responsestr = makeTCPconnection(TcpIP.trim(), TcpPort, xml_ip, isReqcontainRegrequest);

			} catch (NumberFormatException | IOException e) {

				e.printStackTrace();
				log.error("!!!!!!!!!!!! Execption" + e.getMessage() + "----" + e.getCause());
				return Responsestr = "";

			}

		} else

		{
			log.debug("Channel name should be HLR");
			return "";

		}

	}

	private static String makeTCPconnection(String TcpIP, int TcpPort, String strRequestString, Boolean regReqFlag)
			throws NumberFormatException, IOException, SocketTimeoutException {
		int noOfReq = 0;
		int reqLength = 0;
		String p_Response = "";
		InputStreamReader reader = null;
		InputStream in = null;
		Socket Client = null;
		boolean IsSocketCreated = false;
		OutputStream outToServer = null;
		String regRequestStr = "<REGISTRATION_REQUEST><HEADER><ENTITY_NAME>Tamil</ENTITY_NAME><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER></REGISTRATION_REQUEST>";

		Client = new Socket();
		Client.setTcpNoDelay(true);
		SocketAddress sockaddr = null;
		sockaddr = new InetSocketAddress(TcpIP.trim(), TcpPort);
		log.debug("connecting to ..." + sockaddr.toString());
		log.debug("Sending request with count  to ...[ " + noOfReq + "]");
		if (regReqFlag == true) {

			noOfReq = 2;
		} else {
			noOfReq = 1;
		}

		// try {
		Client.connect(sockaddr, 1000);
		log.info("Client name getInetAddress:" + Client.getInetAddress());
		if (Client.isConnected()) {
			IsSocketCreated = true;
			log.debug("connection established");

			/*}
			} catch (Exception e) {
			log.debug("Exception while creating socket,Reason is:" + e.getMessage());
			
			}*/

			if (IsSocketCreated) {
				// try {
				outToServer = Client.getOutputStream();
				for (int j = 0; j < noOfReq; j++) {
					if (j == 0 && noOfReq == 2) {
						log.info("Client name getInetAddress 1 :" + Client.getInetAddress());
						reqLength = regRequestStr.length();
						Formatter lenString = Utilities.getRequestLenthin9digit(reqLength);
						regRequestStr = lenString + regRequestStr;
						log.debug("Request to:" + regRequestStr);
						log.debug("Sending Req Len :" + regRequestStr.getBytes().length);
						outToServer.write(regRequestStr.getBytes());
						outToServer.flush();
						Client.setSoTimeout(10000);
						Client.setKeepAlive(true);
						in = Client.getInputStream();
						reader = new InputStreamReader(in);

						// log.debug("bef while" +
						// Client.getReceiveBufferSize());
						char[] cbuf1 = new char[Client.getReceiveBufferSize() + 1];
						reader.read(cbuf1, 0, 9);

						String resLength = "";
						for (char a : cbuf1) {
							resLength += a;
						}
						log.info("Client name getInetAddress 2:" + Client.getInetAddress());
						int intResLength = Integer.parseInt(resLength.trim());

						log.debug("resLength [" + intResLength + "]");

						char[] cbuf2 = new char[Client.getReceiveBufferSize() + 1];
						reader.read(cbuf2, 0, intResLength);
						/*int i = -1;
						 * while ((i = in.read()) > -1) {
							log.debug(i);
							p_Response += (char) i;
						
						}*/

						for (char a : cbuf2) {
							p_Response += a;

						}

						log.debug("DATA IS READ FROM CHANNELS SUCCESFULY,Actual response is : [" + p_Response.trim()
								+ "]");
						p_Response = "";
					} else {
						log.info("Client name getInetAddress 4:" + Client.getInetAddress());
						log.debug("XML Request to send [" + strRequestString + "]");
						reqLength = strRequestString.length();
						Formatter lenString = Utilities.getRequestLenthin9digit(reqLength);
						strRequestString = lenString + strRequestString;
						log.debug("Request to:" + strRequestString);
						outToServer.write(strRequestString.getBytes());
						outToServer.flush();
						Client.setSoTimeout(10000);
						in = Client.getInputStream();
						reader = new InputStreamReader(in);

						log.debug("bef while" + Client.getReceiveBufferSize());
						char[] cbuf1 = new char[Client.getReceiveBufferSize() + 1];
						reader.read(cbuf1, 0, 9);

						String resLength = "";
						for (char a : cbuf1) {
							resLength += a;
						}

						log.debug("Received data from server [" + resLength.trim() + "]");

						int intResLength = 0;
						try {
							intResLength = Integer.parseInt(resLength.trim());
						} catch (NumberFormatException e) {
							log.debug("server closed the Socket connection so read Len:" + e.getMessage());
							return "";
						}

						log.debug("resLength [" + intResLength + "]");

						char[] cbuf2 = new char[Client.getReceiveBufferSize() + 1];
						reader.read(cbuf2, 0, intResLength);
						/*int i = -1;
						 * while ((i = in.read()) > -1) {
							log.debug(i);
							p_Response += (char) i;
						
						}*/

						for (char a : cbuf2) {
							p_Response += a;

						}
						log.info("Client name getInetAddress: 5" + Client.getInetAddress());

						log.debug("DATA IS READ FROM CHANNELS SUCCESFULY,Actual response is : [" + p_Response.trim()
								+ "]");

					}
				}

				/*} catch (Exception e) {
					log.error(e.getStackTrace());;
				
				}*/
			} else {
				log.debug("connection not established");
			}

		}
		try {

		}

		finally {
			log.debug("Closed connection");
			Client.close();
		}
		return p_Response;
	}
}
