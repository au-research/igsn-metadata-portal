package au.edu.ardc.igsn.igsnportal.service;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Service
public class UserService {

	KeycloakSpringBootProperties kcProperties;

	Logger logger = LoggerFactory.getLogger(UserService.class);

	public UserService(KeycloakSpringBootProperties kcProperties) {
		this.kcProperties = kcProperties;
	}

	public boolean isLoggedIn(HttpServletRequest request) throws ServletException {
		logger.debug("attempting to check if the user is logged in");
		KeycloakSecurityContext keycloakSecurityContext = (KeycloakSecurityContext) (request
				.getAttribute(KeycloakSecurityContext.class.getName()));
		if (keycloakSecurityContext == null) {
			logger.debug("keycloakSecurityContext not found!");
			return false;
		}

		logger.debug("keycloakSecurityContext found");
		RefreshableKeycloakSecurityContext ksc = (RefreshableKeycloakSecurityContext) keycloakSecurityContext;
		String refreshToken = ksc.getRefreshToken();
		try {
			logger.debug("Attempting to refresh the token with refreshToken:" + refreshToken);
			AccessTokenResponse refreshResponse = refreshToken(refreshToken);
			logger.debug("AccessTokenResponse: " + refreshResponse.getSessionState());
		}
		catch (Exception e) {
			logger.debug("Exception found while refreshingToken, session might already expired?");
			request.logout();
			return false;
		}

		return true;
	}

	public Principal getPrincipal(HttpServletRequest request) {
		// if (principal instanceof KeycloakPrincipal) {
		// AccessToken accessToken = ((KeycloakPrincipal)
		// principal).getKeycloakSecurityContext().getToken();
		// String preferredUsername = accessToken.getPreferredUsername();
		// AccessToken.Access realmAccess = accessToken.getRealmAccess();
		// Set<String> roles = realmAccess.getRoles();
		// log.info(Keycloak User: ：{}, Roles：{}", preferredUsername, roles);
		// }
		return request.getUserPrincipal();
	}

	public org.keycloak.authorization.client.Configuration kcConfig() {
		return new org.keycloak.authorization.client.Configuration(kcProperties.getAuthServerUrl(),
				kcProperties.getRealm(), kcProperties.getResource(), kcProperties.getCredentials(), null);
	}

	public AccessTokenResponse refreshToken(String refreshToken) {
		String url = kcProperties.getAuthServerUrl() + "/realms/" + kcProperties.getRealm()
				+ "/protocol/openid-connect/token";
		String clientId = kcProperties.getResource();
		String secret = (String) kcProperties.getCredentials().get("secret");
		Configuration kcConfig = kcConfig();
		Http http = new Http(kcConfig, (params, headers) -> {
		});

		return http.<AccessTokenResponse>post(url).authentication().client().form().param("grant_type", "refresh_token")
				.param("refresh_token", refreshToken).param("client_id", clientId).param("client_secret", secret)
				.response().json(AccessTokenResponse.class).execute();
	}

	public String getPlainAccessToken(HttpServletRequest request) {
		logger.debug("Obtaining PlainAccessToken for current request");
		KeycloakSecurityContext keycloakSecurityContext = (KeycloakSecurityContext) (request
				.getAttribute(KeycloakSecurityContext.class.getName()));
		return keycloakSecurityContext.getTokenString();
	}

	public String getCurrentPath(HttpServletRequest request) {
		return request.getRequestURI().substring(request.getContextPath().length());
	}

	public String getCurrentURL(HttpServletRequest request) {
		return request.getRequestURL().toString();
	}

}
