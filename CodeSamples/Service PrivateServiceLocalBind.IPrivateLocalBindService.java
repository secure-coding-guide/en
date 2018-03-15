package org.jssec.android.service.privateservice.localbind;

/**
 * Define methods which are provided by service
 * Methods in this class are called by activity.
 */
public interface IPrivateLocalBindService {

    /**
     * Get sensitive information (strings)
     */
    public String getInfo();
}
