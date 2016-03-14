package de.otto.edison.hmac;

import de.otto.lhotse.hmac.HmacSignatureCalculator;
import de.otto.lhotse.hmac.HmacSignatureInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AuthenticationService  {

    public static final String X_HMAC_AUTH_SIGNATURE = "x-hmac-auth-signature";
    private HmacSignatureCalculator hmacSignatureCalculator = new HmacSignatureCalculator();

    private final Config config;

    @Autowired
    public AuthenticationService(final Config config) {
        this.config = config;
    }

    public String validateHmacSignature(HttpServletRequest request) {
        String sigHeader = request.getHeader(X_HMAC_AUTH_SIGNATURE);
        if(sigHeader==null) {
            throw new AuthenticationException("hmac header not set");
        }
        String[] split = sigHeader.split(":");
        String username = split[0];
        String secretKey = "";
        // String secretKey = environment.getProperty("edison.hmac." + username + ".key"); // TODO: Can not load property from envvironment -> null

        try {
            ZonedDateTime dateHeader = ZonedDateTime.from(OffsetDateTime.parse(request.getHeader("x-hmac-auth-date")));
            ZonedDateTime now = ZonedDateTime.now();
            if(dateHeader.isBefore(now.minus(5, ChronoUnit.MINUTES)) || dateHeader.isAfter(now.plus(5, ChronoUnit.MINUTES))){
                throw new AuthenticationException("Authentication failed: date not valid.");
            }
            byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
            HmacSignatureInfo sentSignature = hmacSignatureCalculator.calculateSignature(request.getPathInfo(), username, secretKey,
                    body, request.getMethod(), dateHeader);
            if(!sentSignature.getSignature().equals(sigHeader)) {
                throw new AuthenticationException("Authentication failed: signature not valid.");
            }
            return username;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
