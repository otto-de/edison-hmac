package de.otto.edison.hmac;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AuthenticationServiceTest {

    AuthenticationService testee = new AuthenticationService();

    MockHttpServletRequest req = new MockHttpServletRequest();

    @Test(expectedExceptions = AuthenticationException.class)
    public void shouldName() throws Exception {
        try {
            // when
            testee.validateHmacSignature(req);
        }
        catch(AuthenticationException e) {
            // then
            assertThat(e.getMessage(), is("hmac header not set"));
            throw e;
        }
    }
    @Test(expectedExceptions = AuthenticationException.class)
    public void shouldThrowExceptionDueToInvalidHeader() throws Exception{
        try{
            //when
            req.addHeader("Date", RFC_1123_DATE_TIME.format(LocalDateTime.now().plus(5,ChronoUnit.MINUTES)));
            testee.validateHmacSignature(req);
        }
        catch(AuthenticationException e) {
            // then
            assertThat(e.getMessage(), is("Authentication failed: date not valid."));
            throw e;
        }
    }
}