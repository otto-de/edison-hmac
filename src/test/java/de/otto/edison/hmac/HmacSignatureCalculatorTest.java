package de.otto.edison.hmac;

import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HmacSignatureCalculatorTest {

        private HmacSignatureCalculator subject = new HmacSignatureCalculator();

        @Test
        public void shouldSignARequestWithBody() throws Exception {
            // Given
            String url = "http://someUrl.de/bla/blupp?frage=ja&hein=bloed";
            String user = "hmacUser";
            String pw = "hmacPw";
            byte[] body = "Hello Body".getBytes("UTF-8");
            String method = "POST";
            ZonedDateTime dateTime = ZonedDateTime.of(2012, 05, 01, 03, 16, 0, 0, ZoneId.of("CET"));

            // When
            HmacSignatureInfo result = subject.calculateSignature(url, user, pw, body, method, dateTime);

            // Then
            assertThat(result.getSignature(), is("hmacUser:EKHBRoKoEhknkvVyH6uGHYjNiQguodKBol8s/P4lXu4="));
            assertThat(result.getDate(), is(DateTimeFormatter.ISO_INSTANT.format(dateTime)));
        }

        @Test
        public void shouldSignARequestWithoutBody() throws Exception {
            // Given
            String url = "http://someUrl.de/bla/blupp?frage=ja&hein=bloed";
            String user = "hmacUser";
            String pw = "hmacPw";
            String method = "GET";
            ZonedDateTime dateTime = ZonedDateTime.of(2012, 05, 01, 03, 16, 0, 0, ZoneId.of("CET"));

            // When
            HmacSignatureInfo result = subject.calculateSignature(url, user, pw, method, dateTime);

            // Then
            assertThat(result.getSignature(), is("hmacUser:XNIFB5pB+DLMO9WXhZdLwetdHa9W9MwXE0tO/GQu+X8="));
            assertThat(result.getDate(), is(DateTimeFormatter.ISO_INSTANT.format(dateTime)));
        }

    @Test
    public void shouldSignARequestWithBodyWithSimpleFormat() throws Exception {
        // Given
        String url = "http://someUrl.de/bla/blupp?frage=ja&hein=bloed";
        String user = "hmacUser";
        String pw = "hmacPw";
        byte[] body = "Hello Body".getBytes("UTF-8");
        String method = "POST";
        ZonedDateTime dateTime = ZonedDateTime.of(2012, 05, 01, 03, 16, 0, 0, ZoneId.of("CET"));

        // When
        HmacSignatureInfo result = subject.calculateSignature(url, user, pw, body, method, dateTime, true);

        // Then
        assertThat(result.getSignature(), is("hmacUser:Zy+QHNUoo0ydUWeF8nA5TUBRlkONjAb6w0kHRki7q5U="));
        assertThat(result.getDate(), is(DateTimeFormatter.ISO_INSTANT.format(dateTime)));
    }
}