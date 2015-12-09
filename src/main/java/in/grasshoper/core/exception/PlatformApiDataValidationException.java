/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package in.grasshoper.core.exception;

import in.grasshoper.core.data.ApiParameterError;

import java.util.List;

/**
 * Exception thrown when problem with an API request to the platform.
 */
public class PlatformApiDataValidationException extends RuntimeException {

    private final String globalisationMessageCode;
    private final String defaultUserMessage;
    private final List<ApiParameterError> errors;
    public static final String DEFAULT_MESSAGE_CODE = "validation.msg.validation.errors.exist";
    public static final String DEFAULT_USER_MESSAGE = "Validation errors exist.";

    public PlatformApiDataValidationException(final List<ApiParameterError> errors) {
        this.globalisationMessageCode = "validation.msg.validation.errors.exist";
        this.defaultUserMessage = "Validation errors exist.";
        this.errors = errors;
    }

    public PlatformApiDataValidationException(final String globalisationMessageCode, final String defaultUserMessage,
            final List<ApiParameterError> errors) {
        this.globalisationMessageCode = globalisationMessageCode;
        this.defaultUserMessage = defaultUserMessage;
        this.errors = errors;
    }

    public String getGlobalisationMessageCode() {
        return this.globalisationMessageCode;
    }

    public String getDefaultUserMessage() {
        return this.defaultUserMessage;
    }

    public List<ApiParameterError> getErrors() {
        return this.errors;
    }
}