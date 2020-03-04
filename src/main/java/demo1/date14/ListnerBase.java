package demo1.date14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.glassfish.jersey.internal.guava.Multimap;
import org.w3c.dom.Document;

import com.utility.XmlParser;

@WebListener
public class ListnerBase extends Baseclass implements ServletContextListener {
	ServletContext ctx;
	public Connection db_conn;

	public Connection imgdb_conn;

	public void contextInitialized(ServletContextEvent event) {

		try {
			File l_configFile = new File(
					"D:\\LEARNING\\jar files\\pdf\\workspace\\date15\\Configuration\\ITconfig.xml");
			BufferedReader l_buffreader = new BufferedReader(new FileReader(l_configFile));
			String st;
			String l_filebuff = "";
			while ((st = l_buffreader.readLine()) != null) {
				l_filebuff += st;
			}
			l_buffreader.close();
			ctx = event.getServletContext();

			log.debug(l_filebuff.trim());
			Document l_configdoc = XmlParser.configReader(l_filebuff);
			o_singleton.setConfigReaderDocument(l_configdoc);
			Multimap<String, String> l_HashMap = XmlParser.getxmlToMultMap(l_filebuff, "");
			log.debug("l_HashMap " + l_HashMap);

			// ctx.setAttribute("StrconfigObj", l_filebuff);

		} catch (Exception e1) {

			e1.printStackTrace();

		}
		String url = "jdbc:oracle:thin:@192.168.110.204:1521:stcadb";
		String uname = "IT_HLR";
		String passwd = "lyca";
		try {
			log.debug("HLR_DB_CON 0");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			db_conn = DriverManager.getConnection(url, uname, passwd);
			log.debug("HLR_DB_CON establised");
			log.info("HLR_DB_CON establised");

			/*	// storing connection object as an attribute in ServletContext
				ctx = event.getServletContext();
				ctx.setAttribute("DB_conn", db_conn);
			
				Connection op_comn = (Connection) ctx.getAttribute("DB_conn");*/
			// Storing Db conn as Singleton variable
			o_singleton.setL_databaseConnection(db_conn);

			log.debug("Connection ob is [" + o_singleton.getL_databaseConnection() + "] has code :"
					+ o_singleton.hashCode());
		} catch (Exception e) {

			log.error(e.getMessage());
			;
			return;
		}

		String imgurl = "jdbc:oracle:thin:@192.168.110.204:1521:stcadb";
		String imguname = "IT_IMG";
		String imgpasswd = "lyca";
		try {
			log.debug("IT_IMG 0");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			imgdb_conn = DriverManager.getConnection(imgurl, imguname, imgpasswd);
			log.debug("IT_IMG establised");
			/*	// storing connection object as an attribute in ServletContext
				ctx = event.getServletContext();
				ctx.setAttribute("DB_conn", db_conn);
			
				Connection op_comn = (Connection) ctx.getAttribute("DB_conn");*/
			// Storing Db conn as Singleton variable
			o_singleton.setL_imgdatabaseConnection(imgdb_conn);

			log.debug("Connection ob is [" + o_singleton.getL_imgdatabaseConnection() + "] has code :"
					+ o_singleton.hashCode());

		} catch (Exception e) {

			log.error(e.getMessage());
			;
			return;
		}

	}

	public void contextDestroyed(ServletContextEvent arg) {
		try {
			/*ServletContext ctx = arg.getServletContext();
			Connection db_con = (Connection) ctx.getAttribute("DB_conn");*/
			// Connection db_con = getDb_conn();

			Connection db_con = o_singleton.getL_databaseConnection();
			log.debug("HLR_DB_CON closing [" + db_con + "] has code :" + o_singleton.hashCode());

			db_con.close();
			Connection imgdbcon = o_singleton.getL_imgdatabaseConnection();
			log.debug("HLR_DB_CON closing [" + imgdbcon + "] has code :" + o_singleton.hashCode());

			log.warn("***************************************************************************************");

			db_con.close();
			imgdbcon.close();

		} catch (SQLException e) {

			log.error(e.getMessage());

			return;
		}
	}

}
