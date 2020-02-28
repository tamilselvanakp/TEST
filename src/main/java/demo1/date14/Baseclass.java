package demo1.date14;

import org.apache.log4j.Logger;

import com.exception.handler.CustomException;
import com.utility.ResponseSender;
import com.utility.SingletonStorage;
import com.utility.XmlParser;

public class Baseclass {
	public static Logger log = Logger.getLogger(Baseclass.class.getName());
	XmlParser l_XmlParser = new XmlParser();
	CustomException o_customexception = new CustomException();
	SingletonStorage o_singleton = SingletonStorage.getSingletonInstances();
	ResponseSender o_ResponseSender = new ResponseSender();

}
