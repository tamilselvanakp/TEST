package com.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Formatter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.exception.handler.CustomException;

public class Bkp_TcpConnecter2 {
	static Socket socket;
	static CustomException o_customexception = new CustomException();

	public static void getTcpClient(String TcpIP, int TcpPort, String Channel_name, String entityName, String xml_ip) {
		if (Channel_name.equalsIgnoreCase("HLR")) {

			socket = makeTCPconnection(TcpIP.trim(), TcpPort);
			if (socket == null) {
				System.out.println("socket is null");
				return;
			}
			String opdata = null;

			System.out.println("Connecting to....server " + TcpIP + "... Port [" + TcpPort + "]");

			xml_ip = "<REGISTRATION_REQUEST><HEADER><ENTITY_NAME>" + entityName
					+ "</ENTITY_NAME><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER></REGISTRATION_REQUEST>";
			// String xmlLength = XmlLengthFormater(xml_ip.length());

			Formatter lengthformate = getRequestLenthin9digit(xml_ip.length());
			System.out.println("xmlLength is" + lengthformate);
			String Ip_xmlfnl = lengthformate + xml_ip;

			System.out.println("String xml Ip is :" + Ip_xmlfnl);
			try {
				opdata = TCPmessageSender(socket, Ip_xmlfnl);
			} catch (Exception e) {

				e.printStackTrace();
				Response e_response = o_customexception.riseexception("Internal server Error ", 500, e.getMessage());
				throw new WebApplicationException(e_response);
			}
			System.out.println("Received op is :" + opdata);

		} /*catch (Exception e) {
			
			e.printStackTrace();
			Response e_response = o_customexception.riseexception("internal server Error", 500, e.getMessage());
			throw new WebApplicationException(e_response);
			
			}*/

		else {
			System.out.println("Channel Name Should be HLR/hlr");
		}

	}

	public static Socket makeTCPconnection(String TcpIP, int TcpPort) {

		try {
			socket = new Socket(TcpIP, TcpPort);

		} catch (ConnectException e) {

			e.printStackTrace();
			Response e_response = o_customexception.riseexception("Internal server Error", 500, e.getMessage());
			throw new WebApplicationException(e_response);

		} catch (UnknownHostException e) {
			e.printStackTrace();
			Response e_response = o_customexception.riseexception("Internal server Error", 500, e.getMessage());
			throw new WebApplicationException(e_response);

		} catch (IOException e) {

			e.printStackTrace();
			Response e_response = o_customexception.riseexception("internal server Error", 500, e.getMessage());
			throw new WebApplicationException(e_response);

		} catch (Exception e) {

			e.printStackTrace();
			Response e_response = o_customexception.riseexception("internal server Error", 500, e.getMessage());
			throw new WebApplicationException(e_response);

		}
		return socket;

	}

	public static String TCPmessageSender(Socket socket, String xml_ip) throws Exception {

		OutputStream output = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);

		System.out.println("XML iNPUT is : [" + xml_ip.length() + "] [" + xml_ip + "]");
		writer.println(xml_ip);
		writer.flush();
		/*InputStream input = socket.getInputStream();
		InputStreamReader reader = new InputStreamReader(input);
		DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		
		char[] cbuf = new char[socket.getReceiveBufferSize() + 1];
		byte[] cbuf1 = new byte[socket.getReceiveBufferSize() + 1];
		reader.read(cbuf, 0, socket.getReceiveBufferSize());
		dis.readFully(cbuf1);
		
		int character;
		StringBuilder data = new StringBuilder();
		
		while ((character = reader.read()) != -1) {
			data.append((char) character);
		}
		String opstr = "";
		for (byte chh : cbuf1) {
			opstr = opstr + chh;
		}*/
		System.out.println("socket Length" + socket.getReceiveBufferSize());
		String opfromServer = getResponseFromServer(socket, 9);

		if (opfromServer.trim().length() == 9) {
			System.out.println("opfromServer " + opfromServer);
			int len = Integer.parseInt(opfromServer.trim());
			System.out.println("Ip Len " + len);
			opfromServer = getResponseFromServer(socket, len);
		}
		return opfromServer;

	}

	static int[] arr = new int[9];

	public static String XmlLengthFormater(int value) {
		String numberhole1 = "";
		String int2str = Integer.toString(value);
		int[] l_intarrayB = new int[int2str.length()];
		for (int i = 0; i < int2str.length(); i++) {
			l_intarrayB[i] = int2str.charAt(i) - '0';
		}
		for (int o : l_intarrayB) {
			System.out.println(" newGuess index  value" + o);
		}
		System.out.println("arr length" + arr.length + "b length" + l_intarrayB);
		int lenarrA = arr.length;
		int lenarrB = l_intarrayB.length;
		System.out.println();
		int temp = 1;
		for (int i = 0; i < l_intarrayB.length; i++) {
			System.out.print("] Arreay value A [" + arr[lenarrA - temp]);
			System.out.print("] Array value B [" + l_intarrayB[lenarrB - temp]);
			arr[lenarrA - temp] = l_intarrayB[lenarrB - temp];
			temp = temp + 1;
		}
		for (int k : arr) {

			String numberhole = Integer.toString(k);
			numberhole1 = numberhole1 + numberhole;

		}

		System.out.println("Length return is" + numberhole1);
		return numberhole1;

	}

	public static Formatter getRequestLenthin9digit(int length) {

		Formatter l_formater = new Formatter();
		System.out.println("Length Before :" + length);
		l_formater.format("%09d", length);
		System.out.println("Length after :" + l_formater);
		return l_formater;

	}

	public static String getResponseFromServer(Socket socket, int length) throws IOException {

		InputStream inputStream = socket.getInputStream();
		int received_Len = 0;
		received_Len = socket.getSendBufferSize();
		System.out.println("Expeted Len:" + received_Len);
		char[] charBuffer = new char[received_Len];
		BufferedReader l_Buffreader = new BufferedReader(new InputStreamReader(inputStream));
		// int len1=reader.read();
		// System.out.println("The Length is"+len+"["+len1);
		/*while (l_Buffreader.readLine() != null) {
			System.out.println("   " + l_Buffreader.readLine());
		}*/
		System.out.println(" trying to receive in [" + length + "] in :" + received_Len);

		l_Buffreader.read(charBuffer, 0, received_Len - 1);
		/*int c = 0;
		while ((c = inputStream.read()) != -1) {
			System.out.print((char) c);
		}*/

		String l_opStr = String.valueOf(charBuffer);

		System.out.println("Output from server" + l_opStr);
		l_Buffreader.close();

		return l_opStr;

	}

}
