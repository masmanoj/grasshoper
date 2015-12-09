package in.grasshoper.core.security.exception;

import in.grasshoper.core.data.ApiParameterError;
import in.grasshoper.core.exception.PlatformApiDataValidationException;

import java.util.ArrayList;

/**
 * A {@link RuntimeException} that is thrown in the case where a user does not
 * have sufficient authorization to execute operation on platform.
 */
public class ResetPasswordException extends PlatformApiDataValidationException {

    public ResetPasswordException(final Long userId) {

        super("error.msg.password.outdated", "The password of the user with id " + userId + " has expired, please reset it",
                new ArrayList<ApiParameterError>() {

                    {
                        add(ApiParameterError.parameterError("error.msg.password.outdated", "The password of the user with id " + userId
                                + " has expired, please reset it", "userId", userId));

                    }
                }

        );

    }

}