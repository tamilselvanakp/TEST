package demo1.date14;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class Filterconfig implements ContainerRequestFilter, ContainerResponseFilter {
	/*public void contextInitialized(ServletContextEvent event) {
		String url = "jdbc:oracle:thin:@192.168.110.204:1521:stcadb";
		String uname = "IT_HLR";
		String passwd = "lyca";
		try {
			System.out.println("HLR_DB_CON 0");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection l_conn = DriverManager.getConnection(url, uname, passwd);
			System.out.println("HLR_DB_CON establised");
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
			System.out.println("HLR_DB_CON closing");
			db_con.close();
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
	}*/

	@Override
	public void filter(ContainerRequestContext reqContext) throws IOException {
		System.out.println("-- req info --");
		System.out.println("Req Context [" + reqContext + "]");
		System.out.println("getHeaders [" + reqContext.getHeaders() + "]");
		System.out.println("Request Media Type [" + reqContext.getMediaType() + "]");
		System.out.println("Request date [" + reqContext.getDate() + "]");

	}

	@Override
	public void filter(ContainerRequestContext reqContext, ContainerResponseContext resContext) throws IOException {
		System.out.println("-- res info --");
		System.out.println("Res Context [" + resContext + "] req [" + reqContext + "]");
		System.out.println("Response headers [" + resContext.getStringHeaders() + "]");
		System.out.println("Response With Type [" + resContext.getMediaType() + "]");
		System.out.println("BODY [" + resContext.getEntity() + "]");
		System.out.println("Response code [" + resContext.getStatus() + "]");
		System.out.println("Response date [" + resContext.getDate() + "]");

	}

	// private void log(UriInfo uriInfo, MultivaluedMap<String, ?> headers) {
	// System.out.println("Path: " + uriInfo.getPath());
	// headers.entrySet().forEach(h -> System.out.println(h.getKey() + ": " +
	// h.getValue()));
	// }

}
