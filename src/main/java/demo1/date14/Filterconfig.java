package demo1.date14;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

@Provider
public class Filterconfig implements ContainerRequestFilter, ContainerResponseFilter {

	static Logger log = Logger.getLogger(Filterconfig.class.getName());
	/*public void contextInitialized(ServletContextEvent event) {
		String url = "jdbc:oracle:thin:@192.168.110.204:1521:stcadb";
		String uname = "IT_HLR";
		String passwd = "lyca";
		try {
			log.debug("HLR_DB_CON 0");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection l_conn = DriverManager.getConnection(url, uname, passwd);
			log.debug("HLR_DB_CON establised");
			// storing connection object as an attribute in ServletContext
			ServletContext ctx = event.getServletContext();
			ctx.setAttribute("DB_conn", l_conn);
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	
	}
	
	public void contextDestroyed(ServletContextEvent arg) {
		try {
			ServletContext ctx = arg.getServletContext();
			Connection db_con = (Connection) ctx.getAttribute("DB_conn");
			log.debug("HLR_DB_CON closing");
			db_con.close();
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
	}*/

	@Override
	public void filter(ContainerRequestContext reqContext) throws IOException {
		log.debug("-- req info --");
		log.debug("Req Context [" + reqContext + "]");
		log.debug("getHeaders [" + reqContext.getHeaders() + "]");
		log.debug("Request Media Type [" + reqContext.getMediaType() + "]");
		log.debug("Request date [" + reqContext.getDate() + "]");

	}

	@Override
	public void filter(ContainerRequestContext reqContext, ContainerResponseContext resContext) throws IOException {
		log.debug("-- res info --");
		log.debug("Res Context [" + resContext + "] req [" + reqContext + "]");
		log.debug("Response headers [" + resContext.getStringHeaders() + "]");
		log.debug("Response With Type [" + resContext.getMediaType() + "]");
		log.debug("BODY [" + resContext.getEntity() + "]");
		log.debug("Response code [" + resContext.getStatus() + "]");
		log.debug("Response date [" + resContext.getDate() + "]");

	}

	// private void log(UriInfo uriInfo, MultivaluedMap<String, ?> headers) {
	// log.debug("Path: " + uriInfo.getPath());
	// headers.entrySet().forEach(h -> log.debug(h.getKey() + ": " +
	// h.getValue()));
	// }

}
