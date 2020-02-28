package com.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utilities {
	private static ReadWriteLock lock;
	private static int requestCounter = 1;
	static Logger log = Logger.getLogger(Utilities.class.getName());

	public static boolean isnumber(String checkString) {
		if (checkString.matches("[0-9]+"))
			return true;
		else
			return false;

	}

	/*	public static boolean isInRange(int startRange, int endrange, int testNumber) {
			if ((testNumber < startRange) || (testNumber > endrange)) {
				log.debug(
						"startRange [" + startRange + "] endrange [" + endrange + "] testnumber [" + testNumber + "]");
				return false;
			} else {
				log.debug(
						"startRange [" + startRange + "] endrange [" + endrange + "] testnumber [" + testNumber + "]");
				return true;
			}
		}*/
	public static boolean isInRange(int startRange, int endrange, int testNumber) {
		if ((testNumber <= startRange) || (testNumber >= endrange)) {
			/*	int local = 0;
				if(testNumber == startRange)
				{
				   	local = testNumber;
				   	if(endrange == (local+1))
				   	{
				   		return true;
				   	}
				}*/
			if (startRange + 1 == endrange) {

				if ((testNumber == startRange) || (testNumber == endrange)) {
					log.debug("Start and end range diff is 1 so return true startRange [" + startRange + "] endrange ["
							+ endrange + "] testnumber [" + testNumber + "]");
					return true;
				}
			}

			log.debug("False startRange [" + startRange + "] endrange [" + endrange + "] testnumber [" + testNumber
					+ "]");
			return false;
		} else {
			log.debug(
					"True startRange [" + startRange + "] endrange [" + endrange + "] testnumber [" + testNumber + "]");
			return true;
		}
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
			log.debug(" newGuess index  value" + o);
		}
		log.debug("arr length" + arr.length + "b length" + l_intarrayB);
		int lenarrA = arr.length;
		int lenarrB = l_intarrayB.length;
		log.debug("\n");
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

		log.debug("Length return is" + numberhole1);
		return numberhole1;

	}

	public static Formatter getRequestLenthin9digit(int length) {

		Formatter l_formater = new Formatter();
		log.debug("Length Before :" + length);
		l_formater.format("%09d", length);
		log.debug("Length after :" + l_formater);
		return l_formater;

	}

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date();
		String l_currentDateTime = dateFormat.format(date);
		log.debug(l_currentDateTime);
		return l_currentDateTime;
	}

	public static String getCurrentDateTime(String formate) {
		DateFormat dateFormat = new SimpleDateFormat(formate);
		Date date = new Date();
		String l_currentDateTime = dateFormat.format(date);
		log.debug(l_currentDateTime);
		return l_currentDateTime;
	}

	public static java.sql.Date getCurrentSqlDateTime() {

		Date date = new Date();

		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		log.debug("Returning Sql date :" + sqlDate);
		return sqlDate;
	}

	public static void jsonParserNoReturn(String str) {
		// json node to json obj to traverse each element
		JsonElement rootNode = JsonParser.parseString(str);
		JsonObject o_JsonobjRootNode = rootNode.getAsJsonObject();

		Object l_mapKey = null;
		// parsing based on the key value
		for (Object o : o_JsonobjRootNode.keySet()) {
			String l_mapVlaue = (String) o;
			// Checking key value in map in obj map
			l_mapKey = o_JsonobjRootNode.get(l_mapVlaue);
			if (l_mapKey instanceof JsonObject) {
				log.debug("Header: " + l_mapKey);
				// calling tcp connector when it is has been the object of
				// instances
				Utilities.jsonParserNoReturn(l_mapKey.toString());
			} else {
				log.debug("key: " + l_mapVlaue + " value: " + l_mapKey);

			}
		}
	}

	static Object retrnVar;

	public static Object jsonParserWithParitcularElement(String str, String value) {
		// json node to json obj
		JsonElement rootNode = JsonParser.parseString(str);
		JsonObject o_JsonobjRootNode = rootNode.getAsJsonObject();

		Object l_mapKey = null;
		for (Object o : o_JsonobjRootNode.keySet()) {
			String l_mapVlaue = (String) o;

			l_mapKey = o_JsonobjRootNode.get(l_mapVlaue);
			if (l_mapKey instanceof JsonObject) {

				if (l_mapVlaue.equalsIgnoreCase(value)) {

					log.debug("Comparing \"" + value + "\" with key: " + l_mapVlaue + " Map value: "
							+ l_mapKey.toString() + " is Json Elemnt with child node ");
					retrnVar = l_mapKey;
					l_mapKey = null;

				} else {
					// log.debug("Header: " + l_mapKey);
					Utilities.jsonParserWithParitcularElement(l_mapKey.toString(), value);
				}

			} else {

				if (l_mapVlaue.equalsIgnoreCase(value)) {
					log.debug("keystr is " + l_mapVlaue);
					log.debug("Comparing \"" + value + "\" with key: " + l_mapVlaue + " value: " + l_mapKey.toString());
					retrnVar = l_mapKey.toString();
					l_mapKey = null;
					break;

				}

			}
		}
		return retrnVar;
	}

	public static int compareDate(String date1, String date2) throws ParseException {
		// if received date1 is less then receive date2 then
		// -negative -1 response will get
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date received_Date1 = sdf.parse(date1);
		Date received_Date2 = sdf.parse(date2);
		// if received date1 is grater then receive date2 then
		// +positive 1 response will get

		// both are equal 0 will come
		log.debug(
				"Comapring date Received Date1 [" + received_Date1 + "] with Received Date2 [" + received_Date2 + "]");

		return received_Date1.compareTo(received_Date2);
	}

	public synchronized static void addAPIinMDCtologger(String Element) {
		Formatter l_formater = new Formatter();
		l_formater.format("%010d", requestCounter);
		MDC.put("API", Element + ":" + l_formater);
		requestCounter++;
		l_formater.close();
	}

	public synchronized static void addTxIdinMDCtologger(String TxId) {
		Formatter l_formater = new Formatter();

		MDC.put("Tx-Id", TxId);

		l_formater.close();
	}
}
