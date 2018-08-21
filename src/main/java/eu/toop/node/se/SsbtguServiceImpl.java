package eu.toop.node.se;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerException;

import org.joda.time.DateTime;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import eu.toop.node.util.WSClient;
import se.bolagsverket.schema.ssbt.foretag.Funktionar;
import se.bolagsverket.schema.ssbt.foretag.JaNej;
import se.bolagsverket.schema.ssbt.foretag.PersonIdentitetsbeteckning;
import se.bolagsverket.schema.ssbt.metadata.Part;
import se.bolagsverket.schema.ssbt.metadata.PartId;
import se.bolagsverket.schema.ssbt.metadata.Service;
import se.bolagsverket.schema.ssbt.metadata.ServicePart;
import se.bolagsverket.schema.ssbt.rollintyg.Roll;
import se.bolagsverket.schema.ssbt.rollintyg.RollIntyg;
import se.bolagsverket.schema.ssbt.rollintyg.RollIntygMetadata;
import se.bolagsverket.schema.ssbt.rollintyg.RollIntygUtfardatAv;
import se.bolagsverket.schema.ssbt.rollintyg.RollIntygUtfardatForForetag;
import se.bolagsverket.schema.ssbt.rollintyg.RollIntygUtfardatForPerson;
import se.bolagsverket.schema.ssbt.rollintyg.RollIntygUtfardatTill;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.ForetagId;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.GrundlaggandeUppgiftId;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.GrundlaggandeUppgifterBegaran;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.GrundlaggandeUppgifterBegaranDetaljer;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.GrundlaggandeUppgifterBegaranMetadata;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.GrundlaggandeUppgifterSvar;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.ObjectFactory;

public class SsbtguServiceImpl extends WSClient<GrundlaggandeUppgifterSvar> {

	@SuppressWarnings("unchecked")
	@Override
	public GrundlaggandeUppgifterSvar provide(String nr) {
		try {
			GrundlaggandeUppgifterBegaran gb = getGrunlaggandeUppgifter(nr);
			JAXBElement<GrundlaggandeUppgifterBegaran> grundlaggandeUppgifterBegaran = new se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.ObjectFactory().createGrundlaggandeUppgifterBegaran(gb);
			WebServiceMessageCallback callback = new WebServiceMessageCallback() {
				
				@Override
				public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
					SaajSoapMessage saajSoapMessage = (SaajSoapMessage) message;
		            SOAPMessage soapMessage = saajSoapMessage.getSaajMessage();
		            try {
						SOAPHeader header = soapMessage.getSOAPHeader();
						header.detachNode();
					} catch (SOAPException e) {
						throw new IOException(e);
					}
				}
			};
			JAXBElement<GrundlaggandeUppgifterSvar> resp = (JAXBElement<GrundlaggandeUppgifterSvar>) getProxiedWebServiceTemplate().marshalSendAndReceive(grundlaggandeUppgifterBegaran, callback);
			return resp.getValue();
		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}
	
	

	private GrundlaggandeUppgifterBegaran getGrunlaggandeUppgifter(String nr) throws DatatypeConfigurationException {
		GrundlaggandeUppgifterBegaran gb = new ObjectFactory().createGrundlaggandeUppgifterBegaran();
		gb.setSchemaVersion("2.2.0");
		gb.setGrundlaggandeUppgifterBegaranDetaljer(getGrundlaggandeUppgifterBegaranDetaljer(nr));
		gb.setGrundlaggandeUppgifterBegaranMetadata(getGrundlaggandeUppgifterBegaranMetadata());
		return gb;
	}
	
	private GrundlaggandeUppgifterBegaranDetaljer getGrundlaggandeUppgifterBegaranDetaljer(String nr) throws DatatypeConfigurationException {
		GrundlaggandeUppgifterBegaranDetaljer grundlaggandeUppgifterBegaranDetaljer = new ObjectFactory().createGrundlaggandeUppgifterBegaranDetaljer();
		grundlaggandeUppgifterBegaranDetaljer.setForetagId(getForetagId(nr));
		
		grundlaggandeUppgifterBegaranDetaljer.getGrundlaggandeUppgiftId().add(GrundlaggandeUppgiftId.UD_0001);
		grundlaggandeUppgifterBegaranDetaljer.getGrundlaggandeUppgiftId().add(GrundlaggandeUppgiftId.UD_0003);
		grundlaggandeUppgifterBegaranDetaljer.getGrundlaggandeUppgiftId().add(GrundlaggandeUppgiftId.UD_0025);
		grundlaggandeUppgifterBegaranDetaljer.getGrundlaggandeUppgiftId().add(GrundlaggandeUppgiftId.UD_0027);
		
		grundlaggandeUppgifterBegaranDetaljer.setRollIntyg(getRollIntyg());
		return grundlaggandeUppgifterBegaranDetaljer;
	}

	private RollIntyg getRollIntyg() throws DatatypeConfigurationException {
		RollIntyg rollIntyg = new se.bolagsverket.schema.ssbt.rollintyg.ObjectFactory().createRollIntyg();
		rollIntyg.setRollIntygMetadata(getRollIntygMetadata());
		rollIntyg.setRoll(getRoll());
		return rollIntyg;
	}

	private Roll getRoll() {
		Roll roll =  new se.bolagsverket.schema.ssbt.rollintyg.ObjectFactory().createRoll();
		roll.setFirmatecknare(JaNej.JA);
		Funktionar funktionar1 = new se.bolagsverket.schema.ssbt.foretag.ObjectFactory().createFunktionar();
		funktionar1.setFunktionarBeskrivning("styrelseledamot");
		funktionar1.setFunktionarKod("LE");
		Funktionar funktionar2 = new se.bolagsverket.schema.ssbt.foretag.ObjectFactory().createFunktionar();
		funktionar2.setFunktionarBeskrivning("verkställande direktor");
		funktionar2.setFunktionarKod("VD");
		roll.getFunktionar().add(funktionar1);
		roll.getFunktionar().add(funktionar2);
		return roll;
	}

	private RollIntygMetadata getRollIntygMetadata() throws DatatypeConfigurationException {
		RollIntygMetadata rollIntygMetadata = new se.bolagsverket.schema.ssbt.rollintyg.ObjectFactory().createRollIntygMetadata();
		rollIntygMetadata.setRollIntygId(UUID.randomUUID().toString());
		rollIntygMetadata.setRollIntygUtfardat(getXMLGregorianCalendar(DateTime.now()));
		rollIntygMetadata.setRollIntygUtfardatAv(getRollIntygUtfardatAv());
		rollIntygMetadata.setRollIntygUtfardatTill(getRollIntygUtfardatTill());
		rollIntygMetadata.setRollIntygUtfardatForPerson(getRollIntygUtfardatForPerson());
		rollIntygMetadata.setRollIntygUtfardatForForetag(getRollIntygUtfardatForForetag());
		return rollIntygMetadata;
	}

	private RollIntygUtfardatForForetag getRollIntygUtfardatForForetag() {
		RollIntygUtfardatForForetag rollIntygUtfardatForForetag = new se.bolagsverket.schema.ssbt.rollintyg.ObjectFactory().createRollIntygUtfardatForForetag();
		PersonIdentitetsbeteckning personIdentitetsbeteckning = new se.bolagsverket.schema.ssbt.foretag.ObjectFactory().createPersonIdentitetsbeteckning();
		personIdentitetsbeteckning.setOrganisationsnummer("5560000003");
		rollIntygUtfardatForForetag.setPersonIdentitetsbeteckning(personIdentitetsbeteckning);
		return rollIntygUtfardatForForetag;
	}

	private RollIntygUtfardatForPerson getRollIntygUtfardatForPerson() {
		RollIntygUtfardatForPerson rollIntygUtfardatForPerson  = new se.bolagsverket.schema.ssbt.rollintyg.ObjectFactory().createRollIntygUtfardatForPerson();
		PersonIdentitetsbeteckning personIdentitetsbeteckning = new se.bolagsverket.schema.ssbt.foretag.ObjectFactory().createPersonIdentitetsbeteckning();
		personIdentitetsbeteckning.setPersonnummer("198001011234");
		rollIntygUtfardatForPerson.setPersonIdentitetsbeteckning(personIdentitetsbeteckning);
		return rollIntygUtfardatForPerson;
	}

	private RollIntygUtfardatAv getRollIntygUtfardatAv() {
		RollIntygUtfardatAv rollIntygUtfardatAv = new se.bolagsverket.schema.ssbt.rollintyg.ObjectFactory().createRollIntygUtfardatAv();
		rollIntygUtfardatAv.setDataproducent(getServicePart("Bolagsverket", "2021005489", ""));
		return rollIntygUtfardatAv;
	}
	
	private RollIntygUtfardatTill getRollIntygUtfardatTill() {
		RollIntygUtfardatTill rollIntygUtfardatTill = new se.bolagsverket.schema.ssbt.rollintyg.ObjectFactory().createRollIntygUtfardatTill();
		rollIntygUtfardatTill.setDatakonsument(getServicePart("Datakonsumentverket", "2021001234", "E-tjänst1"));
		return rollIntygUtfardatTill;
	}
	
	private ForetagId getForetagId(String nr) {
		ForetagId foretagId = new ObjectFactory().createForetagId();
		PersonIdentitetsbeteckning personIdentitetsbeteckning = new se.bolagsverket.schema.ssbt.foretag.ObjectFactory().createPersonIdentitetsbeteckning();
		personIdentitetsbeteckning.setOrganisationsnummer(nr);
		foretagId.setPersonIdentitetsbeteckning(personIdentitetsbeteckning);
		return foretagId;
	}

	private GrundlaggandeUppgifterBegaranMetadata getGrundlaggandeUppgifterBegaranMetadata() throws DatatypeConfigurationException {
		GrundlaggandeUppgifterBegaranMetadata grundlaggandeUppgifterBegaranMetadata = new ObjectFactory().createGrundlaggandeUppgifterBegaranMetadata();
		grundlaggandeUppgifterBegaranMetadata.setMeddelandeId(UUID.randomUUID().toString());
		grundlaggandeUppgifterBegaranMetadata.setTransaktionId(UUID.randomUUID().toString());
		grundlaggandeUppgifterBegaranMetadata.setTidstampel(getXMLGregorianCalendar(DateTime.now()));
		grundlaggandeUppgifterBegaranMetadata.setDatakonsument(getServicePart("Datakonsumentverket", "2021001234", "E-tjänst1"));
		grundlaggandeUppgifterBegaranMetadata.setAnvandare(getPart());
		return grundlaggandeUppgifterBegaranMetadata;
	}

	private Part getPart() {
		Part part = new se.bolagsverket.schema.ssbt.metadata.ObjectFactory().createPart();
		PartId partId = new se.bolagsverket.schema.ssbt.metadata.ObjectFactory().createPartId();
		partId.setPersonnummer("198001011234");
		part.setPartId(partId);
		part.setPartNamn("Sander");
		return part;
	}

	private ServicePart getServicePart(String partNam, String organisationsnummer, String serviceNamn) {
		ServicePart servicePart = new se.bolagsverket.schema.ssbt.metadata.ObjectFactory().createServicePart();
		servicePart.setPartNamn(partNam);
		PartId partId = new se.bolagsverket.schema.ssbt.metadata.ObjectFactory().createPartId();
		partId.setOrganisationsnummer(organisationsnummer);
		servicePart.setPartId(partId);
		if (null != serviceNamn && !serviceNamn.isEmpty()) {
			Service service = new se.bolagsverket.schema.ssbt.metadata.ObjectFactory().createService();
			service.setServiceNamn(serviceNamn);
			servicePart.setService(service);
		}
		return servicePart;
	}
	
	public static XMLGregorianCalendar getXMLGregorianCalendar(DateTime dateTime) throws DatatypeConfigurationException {
		XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dateTime.toDate()));
		calendar.setTimezone(0);
		return calendar;
	}
}
