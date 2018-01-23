/**
 * 
 */
package cz.sirsi.ancestry.core.jaxb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cz.sirsi.ancestry.core.load.rodx.Ancestry;

import junit.framework.TestCase;

/**
 * @author msiroky
 */
public class JaxbTest extends TestCase {
	public void testReadAndWrite() throws JAXBException, IOException {
		JAXBContext jc = JAXBContext.newInstance("cz.sirsi.ancestry.core.load.rodx");

		Unmarshaller unmarshaller = jc.createUnmarshaller();

		File source = new File(JaxbTest.class.getResource("/Sample.xml").getFile());
		Ancestry ancestry = (Ancestry) unmarshaller.unmarshal(source);

		System.out.println(ancestry.getVersion().getMajorVersion());

		Marshaller marshaller = jc.createMarshaller();

		File tempFile = File.createTempFile("prefix", "suffix");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
		marshaller.marshal(ancestry, new FileWriter(tempFile));

		// String sourceMD5 = MD5.asHex(MD5.getHash(source));
		// String targetMD5 = MD5.asHex(MD5.getHash(tempFile));

		// assertEquals(sourceMD5, targetMD5);
	}
}
