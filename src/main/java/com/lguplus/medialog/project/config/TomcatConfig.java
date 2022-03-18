package com.lguplus.medialog.project.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Apache - mod_jk - tomcat 연동 등 Embedded Tomcat을 설정한다.
 */
@Configuration
public class TomcatConfig {

	@Bean
	public ServletWebServerFactory servletContainer(TomcatAjpSettings ajp) {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		if (ajp.isEnabled()) {
			Connector ajpConnector = new Connector("AJP/1.3");
			ajpConnector.setPort(ajp.getPort());
			ajpConnector.setSecure(false);
			ajpConnector.setAllowTrace(false);
			ajpConnector.setScheme("http");
			((AbstractAjpProtocol<?>) ajpConnector.getProtocolHandler()).setSecretRequired(false);
			tomcat.addAdditionalTomcatConnectors(ajpConnector);
		}
		return tomcat;
	}

	@Component
	@ConfigurationProperties("app.settings.tomcat.ajp")
	@Data
	public static class TomcatAjpSettings {
	    private boolean enabled;
	    private int port;
	    private boolean remoteauthentication;
	}
}
