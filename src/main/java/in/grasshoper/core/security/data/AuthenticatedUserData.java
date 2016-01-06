/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package in.grasshoper.core.security.data;


/**
 * Immutable data object for authentication.
 */
public class AuthenticatedUserData {

    @SuppressWarnings("unused")
    private final String name;
    @SuppressWarnings("unused")
    private final Long userId;
    @SuppressWarnings("unused")
    private final String sessionKey;
    @SuppressWarnings("unused")
    private final boolean authenticated;
    @SuppressWarnings("unused")
    private final boolean shouldRenewPassword;
    @SuppressWarnings("unused")
    private final String email;

    public AuthenticatedUserData(final String name, final Long userId, 
    		final String base64EncodedAuthenticationKey, 
    		final String email, final boolean shouldRenewPassword,
    		final boolean authenticated) {
        this.name = name;
        this.userId = userId;
        this.sessionKey = base64EncodedAuthenticationKey;
        this.authenticated = authenticated;
        this.shouldRenewPassword = shouldRenewPassword;
        this.email = email;
    }
    
    public AuthenticatedUserData(final String name,  
    		final String base64EncodedAuthenticationKey, 
    		final String email, final boolean shouldRenewPassword,
    		final boolean authenticated) {
        this.name = name;
        this.userId = null;
        this.sessionKey = base64EncodedAuthenticationKey;
        this.authenticated = authenticated;
        this.shouldRenewPassword = shouldRenewPassword;
        this.email = email;
    }
}