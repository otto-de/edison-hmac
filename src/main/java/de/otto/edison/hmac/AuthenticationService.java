package de.otto.edison.hmac;

import de.otto.lhotse.hmac.HmacSignatureCalculator;
import de.otto.lhotse.hmac.HmacSignatureInfo;
import org.springframework.core.env.Environment;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class AuthenticationService {

    private Environment environment;

    public static final String X_HMAC_AUTH_SIGNATURE = "x-hmac-auth-signature";
    private HmacSignatureCalculator hmacSignatureCalculator = new HmacSignatureCalculator();

    public String validateHmacSignature(HttpServletRequest request) {
        String sigHeader = request.getHeader(X_HMAC_AUTH_SIGNATURE);
        if(sigHeader==null) {
            throw new AuthenticationException("hmac header not set");
        }
        String[] split = sigHeader.split(":");
        String username = split[0];
        String secretKey = environment.getProperty("hmac." + username + ".key");

        try {
            ZonedDateTime dateHeader = ZonedDateTime.from(Instant.ofEpochMilli(request.getDateHeader("x-hmac-auth-date")));
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
