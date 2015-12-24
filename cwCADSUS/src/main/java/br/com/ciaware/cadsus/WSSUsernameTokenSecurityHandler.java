//Nome do pacote utilizado
package br.com.ciaware.cadsus;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class WSSUsernameTokenSecurityHandler implements SOAPHandler<SOAPMessageContext> {

	private String login;
	private String pwd;

	public WSSUsernameTokenSecurityHandler(String login, String pwd) {
		this.login = login;
		this.pwd = pwd;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {

		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outboundProperty.booleanValue()) {

			try {
				
				SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
				SOAPFactory factory = SOAPFactory.newInstance();
				String prefix = "wsse";
				String uri = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
				SOAPElement securityElem = factory.createElement("Security", prefix, uri);
				SOAPElement tokenElem = factory.createElement("UsernameToken", prefix, uri);
				tokenElem.addAttribute(QName.valueOf("wsu:Id"), "UsernameToken-2");
				tokenElem.addAttribute(QName.valueOf("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
				SOAPElement userElem = factory.createElement("Username", prefix, uri);
				userElem.addTextNode(login);
				SOAPElement pwdElem = factory.createElement("Password", prefix, uri);
				pwdElem.addTextNode(pwd);
				pwdElem.addAttribute(QName.valueOf("Type"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
				tokenElem.addChildElement(userElem);
				tokenElem.addChildElement(pwdElem);
				securityElem.addChildElement(tokenElem);
				if (envelope.getHeader() != null) {
					envelope.getHeader().detachNode();
				}
				
				System.out.println(envelope.toString());
				
				SOAPHeader header = envelope.addHeader();
				header.addChildElement(securityElem);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// inbound
		}
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		return new TreeSet<QName>();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}

	@Override
	public void close(MessageContext context) {
		//
	}
}
