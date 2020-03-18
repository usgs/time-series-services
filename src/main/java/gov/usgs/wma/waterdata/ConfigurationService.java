package gov.usgs.wma.waterdata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
	
	@Value("${site.url.base}")
	private String serverUrl;
	
	@Value("${collections.monitoring-locations.title}")
	private String monLocTitle;

	@Value("${collections.monitoring-locations.description}")
	private String monLocDescription;

	@Value("${collections.monitoring-locations.contactName}")
	private String monLocContactName;

	@Value("${collections.monitoring-locations.contactEmail}")
	private String monLocContactEmail;

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getMonLocTitle() {
		return monLocTitle;
	}

	public void setMonLocTitle(String monLocTitle) {
		this.monLocTitle = monLocTitle;
	}

	public String getMonLocDescription() {
		return monLocDescription;
	}

	public void setMonLocDescription(String monLocDescription) {
		this.monLocDescription = monLocDescription;
	}

	public String getMonLocContactName() {
		return monLocContactName;
	}

	public void setMonLocContactName(String monLocContactName) {
		this.monLocContactName = monLocContactName;
	}

	public String getMonLocContactEmail() {
		return monLocContactEmail;
	}

	public void setMonLocContactEmail(String monLocContactEmail) {
		this.monLocContactEmail = monLocContactEmail;
	}

}