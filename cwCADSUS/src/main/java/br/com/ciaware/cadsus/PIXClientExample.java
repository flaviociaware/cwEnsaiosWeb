//Nome do pacote utilizado
package br.com.ciaware.cadsus;
import ihe.iti.pixv3._2007.PIXManagerPortType;
import ihe.iti.pixv3._2007.PIXManagerService;

import java.io.Serializable;
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
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.soap.SOAPFaultException;

import org.hl7.v3.AD;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.AdxpCity;
import org.hl7.v3.AdxpCountry;
import org.hl7.v3.BL;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT030007UVPerson;
import org.hl7.v3.COCTMT090100UV01AssignedPerson;
import org.hl7.v3.COCTMT150003UV03ContactParty;
import org.hl7.v3.COCTMT150003UV03Organization;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EN;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.IVLTS;
import org.hl7.v3.IVXBTS;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import org.hl7.v3.MCCIMT000200UV01AcknowledgementDetail;
import org.hl7.v3.PIXObjectFactory;
import org.hl7.v3.PN;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02BirthPlace;
import org.hl7.v3.PRPAMT201301UV02Citizen;
import org.hl7.v3.PRPAMT201301UV02LanguageCommunication;
import org.hl7.v3.PRPAMT201301UV02Nation;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201301UV02PersonalRelationship;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.hl7.v3.ParticipationTargetSubject;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.RoleClassContact;
import org.hl7.v3.ST;
import org.hl7.v3.SetOperator;
import org.hl7.v3.TEL;
import org.hl7.v3.TS;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.XDeterminerInstanceKind;
import org.hl7.v3.XParticipationAuthorPerformer;


public class PIXClientExample {

	private static final PIXManagerService SERVICE;
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static String systemCodeAuthorityName;
	private static String localIDPatient;
	private static String OIDPatient;

	static {

		// CONFIGURA A JVM PARA IGNORAR O CERTIFICADOS INVALIDOS
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
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

		SERVICE = new PIXManagerService();

		// Handlers para acionar os parametros de autenticacao no cabecalho da
		// mensagem
		// http://datasus.saude.gov.br/images/Interoperabilidade/Especificacao%20Tecnica%20para%20Integracao%20PDQ%20com%20o%20Cartao%20Nacional%20de%20Saude%20v5.pdf
		final WSSUsernameTokenSecurityHandler handler = new WSSUsernameTokenSecurityHandler(
				"CADSUS.CNS.PDQ.PUBLICO", "kUXNmiiii#RDdlOELdoe00966");
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

	private PIXClientExample() {
	}

	public static void main(String args[]) {

		PIXManagerPortType pixPortType = SERVICE.getPIXManagerPortSoap12();
		PRPAIN201301UV02 request = sampleRequest();
		MCCIIN000002UV01 response = null;
		PRPAIN201310UV02 responsePIXQuery = null;

		try {

			// response = pixPortType.pixManagerPRPAIN201301UV02(request);

			responsePIXQuery = pixPortType
					.pixManagerPRPAIN201309UV02(callPIXQueryRequest());
			
			PRPAIN201310UV02MFMIMT700711UV01Subject2 subject = responsePIXQuery.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1();
			
			for (II id : subject.getPatient().getId()) {
				System.out.println("SystemCode= " + id.getAssigningAuthorityName() +", LocalID= " + id.getExtension());
			}

			if (response.getAcknowledgement() == null
					|| response.getAcknowledgement().size() <= 0)
				throw new Exception();

			MCCIMT000200UV01Acknowledgement ack = response.getAcknowledgement()
					.get(0);
			CS code = ack.getTypeCode();

			if (code.getCode() != null && !code.getCode().equals("CA")) {

				String txtDetail = "";
				for (MCCIMT000200UV01AcknowledgementDetail detail : ack
						.getAcknowledgementDetail()) {

					if (detail.getText() != null) {

						for (Serializable serial : detail.getText()
								.getContent()) {

							txtDetail += (String) serial;
							txtDetail += "; ";
						}
					}
				}

				throw new Exception(txtDetail);
			}

			System.out.println("SUCESSO");

		} catch (SOAPFaultException e) {
			System.out.println("ERRO: " + e.getLocalizedMessage());
		} catch (Exception e) {
			System.out.println("ERRO: " + e.getMessage());
		}

	}

	public static PRPAIN201309UV02 callPIXQueryRequest() {
		PIXObjectFactory factory = new PIXObjectFactory();

		// <PRPA_IN201309UV02
		// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		// xmlns="urn:hl7-org:v3" ITSVersion="XML_1.0">
		PRPAIN201309UV02 operacao = new PRPAIN201309UV02();
		operacao.setITSVersion("XML_1.0");

		// <id root="2220c1c4-87ef-11dc-b865-3603d6866807"/>
		II id = new II();
		id.setRoot("2220c1c4-87ef-11dc-b865-3603d6866807");
		operacao.setId(id);

		// <creationTime value="20070810140900"/>
		TS ts = new TS();
		ts.setValue("20070810140900");
		operacao.setCreationTime(ts);

		// <interactionId root="2.16.840.1.113883.1.6"
		// extension="PRPA_IN201309UV02"/>
		II intId = new II();
		intId.setRoot("2.16.840.1.113883.1.6");
		intId.setExtension("PRPA_IN201309UV02");
		operacao.setInteractionId(intId);

		// <processingCode code="P"/>
		CS processingCode = new CS();
		processingCode.setCode("P");
		operacao.setProcessingCode(processingCode);

		// <processingModeCode code="T"/>
		CS processingModeCode = new CS();
		processingModeCode.setCode("T");
		operacao.setProcessingModeCode(processingModeCode);

		// <acceptAckCode code="AL"/>
		CS acceptAckCode = new CS();
		acceptAckCode.setCode("AL");
		operacao.setAcceptAckCode(acceptAckCode);

		// <receiver typeCode="RCV">
		MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
		receiver.setTypeCode(CommunicationFunctionType.RCV);
		operacao.getReceiver().add(receiver);
		// <device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
		device.setClassCode(EntityClassDevice.DEV);
		device.setDeterminerCode("INSTANCE");
		receiver.setDevice(device);
		// <id root="1.2.840.114350.1.13.99999.4567"/>
		II deviceId = new II();
		deviceId.setRoot("1.2.840.114350.1.13.99999.4567");
		device.getId().add(deviceId);
		// <telecom value="https://example.org/PIXQuery"/>
		TEL deviceTel = new TEL();
		deviceTel.setValue("https://example.org/PIXQuery");
		device.getTelecom().add(deviceTel);
		// </device>
		// </receiver>
		// <sender typeCode="SND">
		MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
		sender.setTypeCode(CommunicationFunctionType.SND);
		operacao.setSender(sender);
		// <device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device senderDevice = new MCCIMT000100UV01Device();
		senderDevice.setClassCode(EntityClassDevice.DEV);
		senderDevice.setDeterminerCode("INSTANCE");
		sender.setDevice(senderDevice);
		// <id root="1.2.840.114350.1.13.99997.2.7788"/>
		II senderDeviceId = new II();
		senderDeviceId.setRoot("1.2.840.114350.1.13.99997.2.7788");
		senderDevice.getId().add(senderDeviceId);
		// </device>
		// </sender>
		// <controlActProcess classCode="CACT" moodCode="EVN">
		PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
		controlActProcess.setClassCode(ActClassControlAct.CACT);
		controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
		operacao.setControlActProcess(controlActProcess);
		// <code code="PRPA_TE201309UV02" codeSystem="2.16.840.1.113883.1.6"/>
		CD controlActCode = new CD();
		controlActCode.setCode("PRPA_TE201309UV02");
		controlActCode.setCodeSystem("2.16.840.1.113883.1.6");
		controlActProcess.setCode(controlActCode);
		// <authorOrPerformer typeCode="AUT">
		QUQIMT021001UV01AuthorOrPerformer author = new QUQIMT021001UV01AuthorOrPerformer();
		author.setTypeCode(XParticipationAuthorPerformer.AUT);
		controlActProcess.getAuthorOrPerformer().add(author);
		// <assignedPerson classCode="ASSIGNED">
		COCTMT090100UV01AssignedPerson assignedPerson = new COCTMT090100UV01AssignedPerson();
		assignedPerson.setClassCode("ASSIGNED");
		author.setAssignedPerson(factory
				.createMFMIMT700711UV01AuthorOrPerformerAssignedPerson(assignedPerson));

		// <id root="1.2.840.114350.1.13.99997.2.7766" extension="USR5568"/>
		II assignedPersonId = new II();
		assignedPersonId.setRoot("1.2.840.114350.1.13.99997.2.7766");
		assignedPersonId.setExtension("USR5568");
		assignedPerson.getId().add(assignedPersonId);
		// </assignedPerson>
		// </authorOrPerformer>
		// <queryByParameter>
		PRPAMT201307UV02QueryByParameter queryByParameter = new PRPAMT201307UV02QueryByParameter();
		controlActProcess
				.setQueryByParameter(factory
						.createPRPAIN201309UV02QUQIMT021001UV01ControlActProcessQueryByParameter(queryByParameter));
		// <queryId root="2.16.840.1.113883.3.4594" extension="1234567789"/>
		II queryId = new II();
		queryId.setRoot("2.16.840.1.113883.3.4594");
		queryId.setExtension("1234567789");
		queryByParameter.setQueryId(queryId);
		// <statusCode code="new"/>
		CS queryStatusCode = new CS();
		queryStatusCode.setCode("new");
		queryByParameter.setStatusCode(queryStatusCode);
		// <responsePriorityCode code="I"/>
		CS queryResponsePriorityCode = new CS();
		queryResponsePriorityCode.setCode("I");
		;
		queryByParameter.setResponsePriorityCode(queryResponsePriorityCode);
		// <parameterList>
		PRPAMT201307UV02ParameterList parameterList = new PRPAMT201307UV02ParameterList();
		queryByParameter.setParameterList(parameterList);
		// <patientIdentifier>
		PRPAMT201307UV02PatientIdentifier patientIdentifier = new PRPAMT201307UV02PatientIdentifier();
		parameterList.getPatientIdentifier().add(patientIdentifier);
		// <value root="2.16.840.1.113883.3.4594.100.3" extension="87667654"/>
		II value = new II();
		value.setRoot("2.16.840.1.113883.3.4594.100.3");
		value.setExtension("87667654");
		patientIdentifier.getValue().add(value);
		// <semanticsText/>
		patientIdentifier.setSemanticsText(new ST());
		// </patientIdentifier>
		// </parameterList>
		// </queryByParameter>
		// </controlActProcess>
		// </PRPA_IN201309UV02>

		return operacao;
	}

	public static PRPAIN201301UV02 sampleRequest() {

		PIXObjectFactory factory = new PIXObjectFactory();

		// <env:Body>
		// <urn:PRPA_IN201301UV02 xmlns:urn="urn:hl7-org:v3"
		// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		// ITSVersion="XML_1.0">
		PRPAIN201301UV02 prpain = new PRPAIN201301UV02();
		prpain.setITSVersion("XML_1.0");

		// <urn:id extension="6e88df42-70ac-4db6-a4ba-449c7fbf2a4e"
		// root="2.16.840.1.113883.3.72.6.1"/>
		II idPrpain = new II();
		idPrpain.setExtension("6e88df42-70ac-4db6-a4ba-449c7fbf2a4e");
		idPrpain.setRoot("2.16.840.1.113883.3.72.6.1");
		prpain.setId(idPrpain);

		// <urn:creationTime value="20101123115812"/>
		TS ts = new TS();
		ts.setValue(sdf.format(new Date()));
		prpain.setCreationTime(ts);

		// <urn:interactionId extension="PRPA_IN201301UV02"
		// root="2.16.840.1.113883.1.6"/>
		II interactId = new II();
		interactId.setExtension("PRPA_IN201301UV02");
		interactId.setRoot("2.16.840.1.113883.1.6");
		prpain.setInteractionId(interactId);

		// <urn:processingCode code="P"/>
		CS processCode = new CS();
		processCode.setCode("P");
		prpain.setProcessingCode(processCode);

		// <urn:processingModeCode code="T"/>
		CS processMode = new CS();
		processMode.setCode("T");
		prpain.setProcessingModeCode(processMode);

		// <urn:acceptAckCode code="AL"/>
		CS ackCode = new CS();
		ackCode.setCode("AL");
		prpain.setAcceptAckCode(ackCode);

		// <urn:receiver typeCode="RCV">
		MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
		receiver.setTypeCode(CommunicationFunctionType.RCV);
		prpain.getReceiver().add(receiver);

		// <urn:device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device deviceReceiver = new MCCIMT000100UV01Device();
		receiver.setDevice(deviceReceiver);
		deviceReceiver.setClassCode(EntityClassDevice.DEV);
		deviceReceiver.setDeterminerCode("INSTANCE");

		// <urn:id root="2.16.840.1.113883.3.72.6.5.100.85"/>
		II idReceiver = new II();
		idReceiver.setRoot("2.16.840.1.113883.3.72.6.5.100.85");
		deviceReceiver.getId().add(idReceiver);

		// <urn:asAgent classCode="AGNT">
		MCCIMT000100UV01Agent asAgentReceiver = new MCCIMT000100UV01Agent();
		asAgentReceiver.getClassCode().add("AGNT");
		deviceReceiver.setAsAgent(factory
				.createMCCIMT000100UV01DeviceAsAgent(asAgentReceiver));

		// <urn:representedOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		MCCIMT000100UV01Organization orgReceiver = new MCCIMT000100UV01Organization();
		orgReceiver.setClassCode("ORG");
		orgReceiver.setDeterminerCode("INSTANCE");
		asAgentReceiver
				.setRepresentedOrganization(factory
						.createMCCIMT000100UV01AgentRepresentedOrganization(orgReceiver));

		// <urn:id root="2.16.840.1.113883.3.72.6.1"/>
		II idOrgReceiver = new II();
		idOrgReceiver.setRoot("2.16.840.1.113883.3.72.6.1");
		orgReceiver.getId().add(idOrgReceiver);

		// </urn:representedOrganization>
		// </urn:asAgent>
		// </urn:device>
		// </urn:receiver>

		// <urn:sender typeCode="SND">
		MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
		sender.setTypeCode(CommunicationFunctionType.SND);
		prpain.setSender(sender);

		// <urn:device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device deviceSender = new MCCIMT000100UV01Device();
		sender.setDevice(deviceSender);
		deviceSender.setClassCode(EntityClassDevice.DEV);
		deviceSender.setDeterminerCode("INSTANCE");

		// <urn:id root="2.16.840.1.113883.3.72.6.2"/>
		II idSender = new II();
		idSender.setRoot("2.16.840.1.113883.3.72.6.2");
		deviceSender.getId().add(idSender);

		// <urn:asAgent classCode="AGNT">
		MCCIMT000100UV01Agent asAgentSender = new MCCIMT000100UV01Agent();
		asAgentSender.getClassCode().add("AGNT");
		deviceSender.setAsAgent(factory
				.createMCCIMT000100UV01DeviceAsAgent(asAgentSender));

		// <urn:representedOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		MCCIMT000100UV01Organization orgSender = new MCCIMT000100UV01Organization();
		orgSender.setClassCode("ORG");
		orgSender.setDeterminerCode("INSTANCE");
		asAgentSender.setRepresentedOrganization(factory
				.createMCCIMT000100UV01AgentRepresentedOrganization(orgSender));

		// <urn:id root="2.16.840.1.113883.3.72.6.1"/>
		II idOrgSender = new II();
		idOrgSender.setRoot("2.16.840.1.113883.3.72.6.1");
		orgSender.getId().add(idOrgSender);

		// </urn:representedOrganization>
		// </urn:asAgent>
		// </urn:device>
		// </urn:sender>

		// <urn:controlActProcess classCode="CACT" moodCode="EVN">
		PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
		controlActProcess.setClassCode(ActClassControlAct.CACT);
		controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
		prpain.setControlActProcess(controlActProcess);

		// <urn:code code="PRPA_TE201301UV02"
		// codeSystem="2.16.840.1.113883.1.6"/>
		CD capCode = new CD();
		capCode.setCode("PRPA_TE201301UV02");
		capCode.setCodeSystem("2.16.840.1.113883.1.6");
		controlActProcess.setCode(capCode);

		// <urn:subject typeCode="SUBJ">
		PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
		subject.getTypeCode().add("SUBJ");
		controlActProcess.getSubject().add(subject);

		// <urn:registrationEvent classCode="REG" moodCode="EVN">
		PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationEvent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
		registrationEvent.getClassCode().add("REG");
		registrationEvent.getMoodCode().add("EVN");
		subject.setRegistrationEvent(registrationEvent);

		// <urn:id nullFlavor="NA"/>
		II idRegEvent = new II();
		idRegEvent.getNullFlavor().add("NA");
		registrationEvent.getId().add(idRegEvent);

		// <urn:statusCode code="active"/>
		CS statusCodeRegEvent = new CS();
		statusCodeRegEvent.setCode("active");
		registrationEvent.setStatusCode(statusCodeRegEvent);

		// <urn:subject1 typeCode="SBJ">
		PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
		subject1.setTypeCode(ParticipationTargetSubject.SBJ);
		registrationEvent.setSubject1(subject1);

		// <urn:patient classCode="PAT">
		PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
		patient.getClassCode().add("PAT");
		subject1.setPatient(patient);

		// <urn:id assigningAuthorityName="CADSUS" extension="9825894104729"
		// root="2.16.840.1.113883.13.236"/>
		II idPatient = new II();
		idPatient.setAssigningAuthorityName(systemCodeAuthorityName);
		idPatient.setExtension(localIDPatient);
		idPatient.setRoot(OIDPatient);
		patient.getId().add(idPatient);

		// <urn:statusCode code="active"/>
		CS statusCodePatient = new CS();
		statusCodePatient.setCode("active");
		patient.setStatusCode(statusCodePatient);

		// <urn:confidentialityCode codeSystem="2.16.840.1.113883.5.25"
		// code="R"/>
		CE confCode = new CE();
		confCode.setCodeSystem("2.16.840.1.113883.5.25");
		confCode.setCode("N");
		patient.getConfidentialityCode().add(confCode);

		// <urn:veryImportantPersonCode code="VIP"
		// codeSystem="2.16.840.1.113883.5.1075"/>
		// CE vip = new CE();
		// vip.setCodeSystem("2.16.840.1.113883.5.1075");
		// vip.setCode("false");
		// patient.setVeryImportantPersonCode(vip);

		// <urn:patientPerson classCode="PSN" determinerCode="INSTANCE">
		PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();
		person.getClassCode().add("PSN");
		person.setDeterminerCode("INSTANCE");
		patient.setPatientPerson(factory
				.createPRPAMT201301UV02PatientPatientPerson(person));

		// Nome
		// <urn:name use="L">
		PN nome = new PN();
		nome.getUse().add("L");
		person.getName().add(nome);

		// <urn:given>BRUCE WAYNE</urn:given>
		EnGiven givenNome = new EnGiven();
		givenNome.getContent().add("CRISTO REDENTOR MARACANA");
		nome.getContent().add(factory.createENGiven(givenNome));

		// </urn:name>

		// Nome social ou apelido
		// <urn:name use="ASGN">
		PN apelido = new PN();
		apelido.getUse().add("ASGN");
		person.getName().add(apelido);

		// <urn:given>BAT</urn:given>
		EnGiven givenApelido = new EnGiven();
		givenApelido.getContent().add("JOSE MARACA");
		apelido.getContent().add(factory.createENGiven(givenApelido));

		// </urn:name>

		// <urn:telecom use="ASN" value="+55-61-98012345"/>
		TEL telefone = new TEL();
		telefone.getUse().add("ASN");
		telefone.setValue("+55-61-97865432");
		person.getTelecom().add(telefone);
		//
		// // <urn:telecom use="NET" value="foo@mail.com"/>
		// TEL emailPrincipal = new TEL();
		// emailPrincipal.getUse().add("NET");
		// emailPrincipal.setValue("foo@mail.com");
		// person.getTelecom().add(emailPrincipal);
		//
		// // <urn:telecom use="NET" value="bar@mail.com"/>
		// TEL emailAlternativo = new TEL();
		// emailAlternativo.getUse().add("NET");
		// emailAlternativo.setValue("bar@mail.com");
		// person.getTelecom().add(emailAlternativo);

		// Sexo
		// <urn:administrativeGenderCode codeSystem="2.16.840.1.113883.5.1"
		// code="M"/>
		CE sexo = new CE();
		sexo.setCodeSystem("2.16.840.1.113883.5.1");
		sexo.setCode("M");
		person.setAdministrativeGenderCode(sexo);

		// Data de nascimento
		// <urn:birthTime value="19501231"/>
		TS dataNascimento = new TS();
		dataNascimento.setValue("19500102");
		person.setBirthTime(dataNascimento);

		// Informa\E7\E3o de mortalidade
		// <urn:deceasedInd value="true"/>
		// BL isMorto = new BL();
		// isMorto.setValue(true);
		// person.setDeceasedInd(isMorto);
		// //
		// // // <urn:deceasedTime value="20140131"/>
		// TS dataMorte = new TS();
		// dataMorte.setValue("20140131");
		// person.setDeceasedTime(dataMorte);
		//
		// // <urn:multipleBirthInd value="true"/>
		// BL isPartoGemelar = new BL();
		// isPartoGemelar.setValue(true);
		// person.setMultipleBirthInd(isPartoGemelar);
		//
		// // <urn:multipleBirthOrderNumber value="2"/>
		// INT ordemPartoGemelar = new INT();
		// ordemPartoGemelar.setValue(new BigInteger("2"));
		// person.setMultipleBirthOrderNumber(ordemPartoGemelar);

		// Endere\E7o, Caso exista use="H", Caso n\E3o exista use="BAD"
		// <urn:addr use="H">
		AD endereco = new AD();
		endereco.getUse().add("BAD");
		person.getAddr().add(endereco);

		// <urn:streetNameType>739</urn:streetNameType>
		// AdxpStreetNameType streetNameType = new AdxpStreetNameType();
		// streetNameType.getContent().add("739");
		// endereco.getContent().add(factory.createADStreetNameType(streetNameType));
		//
		// // <urn:streetName>444 NORTE BLOCO E</urn:streetName>
		// AdxpStreetName streetName = new AdxpStreetName();
		// streetName.getContent().add("444 NORTE BLOCO E");
		// endereco.getContent().add(factory.createADStreetName(streetName));
		//
		// // <urn:houseNumber>S/N</urn:houseNumber>
		// AdxpHouseNumber houseNumber = new AdxpHouseNumber();
		// houseNumber.getContent().add("S/N");
		// endereco.getContent().add(factory.createADHouseNumber(houseNumber));
		//
		// // <urn:unitID>AP 222</urn:unitID>
		// AdxpUnitID unitID = new AdxpUnitID();
		// unitID.getContent().add("AP 222");
		// endereco.getContent().add(factory.createADUnitID(unitID));
		//
		// // <urn:additionalLocator>ASA NORTE</urn:additionalLocator>
		// AdxpAdditionalLocator additionalLocator = new
		// AdxpAdditionalLocator();
		// additionalLocator.getContent().add("ASA NORTE");
		// endereco.getContent().add(factory.createADAdditionalLocator(additionalLocator));
		//
		// // <urn:city>530010</urn:city>
		// AdxpCity city = new AdxpCity();
		// city.getContent().add("530010");
		// endereco.getContent().add(factory.createADCity(city));
		//
		// // <urn:state>DF</urn:state>
		// AdxpState state = new AdxpState();
		// state.getContent().add("DF");
		// endereco.getContent().add(factory.createADState(state));
		//
		// // <urn:postalCode>70853040</urn:postalCode>
		// AdxpPostalCode postalCode = new AdxpPostalCode();
		// postalCode.getContent().add("70853040");
		// endereco.getContent().add(factory.createADPostalCode(postalCode));
		//
		// // <urn:country>1</urn:country>
		// AdxpCountry country = new AdxpCountry();
		// country.getContent().add("1");
		// endereco.getContent().add(factory.createADCountry(country));

		// </urn:addr>

		// Estado civil (n\E3o informado no Cadsus)
		// <urn:maritalStatusCode code="2"/>
		// CE estadoCivil = new CE();
		// estadoCivil.setCode("2");
		// person.setMaritalStatusCode(estadoCivil);

		// <urn:religiousAffiliationCode code="1"/>
		// CE religiao = new CE();
		// religiao.setCode("1");
		// person.setReligiousAffiliationCode(religiao);

		// <urn:raceCode code="01"/>
		CE raca = new CE();
		raca.setCode("99");
		person.getRaceCode().add(raca);

		// <urn:ethnicGroupCode code="0202"/>
		// CE etnia = new CE();
		// etnia.setCode("0202");
		// person.getEthnicGroupCode().add(etnia);

		// <urn:asCitizen classCode="CIT">
		PRPAMT201301UV02Citizen asCitizen = new PRPAMT201301UV02Citizen();
		asCitizen.getClassCode().add("CIT");
		person.getAsCitizen().add(asCitizen);

		// <urn:id extension="FB31241" root="2.16.840.1.113883.4.330"/>
		II idAsCitizen = new II();
		idAsCitizen.setExtension("FB11111");
		idAsCitizen.setRoot("2.16.840.1.113883.4.330");
		asCitizen.getId().add(idAsCitizen);

		// <urn:effectiveTime operator="E" value="20010101">
		IVLTS effectiveTime = new IVLTS();
		effectiveTime.setOperator(SetOperator.E);
		effectiveTime.setValue("20010101");
		asCitizen.setEffectiveTime(effectiveTime);

		// <urn:high value="20150101"/>
		IVXBTS high = new IVXBTS();
		high.setValue("20150101");
		effectiveTime.getRest().add(factory.createIVLTSHigh(high));

		// </urn:effectiveTime>

		// <urn:politicalNation classCode="NAT" determinerCode="INSTANCE">
		PRPAMT201301UV02Nation politicalNation = new PRPAMT201301UV02Nation();
		politicalNation.getClassCode().add("NAT");
		politicalNation.setDeterminerCode("INSTANCE");
		asCitizen.setPoliticalNation(politicalNation);

		// <urn:code code="2"/>
		CD codePolNation = new CD();
		codePolNation.setCode("010");
		politicalNation.setCode(codePolNation);

		// </urn:politicalNation>
		// </urn:asCitizen>

		// <!--CNS-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs cns = new PRPAMT201301UV02OtherIDs();
		// cns.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(cns);
		//
		// // <urn:id extension="888888888888888"
		// root="2.16.840.1.113883.13.236"/>
		// II idCns = new II();
		// idCns.setExtension("888888888888888");
		// idCns.setRoot("2.16.840.1.113883.13.236");
		// cns.getId().add(idCns);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgCns = new
		// COCTMT150002UV01Organization();
		// scopOrgCns.setClassCode("ORG");
		// scopOrgCns.setDeterminerCode("INSTANCE");
		// cns.setScopingOrganization(scopOrgCns);
		//
		// // <urn:id root="2.16.840.1.113883.13.236"/>
		// II idScopOrgCns = new II();
		// idScopOrgCns.setRoot("2.16.840.1.113883.13.236");
		// scopOrgCns.getId().add(idScopOrgCns);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--CPF-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs cpf = new PRPAMT201301UV02OtherIDs();
		// cpf.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(cpf);
		//
		// // <urn:id extension="11111111111" root="2.16.840.1.113883.13.237"/>
		// II idCpf = new II();
		// idCpf.setExtension("11111111111");
		// idCpf.setRoot("2.16.840.1.113883.13.237");
		// cpf.getId().add(idCpf);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgCpf = new
		// COCTMT150002UV01Organization();
		// scopOrgCpf.setClassCode("ORG");
		// scopOrgCpf.setDeterminerCode("INSTANCE");
		// cpf.setScopingOrganization(scopOrgCpf);
		//
		// // <urn:id root="2.16.840.1.113883.13.237"/>
		// II idScopOrgCpf = new II();
		// idScopOrgCpf.setRoot("2.16.840.1.113883.13.237");
		// scopOrgCpf.getId().add(idScopOrgCpf);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--PIS-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs pis = new PRPAMT201301UV02OtherIDs();
		// pis.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(pis);
		//
		// // <urn:id extension="01030433140" root="2.16.840.1.113883.13.240"/>
		// II idPis = new II();
		// idPis.setExtension("01030433140");
		// idPis.setRoot("2.16.840.1.113883.13.240");
		// pis.getId().add(idPis);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgPis = new
		// COCTMT150002UV01Organization();
		// scopOrgPis.setClassCode("ORG");
		// scopOrgPis.setDeterminerCode("INSTANCE");
		// pis.setScopingOrganization(scopOrgPis);
		//
		// // <urn:id root="2.16.840.1.113883.13.240"/>
		// II idScopOrgPis = new II();
		// idScopOrgPis.setRoot("2.16.840.1.113883.13.240");
		// scopOrgPis.getId().add(idScopOrgPis);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--DNV-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs dnv = new PRPAMT201301UV02OtherIDs();
		// dnv.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(dnv);
		//
		// // <urn:id extension="3333333333" root="2.16.840.1.113883.13.242"/>
		// II idDnv = new II();
		// idDnv.setExtension("3333333333");
		// idDnv.setRoot("2.16.840.1.113883.13.242");
		// dnv.getId().add(idDnv);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgDnv = new
		// COCTMT150002UV01Organization();
		// scopOrgDnv.setClassCode("ORG");
		// scopOrgDnv.setDeterminerCode("INSTANCE");
		// dnv.setScopingOrganization(scopOrgDnv);
		//
		// // <urn:id root="2.16.840.1.113883.13.242"/>
		// II idScopOrgDnv = new II();
		// idScopOrgDnv.setRoot("2.16.840.1.113883.13.242");
		// scopOrgDnv.getId().add(idScopOrgDnv);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--RIC-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs ric = new PRPAMT201301UV02OtherIDs();
		// ric.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(ric);
		//
		// // <urn:id extension="4444444444" root="2.16.840.1.113883.3.3024"/>
		// II idRic = new II();
		// idRic.setExtension("4444444444");
		// idRic.setRoot("2.16.840.1.113883.3.3024");
		// ric.getId().add(idRic);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgRic = new
		// COCTMT150002UV01Organization();
		// scopOrgRic.setClassCode("ORG");
		// scopOrgRic.setDeterminerCode("INSTANCE");
		// ric.setScopingOrganization(scopOrgRic);
		//
		// // <urn:id root="2.16.840.1.113883.3.3024"/>
		// II idScopOrgRic = new II();
		// idScopOrgRic.setRoot("2.16.840.1.113883.3.3024");
		// scopOrgRic.getId().add(idScopOrgRic);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--CNH-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs cnh = new PRPAMT201301UV02OtherIDs();
		// cnh.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(cnh);
		//
		// // <urn:id extension="555555555" root="2.16.840.1.113883.13.238"/>
		// II idCnh = new II();
		// idCnh.setExtension("555555555");
		// idCnh.setRoot("2.16.840.1.113883.13.238");
		// cnh.getId().add(idCnh);
		//
		// // <urn:id extension="DF" root="2.16.840.1.113883.4.707"/>
		// II idCnhUf = new II();
		// idCnhUf.setExtension("DF");
		// idCnhUf.setRoot("2.16.840.1.113883.4.707");
		// cnh.getId().add(idCnhUf);
		//
		// // <urn:id extension="20020202" root="2.16.840.1.113883.13.238.1"/>
		// II idCnhDtEmissao = new II();
		// idCnhDtEmissao.setExtension("20020202");
		// idCnhDtEmissao.setRoot("2.16.840.1.113883.13.238.1");
		// cnh.getId().add(idCnhDtEmissao);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgCnh = new
		// COCTMT150002UV01Organization();
		// scopOrgCnh.setClassCode("ORG");
		// scopOrgCnh.setDeterminerCode("INSTANCE");
		// cnh.setScopingOrganization(scopOrgCnh);

		// // <urn:id root="2.16.840.1.113883.13.238"/>
		// II idScopOrgCnh1 = new II();
		// idScopOrgCnh1.setRoot("2.16.840.1.113883.13.238");
		// scopOrgCnh.getId().add(idScopOrgCnh1);
		//
		// // <urn:id root="2.16.840.1.113883.4.707"/>
		// II idScopOrgCnh2 = new II();
		// idScopOrgCnh2.setRoot("2.16.840.1.113883.4.707");
		// scopOrgCnh.getId().add(idScopOrgCnh2);
		//
		// // <urn:id root="2.16.840.1.113883.13.238.1"/>
		// II idScopOrgCnh3 = new II();
		// idScopOrgCnh3.setRoot("2.16.840.1.113883.13.238.1");
		// scopOrgCnh.getId().add(idScopOrgCnh3);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--TITULO_ELEITOR-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs titulo = new PRPAMT201301UV02OtherIDs();
		// titulo.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(titulo);
		//
		// // <urn:id extension="6666666666666"
		// root="2.16.840.1.113883.13.239"/>
		// II idTitulo = new II();
		// idTitulo.setExtension("6666666666666");
		// idTitulo.setRoot("2.16.840.1.113883.13.239");
		// titulo.getId().add(idTitulo);
		//
		// // <urn:id extension="11" root="2.16.840.1.113883.13.239.1"/>
		// II idTituloZona = new II();
		// idTituloZona.setExtension("11");
		// idTituloZona.setRoot("2.16.840.1.113883.13.239.1");
		// titulo.getId().add(idTituloZona);
		//
		// // <urn:id extension="2222" root="2.16.840.1.113883.13.239.2"/>
		// II idTituloSecao = new II();
		// idTituloSecao.setExtension("2222");
		// idTituloSecao.setRoot("2.16.840.1.113883.13.239.2");
		// titulo.getId().add(idTituloSecao);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgTitulo = new
		// COCTMT150002UV01Organization();
		// scopOrgTitulo.setClassCode("ORG");
		// scopOrgTitulo.setDeterminerCode("INSTANCE");
		// titulo.setScopingOrganization(scopOrgTitulo);
		//
		// // <urn:id root="2.16.840.1.113883.13.239"/>
		// II idScopOrgTitulo1 = new II();
		// idScopOrgTitulo1.setRoot("2.16.840.1.113883.13.239");
		// scopOrgTitulo.getId().add(idScopOrgTitulo1);
		//
		// // <urn:id root="2.16.840.1.113883.13.239.1"/>
		// II idScopOrgTitulo2 = new II();
		// idScopOrgTitulo2.setRoot("2.16.840.1.113883.13.239.1");
		// scopOrgTitulo.getId().add(idScopOrgTitulo2);
		//
		// // <urn:id root="2.16.840.1.113883.13.239.2"/>
		// II idScopOrgTitulo3 = new II();
		// idScopOrgTitulo3.setRoot("2.16.840.1.113883.13.239.2");
		// scopOrgTitulo.getId().add(idScopOrgTitulo3);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--RG-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs rg = new PRPAMT201301UV02OtherIDs();
		// rg.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(rg);
		//
		// // <urn:id extension="777777777" root="2.16.840.1.113883.13.243"/>
		// II idRg = new II();
		// idRg.setExtension("777777777");
		// idRg.setRoot("2.16.840.1.113883.13.243");
		// rg.getId().add(idRg);
		//
		// // <urn:id extension="20030303" root="2.16.840.1.113883.13.243.1"/>
		// II idRgDataEmissao = new II();
		// idRgDataEmissao.setExtension("20030303");
		// idRgDataEmissao.setRoot("2.16.840.1.113883.13.243.1");
		// rg.getId().add(idRgDataEmissao);
		//
		// // <urn:id extension="DF" root="2.16.840.1.113883.4.707"/>
		// II idRgUf = new II();
		// idRgUf.setExtension("DF");
		// idRgUf.setRoot("2.16.840.1.113883.4.707");
		// rg.getId().add(idRgUf);
		//
		// // <urn:id extension="10" root="2.16.840.1.113883.13.245"/>
		// II idRgOrgEmissor = new II();
		// idRgOrgEmissor.setExtension("10");
		// idRgOrgEmissor.setRoot("2.16.840.1.113883.13.245");
		// rg.getId().add(idRgOrgEmissor);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgRg = new
		// COCTMT150002UV01Organization();
		// scopOrgRg.setClassCode("ORG");
		// scopOrgRg.setDeterminerCode("INSTANCE");
		// rg.setScopingOrganization(scopOrgRg);
		//
		// // <urn:id root="2.16.840.1.113883.13.243"/>
		// II idScopOrgRg1 = new II();
		// idScopOrgRg1.setRoot("2.16.840.1.113883.13.243");
		// scopOrgRg.getId().add(idScopOrgRg1);
		//
		// // <urn:id root="2.16.840.1.113883.13.243.1"/>
		// II idScopOrgRg2 = new II();
		// idScopOrgRg2.setRoot("2.16.840.1.113883.13.243.1");
		// scopOrgRg.getId().add(idScopOrgRg2);
		//
		// // <urn:id root="2.16.840.1.113883.4.707"/>
		// II idScopOrgRg3 = new II();
		// idScopOrgRg3.setRoot("2.16.840.1.113883.4.707");
		// scopOrgRg.getId().add(idScopOrgRg3);
		//
		// // <urn:id root="2.16.840.1.113883.13.245"/>
		// II idScopOrgRg4 = new II();
		// idScopOrgRg4.setRoot("2.16.840.1.113883.13.245");
		// scopOrgRg.getId().add(idScopOrgRg4);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--CTPS-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs ctps = new PRPAMT201301UV02OtherIDs();
		// ctps.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(ctps);
		//
		// // <urn:id extension="1234" root="2.16.840.1.113883.13.244"/>
		// II idCtps = new II();
		// idCtps.setExtension("1234");
		// idCtps.setRoot("2.16.840.1.113883.13.244");
		// ctps.getId().add(idCtps);
		//
		// // <urn:id extension="123" root="2.16.840.1.113883.13.244.1"/>
		// II idCtpsSerie = new II();
		// idCtpsSerie.setExtension("123");
		// idCtpsSerie.setRoot("2.16.840.1.113883.13.244.1");
		// ctps.getId().add(idCtpsSerie);
		//
		// // <urn:id extension="20040404" root="2.16.840.1.113883.13.244.2"/>
		// II idCtpsDtEmissao = new II();
		// idCtpsDtEmissao.setExtension("20040404");
		// idCtpsDtEmissao.setRoot("2.16.840.1.113883.13.244.2");
		// ctps.getId().add(idCtpsDtEmissao);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgCtps = new
		// COCTMT150002UV01Organization();
		// scopOrgCtps.setClassCode("ORG");
		// scopOrgCtps.setDeterminerCode("INSTANCE");
		// ctps.setScopingOrganization(scopOrgCtps);
		//
		// // <urn:id root="2.16.840.1.113883.13.244"/>
		// II idScopOrgCtps1 = new II();
		// idScopOrgCtps1.setRoot("2.16.840.1.113883.13.244");
		// scopOrgCtps.getId().add(idScopOrgCtps1);
		//
		// // <urn:id root="2.16.840.1.113883.13.244.1"/>
		// II idScopOrgCtps2 = new II();
		// idScopOrgCtps2.setRoot("2.16.840.1.113883.13.244.1");
		// scopOrgCtps.getId().add(idScopOrgCtps2);
		//
		// // <urn:id root="2.16.840.1.113883.13.244.2"/>
		// II idScopOrgCtps3 = new II();
		// idScopOrgCtps3.setRoot("2.16.840.1.113883.13.244.2");
		// scopOrgCtps.getId().add(idScopOrgCtps3);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--OLD CERTIFICATE - MARRIAGE-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs certVelhoCasamento = new
		// PRPAMT201301UV02OtherIDs();
		// certVelhoCasamento.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(certVelhoCasamento);
		//
		// // <urn:id extension="null" root="2.16.840.1.113883.13.241.4"/>
		// II idCertVelhoCasamento = new II();
		// idCertVelhoCasamento.setExtension("null");
		// idCertVelhoCasamento.setRoot("2.16.840.1.113883.13.241.4");
		// certVelhoCasamento.getId().add(idCertVelhoCasamento);
		//
		// // <urn:id extension="NOMECARTORIO1"
		// root="2.16.840.1.113883.4.706.1"/>
		// II idCertVelhoCasamentoNomeCartorio = new II();
		// idCertVelhoCasamentoNomeCartorio.setExtension("NOMECARTORIO1");
		// idCertVelhoCasamentoNomeCartorio.setRoot("2.16.840.1.113883.4.706.1");
		// certVelhoCasamento.getId().add(idCertVelhoCasamentoNomeCartorio);
		//
		// // <urn:id extension="LIVRO1" root="2.16.840.1.113883.4.706.2"/>
		// II idCertVelhoCasamentoLivro = new II();
		// idCertVelhoCasamentoLivro.setExtension("LIVRO1");
		// idCertVelhoCasamentoLivro.setRoot("2.16.840.1.113883.4.706.2");
		// certVelhoCasamento.getId().add(idCertVelhoCasamentoLivro);
		//
		// // <urn:id extension="FOL1" root="2.16.840.1.113883.4.706.3"/>
		// II idCertVelhoCasamentoFolha = new II();
		// idCertVelhoCasamentoFolha.setExtension("FOL1");
		// idCertVelhoCasamentoFolha.setRoot("2.16.840.1.113883.4.706.3");
		// certVelhoCasamento.getId().add(idCertVelhoCasamentoFolha);
		//
		// // <urn:id extension="TERMO1" root="2.16.840.1.113883.4.706.4"/>
		// II idCertVelhoCasamentoTermo = new II();
		// idCertVelhoCasamentoTermo.setExtension("TERMO1");
		// idCertVelhoCasamentoTermo.setRoot("2.16.840.1.113883.4.706.4");
		// certVelhoCasamento.getId().add(idCertVelhoCasamentoTermo);
		//
		// // <urn:id extension="20050505" root="2.16.840.1.113883.4.706.5"/>
		// II idCertVelhoCasamentoDtEmissao = new II();
		// idCertVelhoCasamentoDtEmissao.setExtension("20050505");
		// idCertVelhoCasamentoDtEmissao.setRoot("2.16.840.1.113883.4.706.5");
		// certVelhoCasamento.getId().add(idCertVelhoCasamentoDtEmissao);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgCertVelhoCasamento = new
		// COCTMT150002UV01Organization();
		// scopOrgCertVelhoCasamento.setClassCode("ORG");
		// scopOrgCertVelhoCasamento.setDeterminerCode("INSTANCE");
		// certVelhoCasamento.setScopingOrganization(scopOrgCertVelhoCasamento);
		//
		// // <urn:id root="2.16.840.1.113883.13.241.4"/>
		// II idScopOrgCertVelhoCasamento1 = new II();
		// idScopOrgCertVelhoCasamento1.setRoot("2.16.840.1.113883.13.241.4");
		// scopOrgCertVelhoCasamento.getId().add(idScopOrgCertVelhoCasamento1);
		//
		// // <urn:id root="2.16.840.1.113883.4.706.1"/>
		// II idScopOrgCertVelhoCasamento2 = new II();
		// idScopOrgCertVelhoCasamento2.setRoot("2.16.840.1.113883.4.706.1");
		// scopOrgCertVelhoCasamento.getId().add(idScopOrgCertVelhoCasamento2);
		//
		// // <urn:id root="2.16.840.1.113883.4.706.2"/>
		// II idScopOrgCertVelhoCasamento3 = new II();
		// idScopOrgCertVelhoCasamento3.setRoot("2.16.840.1.113883.4.706.2");
		// scopOrgCertVelhoCasamento.getId().add(idScopOrgCertVelhoCasamento3);
		//
		// // <urn:id root="2.16.840.1.113883.4.706.3"/>
		// II idScopOrgCertVelhoCasamento4 = new II();
		// idScopOrgCertVelhoCasamento4.setRoot("2.16.840.1.113883.4.706.3");
		// scopOrgCertVelhoCasamento.getId().add(idScopOrgCertVelhoCasamento4);
		//
		// // <urn:id root="2.16.840.1.113883.4.706.4"/>
		// II idScopOrgCertVelhoCasamento5 = new II();
		// idScopOrgCertVelhoCasamento5.setRoot("2.16.840.1.113883.4.706.4");
		// scopOrgCertVelhoCasamento.getId().add(idScopOrgCertVelhoCasamento5);
		//
		// // <urn:id root="2.16.840.1.113883.4.706.5"/>
		// II idScopOrgCertVelhoCasamento6 = new II();
		// idScopOrgCertVelhoCasamento6.setRoot("2.16.840.1.113883.4.706.5");
		// scopOrgCertVelhoCasamento.getId().add(idScopOrgCertVelhoCasamento6);
		//
		// // </urn:scopingOrganization>
		// // </urn:asOtherIDs>
		//
		// // <!--NEW CERTIFICATE - BIRTH-->
		// // <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs certNovoNascimento = new
		// PRPAMT201301UV02OtherIDs();
		// certNovoNascimento.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(certNovoNascimento);
		//
		// // <urn:id extension="null" root="2.16.840.1.113883.13.241.1"/>
		// II idCertNovoNascimento = new II();
		// idCertNovoNascimento.setExtension("null");
		// idCertNovoNascimento.setRoot("2.16.840.1.113883.13.241.1");
		// certNovoNascimento.getId().add(idCertNovoNascimento);
		//
		// // <urn:id extension="12345678901234567890123456789012"
		// // root="2.16.840.1.113883.4.706"/>
		// II idCertNovoNascimentoNumero = new II();
		// idCertNovoNascimentoNumero.setExtension("12345678901234567890123456789012");
		// idCertNovoNascimentoNumero.setRoot("2.16.840.1.113883.4.706");
		// certNovoNascimento.getId().add(idCertNovoNascimentoNumero);
		//
		// // <urn:id extension="20060606" root="2.16.840.1.113883.4.706.5"/>
		// II idCertNovoNascimentoDtEmissao = new II();
		// idCertNovoNascimentoDtEmissao.setExtension("20060606");
		// idCertNovoNascimentoDtEmissao.setRoot("2.16.840.1.113883.4.706.5");
		// certNovoNascimento.getId().add(idCertNovoNascimentoDtEmissao);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgCertNovoNascimento = new
		// COCTMT150002UV01Organization();
		// scopOrgCertNovoNascimento.setClassCode("ORG");
		// scopOrgCertNovoNascimento.setDeterminerCode("INSTANCE");
		// certNovoNascimento.setScopingOrganization(scopOrgCertNovoNascimento);
		//
		// // <urn:id root="2.16.840.1.113883.13.241.1"/>
		// II idScopOrgCertNovoNascimento1 = new II();
		// idScopOrgCertNovoNascimento1.setRoot("2.16.840.1.113883.13.241.1");
		// scopOrgCertNovoNascimento.getId().add(idScopOrgCertNovoNascimento1);
		//
		// // <urn:id root="2.16.840.1.113883.4.706"/>
		// II idScopOrgCertNovoNascimento2 = new II();
		// idScopOrgCertNovoNascimento2.setRoot("2.16.840.1.113883.4.706");
		// scopOrgCertNovoNascimento.getId().add(idScopOrgCertNovoNascimento2);
		//
		// // <urn:id root="2.16.840.1.113883.4.706.5"/>
		// II idScopOrgCertNovoNascimento3 = new II();
		// idScopOrgCertNovoNascimento3.setRoot("2.16.840.1.113883.4.706.5");
		// scopOrgCertNovoNascimento.getId().add(idScopOrgCertNovoNascimento3);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <!--DADOS NATURALIZACAO-->
		// <urn:asOtherIDs classCode="ROL">
		// PRPAMT201301UV02OtherIDs dadosNatural = new
		// PRPAMT201301UV02OtherIDs();
		// dadosNatural.getClassCode().add("ROL");
		// person.getAsOtherIDs().add(dadosNatural);
		//
		// // <urn:id extension="99999999" root="2.16.840.1.113883.4.713"/>
		// II idDadosNatural = new II();
		// idDadosNatural.setExtension("99999999");
		// idDadosNatural.setRoot("2.16.840.1.113883.4.713");
		// dadosNatural.getId().add(idDadosNatural);
		//
		// // <urn:id extension="19970101" root="2.16.840.1.113883.4.713.1"/>
		// II idDadosNaturalDtEntradaBr = new II();
		// idDadosNaturalDtEntradaBr.setExtension("19970101");
		// idDadosNaturalDtEntradaBr.setRoot("2.16.840.1.113883.4.713.1");
		// dadosNatural.getId().add(idDadosNaturalDtEntradaBr);
		//
		// // <urn:id extension="19980101" root="2.16.840.1.113883.4.713.2"/>
		// II idDadosNaturalDtNatural = new II();
		// idDadosNaturalDtNatural.setExtension("19980101");
		// idDadosNaturalDtNatural.setRoot("2.16.840.1.113883.4.713.2");
		// dadosNatural.getId().add(idDadosNaturalDtNatural);
		//
		// // <urn:scopingOrganization classCode="ORG"
		// determinerCode="INSTANCE">
		// COCTMT150002UV01Organization scopOrgDadosNatural = new
		// COCTMT150002UV01Organization();
		// scopOrgDadosNatural.setClassCode("ORG");
		// scopOrgDadosNatural.setDeterminerCode("INSTANCE");
		// dadosNatural.setScopingOrganization(scopOrgDadosNatural);
		//
		// // <urn:id root="2.16.840.1.113883.4.713"/>
		// II idScopOrgDadosNatural1 = new II();
		// idScopOrgDadosNatural1.setRoot("2.16.840.1.113883.4.713");
		// scopOrgDadosNatural.getId().add(idScopOrgDadosNatural1);
		//
		// // <urn:id root="2.16.840.1.113883.4.713.1"/>
		// II idScopOrgDadosNatural2 = new II();
		// idScopOrgDadosNatural2.setRoot("2.16.840.1.113883.4.713.1");
		// scopOrgDadosNatural.getId().add(idScopOrgDadosNatural2);
		//
		// // <urn:id root="2.16.840.1.113883.4.713.2"/>
		// II idScopOrgDadosNatural3 = new II();
		// idScopOrgDadosNatural3.setRoot("2.16.840.1.113883.4.713.2");
		// scopOrgDadosNatural.getId().add(idScopOrgDadosNatural3);

		// </urn:scopingOrganization>
		// </urn:asOtherIDs>

		// <urn:personalRelationship classCode="PRS">
		// PRPAMT201301UV02PersonalRelationship relationPai = new
		// PRPAMT201301UV02PersonalRelationship();
		// relationPai.getClassCode().add("PRS");
		// person.getPersonalRelationship().add(relationPai);
		//
		// // <urn:code code="NPRN" codeSystem="2.16.840.1.113883.1.11.19563" />
		// CE codeRelPai = new CE();
		// codeRelPai.setCode("NPRN");
		// codeRelPai.setCodeSystem("2.16.840.1.113883.1.11.19563");
		// relationPai.setCode(codeRelPai);
		//
		// // <urn:relationshipHolder1 classCode="PSN"
		// determinerCode="INSTANCE">
		// COCTMT030007UVPerson personPai = new COCTMT030007UVPerson();
		// personPai.getClassCode().add("PSN");
		// personPai.setDeterminerCode(XDeterminerInstanceKind.INSTANCE);
		// relationPai.setRelationshipHolder1(factory.createPRPAMT201301UV02PersonalRelationshipRelationshipHolder1(personPai));
		//
		// // <urn:name use="L">
		// EN nomePai = new EN();
		// nomePai.getUse().add("L");
		// personPai.getName().add(nomePai);
		//
		// // <urn:given>THOMAS WAYNE</urn:given>
		// EnGiven givenNomePai = new EnGiven();
		// givenNomePai.getContent().add("THOMAS WAYNE");
		// nomePai.getContent().add(factory.createENGiven(givenNomePai));

		// </urn:name>
		// </urn:relationshipHolder1>
		// </urn:personalRelationship>

		// Nome da m\E3e, caso n\E3o exista colocar "SEM INFORMA\C7\C3O"
		// <urn:personalRelationship classCode="PRS">
		PRPAMT201301UV02PersonalRelationship relationMae = new PRPAMT201301UV02PersonalRelationship();
		relationMae.getClassCode().add("PRS");
		person.getPersonalRelationship().add(relationMae);

		// <urn:code code="PRN" codeSystem="2.16.840.1.113883.1.11.19563" />
		CE codeRelMae = new CE();
		codeRelMae.setCode("PRN");
		codeRelMae.setCodeSystem("2.16.840.1.113883.1.11.19563");
		relationMae.setCode(codeRelMae);

		// <urn:relationshipHolder1 classCode="PSN" determinerCode="INSTANCE">
		COCTMT030007UVPerson personMae = new COCTMT030007UVPerson();
		personMae.getClassCode().add("PSN");
		personMae.setDeterminerCode(XDeterminerInstanceKind.INSTANCE);
		relationMae
				.setRelationshipHolder1(factory
						.createPRPAMT201301UV02PersonalRelationshipRelationshipHolder1(personMae));

		// <urn:name use="L">
		EN nomeMae = new EN();
		nomeMae.getUse().add("L");
		personMae.getName().add(nomeMae);

		// <urn:given>MARTHA WAYNE</urn:given>
		EnGiven givenNomeMae = new EnGiven();
		givenNomeMae.getContent().add("MAE DO CIRSTO REDENTOR");
		nomeMae.getContent().add(factory.createENGiven(givenNomeMae));

		// </urn:name>
		// </urn:relationshipHolder1>
		// </urn:personalRelationship>

		// Municipio de nascimento, caso n\E3o exista mandar 999999
		// <urn:birthPlace classCode="BIRTHPL" determinerCode="INSTANCE">
		PRPAMT201301UV02BirthPlace birthPlace = new PRPAMT201301UV02BirthPlace();
		birthPlace.getClassCode().add("BIRTHPL");
		// LINHA COMENTADA ABAIXO NA EXISTE NO SCHEMA IMPORTADO
		// birthPlace.setDeterminerCode("INSTANCE");
		person.setBirthPlace(factory
				.createPRPAMT201301UV02NonPersonLivingSubjectBirthPlace(birthPlace));

		// <urn:addr>
		AD enderecoNascimento = new AD();
		birthPlace.setAddr(enderecoNascimento);

		// <urn:city>530010</urn:city>
		AdxpCity cidadeNascimento = new AdxpCity();
		cidadeNascimento.getContent().add("999999");
		enderecoNascimento.getContent().add(
				factory.createADCity(cidadeNascimento));

		// <urn:country>1</urn:country>
		AdxpCountry paisNascimento = new AdxpCountry();
		paisNascimento.getContent().add("010");
		enderecoNascimento.getContent().add(
				factory.createADCountry(paisNascimento));

		// </urn:addr>
		// </urn:birthPlace>

		// <urn:languageCommunication>
		PRPAMT201301UV02LanguageCommunication portugues = new PRPAMT201301UV02LanguageCommunication();
		person.getLanguageCommunication().add(portugues);

		// <urn:languageCode code="pt" codeSystem="2.16.840.1.113883.6.100"/>
		CE codePortugues = new CE();
		codePortugues.setCode("pt");
		codePortugues.setCodeSystem("2.16.840.1.113883.6.100");
		portugues.setLanguageCode(codePortugues);

		// <urn:preferenceInd value="true"/>
		BL prefPortugues = new BL();
		prefPortugues.setValue(true);
		portugues.setPreferenceInd(prefPortugues);

		// </urn:languageCommunication>

		// <urn:languageCommunication>
		PRPAMT201301UV02LanguageCommunication ingles = new PRPAMT201301UV02LanguageCommunication();
		person.getLanguageCommunication().add(ingles);

		// <urn:languageCode code="en" codeSystem="2.16.840.1.113883.6.100"/>
		CE codeIngles = new CE();
		codeIngles.setCode("en");
		codeIngles.setCodeSystem("2.16.840.1.113883.6.100");
		ingles.setLanguageCode(codeIngles);

		// <urn:preferenceInd value="false"/>
		BL prefIngles = new BL();
		prefIngles.setValue(false);
		ingles.setPreferenceInd(prefIngles);

		// </urn:languageCommunication>
		// </urn:patientPerson>

		// <urn:providerOrganization classCode="ORG" determinerCode="INSTANCE">
		COCTMT150003UV03Organization providerOrg = new COCTMT150003UV03Organization();
		providerOrg.setClassCode("ORG");
		providerOrg.setDeterminerCode("INSTANCE");
		patient.setProviderOrganization(providerOrg);

		// <urn:id root="2.16.840.1.113883.13.236"/>
		II idProviderOrg = new II();
		idProviderOrg.setRoot("2.16.840.1.113883.13.236");
		providerOrg.getId().add(idProviderOrg);

		// <urn:contactParty classCode="CON"/>
		COCTMT150003UV03ContactParty contactParty = new COCTMT150003UV03ContactParty();
		contactParty.setClassCode(RoleClassContact.CON);
		providerOrg.getContactParty().add(contactParty);

		// </urn:providerOrganization>
		// </urn:patient>
		// </urn:subject1>
		// </urn:registrationEvent>
		// </urn:subject>
		// </urn:controlActProcess>
		// </urn:PRPA_IN201301UV02>
		// </env:Body>

		return prpain;
	}
}
