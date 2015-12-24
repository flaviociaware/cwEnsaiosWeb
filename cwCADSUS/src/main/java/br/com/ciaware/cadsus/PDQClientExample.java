//Nome do pacote utilizado
package br.com.ciaware.cadsus;

import ihe.iti.pdqv3._2007.PDQSupplierPortType;
import ihe.iti.pdqv3._2007.PDQSupplierService;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.CD;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EN;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PDQObjectFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.ST;
import org.hl7.v3.TS;
import org.hl7.v3.XActMoodIntentEvent;

public class PDQClientExample {

	private static final PDQSupplierService SERVICE;

	public static void main(String[] args) {
		new PDQClientExample().callService();
	}
	
	
	static {

		// CONFIGURA A JVM PARA IGNORAR O CERTIFICADOS INVALIDOS
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e1) {
			throw new RuntimeException(e1);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		SERVICE = new PDQSupplierService();

		// Handlers para acionar os parametros de autenticacao no cabecalho da
		// mensagem
		// CADSUS.CNS.PDQ.PUBLICO
		// kUXNmiiii#RDdlOELdoe00966
		// http://datasus.saude.gov.br/images/Interoperabilidade/Especificacao%20Tecnica%20para%20Integracao%20PDQ%20com%20o%20Cartao%20Nacional%20de%20Saude%20v5.pdf
		
		final WSSUsernameTokenSecurityHandler handler = new WSSUsernameTokenSecurityHandler(
				"CADSUS.CNS.PDQ.PUBLICO", 
				"kUXNmiiii#RDdlOELdoe00966");
		
		SERVICE.setHandlerResolver(new HandlerResolver() {

			@Override
			@SuppressWarnings("rawtypes")
			public List<Handler> getHandlerChain(PortInfo arg0) {
				List<Handler> handlerList = new ArrayList<Handler>();
				handlerList.add(handler);
				return handlerList;
			}
		});
	}

	public PDQClientExample() {
	}

	public void callService() {
		PDQSupplierPortType pdq = SERVICE.getPDQSupplierPortSoap12();

		PRPAIN201305UV02 body = new PRPAIN201305UV02();
		body.setITSVersion("XML_1.0");
		// Parte fixa
		// <id root="2.16.840.1.113883.4.714" extension="123456"/>
		II ii = new II();
		ii.setRoot("2.16.840.1.113883.4.714");
		ii.setExtension("123456");
		body.setId(ii);
		// <creationTime value="20070428150301"/>
		TS ts = new TS();
		ts.setValue(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		body.setCreationTime(ts);
		// <interactionId root="2.16.840.1.113883.1.6"
		// extension="PRPA_IN201305UV02"/>
		II intId = new II();
		intId.setRoot("2.16.840.1.113883.1.6");
		intId.setExtension("PRPA_IN201305UV02");
		body.setInteractionId(intId);
		// <processingCode code="T"/>
		CS processingCode = new CS();
		processingCode.setCode("T");
		body.setProcessingCode(processingCode);
		// <processingModeCode code="T"/>
		body.setProcessingModeCode(processingCode);
		// <acceptAckCode code="AL"/>
		CS acceptAckCode = new CS();
		acceptAckCode.setCode("AL");
		body.setAcceptAckCode(acceptAckCode);
		// <receiver typeCode="RCV">
		MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
		receiver.setTypeCode(CommunicationFunctionType.RCV);
		// <device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device deviceReceiver = new MCCIMT000100UV01Device();
		deviceReceiver.setClassCode(EntityClassDevice.DEV);
		deviceReceiver.setDeterminerCode("INSTANCE");
		// <id root="2.16.840.1.113883.3.72.6.5.100.85"/>
		II idDeviceReceiver = new II();
		idDeviceReceiver.setRoot("2.16.840.1.113883.3.72.6.5.100.85");
		deviceReceiver.getId().add(idDeviceReceiver);
		// </device>
		receiver.setDevice(deviceReceiver);
		body.getReceiver().add(receiver);
		// </receiver>
		// <sender typeCode="SND">
		MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
		sender.setTypeCode(CommunicationFunctionType.SND);
		// <device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device deviceSender = new MCCIMT000100UV01Device();
		deviceSender.setClassCode(EntityClassDevice.DEV);
		deviceSender.setDeterminerCode("INSTANCE");
		// <id root="2.16.840.1.113883.3.72.6.2"/>
		II idDeviceSender = new II();
		idDeviceSender.setRoot("2.16.840.1.113883.3.72.6.2");
		deviceSender.getId().add(idDeviceSender);
		// <name>SIGA-SAUDE</name>
		EN nameSender = new EN();
		nameSender.getContent().add("SIGA");
		deviceSender.getName().add(nameSender);
		// </device>
		sender.setDevice(deviceSender);
		body.setSender(sender);
		// </sender>

		// <controlActProcess classCode="CACT" moodCode="EVN">
		PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlAct = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
		controlAct.setClassCode(ActClassControlAct.CACT);
		controlAct.setMoodCode(XActMoodIntentEvent.EVN);
		body.setControlActProcess(controlAct);
		// <code code="PRPA_TE201305UV02" codeSystem="2.16.840.1.113883.1.6"/>
		CD code = new CD();
		code.setCode("PRPA_TE201305UV02");
		code.setCodeSystem("2.16.840.1.113883.1.6");
		controlAct.setCode(code);
		// <queryByParameter>
		PRPAMT201306UV02QueryByParameter queryByParamenter = new PRPAMT201306UV02QueryByParameter();
		// <queryId root="1.2.840.114350.1.13.28.1.18.5.999"
		// extension="1840997084" />
		II queryId = new II();
		queryId.setRoot("1.2.840.114350.1.13.28.1.18.5.999");
		queryId.setExtension("1840997084");
		queryByParamenter.setQueryId(queryId);
		// <statusCode code="new"/>
		CS statusCode = new CS();
		statusCode.setCode("new");
		queryByParamenter.setStatusCode(statusCode);
		// <responseModalityCode code="R"/>
		CS responseModalityCode = new CS();
		responseModalityCode.setCode("R");
		queryByParamenter.setResponseModalityCode(responseModalityCode);
		// <responsePriorityCode code="I"/>
		CS responsePriorityCode = new CS();
		responsePriorityCode.setCode("I");
		queryByParamenter.setResponsePriorityCode(responsePriorityCode);
		// <parameterList>
		PRPAMT201306UV02ParameterList parameterList = new PRPAMT201306UV02ParameterList();
		queryByParamenter.setParameterList(parameterList);
		// <livingSubjectId>
//		PRPAMT201306UV02LivingSubjectId livingSubjectId = new PRPAMT201306UV02LivingSubjectId();
//		parameterList.getLivingSubjectId().add(livingSubjectId);
//		// <value root="2.16.840.1.113883.13.236" extension="708006399875323"/>
//		// - PESQUISA POR CNS
//		II cnsParameter = new II();
//		cnsParameter.setRoot("2.16.840.1.113883.13.236");
//		cnsParameter.setExtension("[CNS]");
//		livingSubjectId.getValue().add(cnsParameter);
//		// <semanticsText>LivingSubject.id</semanticsText>
//		ST cnsSemanticsText = new ST();
//		cnsSemanticsText.getContent().add("LivingSubject.id");
//		livingSubjectId.setSemanticsText(cnsSemanticsText);
		// </livingSubjectId>
		
		//Sexo
//		PRPAMT201306UV02LivingSubjectAdministrativeGender livingSubjectGender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
//		parameterList.getLivingSubjectAdministrativeGender().add(livingSubjectGender);
//		CE sexoParametro = new CE();
//		sexoParametro.setCodeSystem("2.16.840.1.113883.5.1");
//		sexoParametro.setCode("[Sexo]");
//		livingSubjectGender.getValue().add(sexoParametro);
//		ST sexoSemanticsText = new ST();
//		sexoSemanticsText.getContent().add("LivingSubject.administrativeGender");
//		livingSubjectGender.setSemanticsText(sexoSemanticsText);
		
		//Nome
		PDQObjectFactory factory = new PDQObjectFactory();
		PRPAMT201306UV02LivingSubjectName livingSubjectNome = new PRPAMT201306UV02LivingSubjectName();
		parameterList.getLivingSubjectName().add(livingSubjectNome);
		EN nomeParametro = new EN();
		nomeParametro.getUse().add("L");
		EnGiven given = new EnGiven();
		given.getContent().add("NOME TESTADOR SERVICOS");
		nomeParametro.getContent().add(factory.createENGiven(given));
		livingSubjectNome.getValue().add(nomeParametro);
		ST nomeSemanticsText = new ST();
		nomeSemanticsText.getContent().add("LivingSubject.Given");
		livingSubjectNome.setSemanticsText(nomeSemanticsText);
		
		//CPF
//		PRPAMT201306UV02LivingSubjectId livingSubjectIdCPF = new PRPAMT201306UV02LivingSubjectId();
//		parameterList.getLivingSubjectId().add(livingSubjectIdCPF);
//		// <value root="2.16.840.1.113883.13.236" extension="708006399875323"/>
//		// - PESQUISA POR CNS
//		II cpfParameter = new II();
//		cpfParameter.setRoot("2.16.840.1.113883.13.237");
//		cpfParameter.setExtension("[CPF]");
//		livingSubjectIdCPF.getValue().add(cpfParameter);
//		// <semanticsText>LivingSubject.id</semanticsText>
//		ST cpfSemanticsText = new ST();
//		cpfSemanticsText.getContent().add("LivingSubject.id");
//		livingSubjectIdCPF.setSemanticsText(cpfSemanticsText);
		
		//LocalID
//		PRPAMT201306UV02LivingSubjectId livingSubjectLocalID = new PRPAMT201306UV02LivingSubjectId();
//		parameterList.getLivingSubjectId().add(livingSubjectLocalID);
//		II localIDParametro = new II();
//		localIDParametro.setRoot("2.16.840.1.113883.3.4594.100.3");
//		localIDParametro.setExtension("[Local ID]");
//		livingSubjectLocalID.getValue().add(localIDParametro);
//		ST localIDSemanticsText = new ST();
//		localIDSemanticsText.getContent().add("LivingSubject.id");
//		livingSubjectLocalID.setSemanticsText(localIDSemanticsText);
		
		//Nome da mae
//		PRPAMT201306UV02MothersMaidenName mothersMaidenName = new PRPAMT201306UV02MothersMaidenName();
//		parameterList.getMothersMaidenName().add(mothersMaidenName);
//		PN nomeMaeParametro = new PN();
//		nomeMaeParametro.getUse().add("L");
//		nomeMaeParametro.getContent().add("[Nome da Mãe");
//		mothersMaidenName.getValue().add(nomeMaeParametro);
//		ST nomeMaeSemanticsText = new ST();
//		nomeMaeSemanticsText.getContent().add("mothersMaidenName");
//		mothersMaidenName.setSemanticsText(nomeMaeSemanticsText);
		
		//Data de Nascimento
//		PRPAMT201306UV02LivingSubjectBirthTime livingSubjectBirhtTime = new PRPAMT201306UV02LivingSubjectBirthTime();
//		parameterList.getLivingSubjectBirthTime().add(livingSubjectBirhtTime);
//		IVLTS birthTime = new IVLTS();
//		birthTime.setValue("[Data de nascimento]");
//		livingSubjectBirhtTime.getValue().add(birthTime);
//		ST dataNascimentoST = new ST();
//		dataNascimentoST.getContent().add("LivingSubject.birthTime");
//		livingSubjectBirhtTime.setSemanticsText(dataNascimentoST);
		
		
		// </parameterList>
		// </queryByParameter>
		JAXBElement<PRPAMT201306UV02QueryByParameter> jaxbQuery = new PDQObjectFactory()
				.createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(queryByParamenter);
		controlAct.setQueryByParameter(jaxbQuery);
		// </controlActProcess>

		PRPAIN201306UV02 retorno = pdq.pdqSupplierPRPAIN201305UV02(body);

		PRPAMT201310UV02Person patientPerson = retorno.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
				.getPatientPerson().getValue();
		@SuppressWarnings("unchecked")
		JAXBElement<EnGiven> obj = (JAXBElement<EnGiven>) patientPerson.getName().get(0).getContent().get(0);

		System.out.println("NOME: " + obj.getValue().getContent().get(0));

		//IDs
		for (PRPAMT201310UV02OtherIDs otherId : patientPerson.getAsOtherIDs()) {
			for (II id : otherId.getId()) {
				//CNS
				if ("2.16.840.1.113883.13.236".equals(id.getRoot())) {
					System.out.print("CNS: " + id.getExtension() + " ");
				}
				if ("2.16.840.1.113883.13.236.1".equals(id.getRoot())) {
					System.out.println("Tipo: " + id.getExtension());
				}
			}
		}

		//Nome da mae
		JAXBElement<EnGiven> nomeMae = (JAXBElement<EnGiven>) patientPerson.getPersonalRelationship().get(0).getRelationshipHolder1().getValue().getName().get(0).getContent().get(0);
		System.out.println("Nome da mae: " + nomeMae.getValue().getContent().get(0));
		
		//Sexo
		System.out.println("Sexo: " + patientPerson.getAdministrativeGenderCode().getCode());
		
		//Data de nascimento
		System.out.println("Data de nascimento: " + patientPerson.getBirthTime().getValue());
		
	}

}
