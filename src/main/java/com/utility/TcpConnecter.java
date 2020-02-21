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

import com.exception.handler.CustomException;

public class TcpConnecter {
	static Socket Client = null;
	static SocketAddress sockaddr = null;
	static boolean IsSocketCreated = false;
	static String p_Response = "";
	static OutputStream outToServer = null;
	static InputStream in = null;
	static String strRequestString = "";
	static String newStringFormat = "";
	static int retry = 1;
	static String prefix = "";
	static CustomException o_customexception = new CustomException();
	static InputStreamReader reader = null;

	public static String sendTcpClientRequest(String TcpIP, int TcpPort, String Channel_name, String entityName,
			String xml_ip, boolean isReqcontainRegrequest)
			throws NumberFormatException, SocketTimeoutException, IOException, Exception {
		if (Channel_name.equalsIgnoreCase("HLR")) {

			UtilsVariable o_UtilsVariable = new UtilsVariable();

			System.out.println("!!!!!!!!!!!-----current data-----!!!!!!" + o_UtilsVariable.getL_date());
			return makeTCPconnection(TcpIP.trim(), TcpPort, xml_ip, isReqcontainRegrequest);

		} else {
			System.out.println("Channel name should be HLR");
			return "";

		}
	}

	private static String makeTCPconnection(String TcpIP, int TcpPort, String strRequestString, Boolean regReqFlag)
			throws NumberFormatException, IOException, SocketTimeoutException {
		int noOfReq = 0;
		int reqLength = 0;
		String regRequestStr = "<REGISTRATION_REQUEST><HEADER><ENTITY_NAME>Tamil</ENTITY_NAME><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER></REGISTRATION_REQUEST>";

		Client = new Socket();

		sockaddr = new InetSocketAddress(TcpIP.trim(), TcpPort);
		System.out.println("connecting to ..." + sockaddr.toString());
		if (regReqFlag == true) {

			noOfReq = 2;
		} else {
			noOfReq = 1;
		}

		// try {
		Client.connect(sockaddr, 1000);
		if (Client.isConnected()) {
			IsSocketCreated = true;
			System.out.println("connection established");

			/*}
			} catch (Exception e) {
			System.out.println("Exception while creating socket,Reason is:" + e.getMessage());
			
			}*/

			if (IsSocketCreated) {
				// try {
				outToServer = Client.getOutputStream();
				for (int j = 0; j < noOfReq; j++) {
					if (j == 0 && noOfReq == 2) {

						reqLength = regRequestStr.length();
						Formatter lenString = Utilities.getRequestLenthin9digit(reqLength);
						regRequestStr = lenString + regRequestStr;
						System.out.println("Request to:" + regRequestStr);
						System.out.println("Sending Req Len :" + regRequestStr.getBytes().length);
						outToServer.write(regRequestStr.getBytes());
						outToServer.flush();
						Client.setSoTimeout(10000);
						Client.setKeepAlive(true);
						in = Client.getInputStream();
						reader = new InputStreamReader(in);

						// System.out.println("bef while" +
						// Client.getReceiveBufferSize());
						char[] cbuf1 = new char[Client.getReceiveBufferSize() + 1];
						reader.read(cbuf1, 0, 9);

						String resLength = "";
						for (char a : cbuf1) {
							resLength += a;
						}
						int intResLength = Integer.parseInt(resLength.trim());

						System.out.println("resLength [" + intResLength + "]");

						char[] cbuf2 = new char[Client.getReceiveBufferSize() + 1];
						reader.read(cbuf2, 0, intResLength);
						/*int i = -1;
						 * while ((i = in.read()) > -1) {
							System.out.println(i);
							p_Response += (char) i;
						
						}*/

						for (char a : cbuf2) {
							p_Response += a;

						}

						System.out.println("DATA IS READ FROM CHANNELS SUCCESFULY,Actual response is : ["
								+ p_Response.trim() + "]");
						p_Response = "";
					} else {
						System.out.println("XML Request to send [" + strRequestString + "]");
						reqLength = strRequestString.length();
						Formatter lenString = Utilities.getRequestLenthin9digit(reqLength);
						strRequestString = lenString + strRequestString;
						System.out.println("Request to:" + strRequestString);
						outToServer.write(strRequestString.getBytes());
						outToServer.flush();
						Client.setSoTimeout(10000);
						in = Client.getInputStream();
						reader = new InputStreamReader(in);

						System.out.println("bef while" + Client.getReceiveBufferSize());
						char[] cbuf1 = new char[Client.getReceiveBufferSize() + 1];
						reader.read(cbuf1, 0, 9);

						String resLength = "";
						for (char a : cbuf1) {
							resLength += a;
						}

						System.out.println("Received data from server [" + resLength.trim() + "]");

						int intResLength = 0;
						try {
							intResLength = Integer.parseInt(resLength.trim());
						} catch (NumberFormatException e) {
							System.out.println("server closed the Socket connection so read Len:" + e.getMessage());
							return "";
						}

						System.out.println("resLength [" + intResLength + "]");

						char[] cbuf2 = new char[Client.getReceiveBufferSize() + 1];
						reader.read(cbuf2, 0, intResLength);
						/*int i = -1;
						 * while ((i = in.read()) > -1) {
							System.out.println(i);
							p_Response += (char) i;
						
						}*/

						for (char a : cbuf2) {
							p_Response += a;

						}

						System.out.println("DATA IS READ FROM CHANNELS SUCCESFULY,Actual response is : ["
								+ p_Response.trim() + "]");

						Client.close();
					}
				}

				/*} catch (Exception e) {
					e.printStackTrace();
				
				}*/
			} else {
				System.out.println("connection not established");
			}

		}
		return p_Response;
	}
}
