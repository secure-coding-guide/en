package org.jssec.android.accountmanager.webservice;

public class WebService {

    /**
     * Suppose to access to account managemnet function of online service.
     * 
     * @param username Account name character string
     * @param password password character string
     * @return Return authentication token
     */
    public String login(String username, String password) {
        // *** POINT 7 *** HTTPS should be used for communication between an authenticator and the online services.
        // Actually, communication process with servers is implemented here, but Omit here, since this is a sample.
        return getAuthToken(username, password);
    }

    private String getAuthToken(String username, String password) {
        // In fact, get the value which uniqueness and impossibility of speculation are guaranteed by the server,
        // but the fixed value is returned without communication here, since this is sample.
        return "c2f981bda5f34f90c0419e171f60f45c";
    }
}
