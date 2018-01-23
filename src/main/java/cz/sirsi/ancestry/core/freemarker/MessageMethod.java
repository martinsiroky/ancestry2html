// Last revision 20.4.08
package cz.sirsi.ancestry.core.freemarker;

import java.util.List;

import cz.sirsi.ancestry.core.configuration.MessagesTools;
import freemarker.template.TemplateMethodModel;

/**
 * Freemarker method for getting localized messages in ftl file
 * 
 * @author msiroky
 */
public class MessageMethod implements TemplateMethodModel {

	/**
	 * (non-Javadoc)
	 * 
	 * @see freemarker.template.TemplateMethodModel#exec(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public Object exec(List arg0) {
		return MessagesTools.getWebMessages().getMessage(arg0);
	}
}
