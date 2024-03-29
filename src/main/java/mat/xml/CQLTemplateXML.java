package mat.xml;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import mat.filesystem.ResourceLoader;
import mat.xml.XmlProcessor;

public class CQLTemplateXML {
	
	private final static XmlProcessor cqlTemplateXML;
	
	static {
		String fileName = "cqlTemplate.xml";
		URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
		File templateFile = null;
		try {
			templateFile = new File(templateFileUrl.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		cqlTemplateXML = new XmlProcessor(templateFile);
	}
	
	public static XmlProcessor getCQLTemplateXmlProcessor(){
		return cqlTemplateXML;
	}
	
}

