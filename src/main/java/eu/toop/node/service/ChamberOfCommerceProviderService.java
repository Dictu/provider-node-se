package eu.toop.node.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import eu.toop.node.model.Address;
import eu.toop.node.model.ChamberOfCommerceDataSet;
import eu.toop.node.model.Constants;
import eu.toop.node.model.DataSet;
import eu.toop.node.provider.ProviderService;
import eu.toop.node.util.WSClient;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.GrundlaggandeUppgifterSvar;

@Service
public class ChamberOfCommerceProviderService extends WebServiceGatewaySupport implements ProviderService {

	@Autowired
	private WSClient<GrundlaggandeUppgifterSvar> ssbtguService;
	
	@Override
	public DataSet provide(String nr) {
		GrundlaggandeUppgifterSvar gs = ssbtguService.provide(nr);
		ChamberOfCommerceDataSet set = new ChamberOfCommerceDataSet();
		set.setCompanyCode(nr);
		set.setCompanyName(gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0001().getForetagNamn());
		if (null != gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0025().getForetagsform()) {
			set.setCompanyType(gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0025().getForetagsform().getForetagsformBeskrivning());
		}
		else {
			set.setCompanyType(Constants.NOT_AVAILABLE);
		}
		if (null != gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0027().getAvvecklingsforfarande()) {
			set.setLegalStatus(gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0027().getAvvecklingsforfarande().getAvvecklingAvfordAnledning().getAvvecklingBeskrivning());
		}
		else {
			set.setLegalStatus(Constants.NOT_AVAILABLE);
		}
		if (null != gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0003().getPostadress()) {
			Address address = new Address();
			address.setPostalCode(gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0003().getPostadress().getPostnummer());
			address.setCity(gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0003().getPostadress().getPostort());
			address.setCountry(gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0003().getPostadress().getLand());
			address.setStreetName(gs.getGrundlaggandeUppgifterSvarDetaljer().getUD0003().getPostadress().getUtdelningsadress1());
			set.setHeadOfficeAddres(address);
		}
		else {
			Address address = new Address();
			address.setPostalCode(Constants.NOT_AVAILABLE);
			address.setCity(Constants.NOT_AVAILABLE);
			address.setCountry(Constants.NOT_AVAILABLE);
			address.setStreetName(Constants.NOT_AVAILABLE);
			set.setHeadOfficeAddres(address);
		}
		set.setActivityDeclaration(Constants.NOT_AVAILABLE);
		set.setLegalStatusEffectiveDate(Constants.NOT_AVAILABLE);
		set.setRegistrationAuthority(Constants.NOT_AVAILABLE);
		set.setRegistrationDate(Constants.NOT_AVAILABLE);
		set.setRegistrationNumber(Constants.NOT_AVAILABLE);
		return set;
	}

}
