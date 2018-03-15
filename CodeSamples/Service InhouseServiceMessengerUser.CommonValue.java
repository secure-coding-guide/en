package org.jssec.android.service.inhouseservice.messengeruser;

public class CommonValue {
    /**
     * Command to register as a client
     * Set Messenger to Message.replyTo field 
     */
    public static final int MSG_REGISTER_CLIENT = 1;

    /**
     * Command to unregister a client
     * Set Messenger to Message.replyTo field  
     */
    public static final int MSG_UNREGISTER_CLIENT = 2;

    /**
     * Command to send data to registered clients
     */
    public static final int MSG_SET_VALUE = 3;
}
