package br.com.dextra.dexboard.api.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;

public class Authenticator implements ClientRequestFilter {
    
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        headers.add("Authorization", this.base64Token("user", "user"));
    }
    
    public String base64Token(String user, String password) {
        try {
            byte[] auth = (user + ":" + password).getBytes("UTF-8");
            return "BASIC " + DatatypeConverter.printBase64Binary(auth);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
}