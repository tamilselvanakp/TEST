package demo1.date14;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RESPONSE")

public class Getsubscriberinforepo {
	Map<String, String> l_map = null;
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIMSI() {
		return IMSI;
	}

	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	String IMSI;

	public Map<String, String> getsubdata(String name, String Imsi) {
		l_map = new HashMap<String, String>();
		setName(name);
		setIMSI(Imsi);
		String Name = getName();
		l_map.put("name", Name);
		System.out.println("name " + Name);
		String l_imsi = getIMSI();
		l_map.put("imsi", l_imsi);

		l_map.put("ABCD", l_imsi);

		l_map.put("EFGH", l_imsi);

		return null;

	}

	public Response getItem(String name, String Imsi) {
		System.out.println("GET ITEM " + name + " " + Imsi);

		if (Imsi == null || Imsi == "")
			throw new NotFoundException("Item not found");
		String Name = getName();
		String l_imsi = getIMSI();
		EntityTag et = new EntityTag(Imsi);

		return Response.ok(et, "application/xml").build();
	}
}
