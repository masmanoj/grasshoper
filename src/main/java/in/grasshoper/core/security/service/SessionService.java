package in.grasshoper.core.security.service;


import static in.grasshoper.core.security.SecurityConstants.AuthTimeOut;
import in.grasshoper.core.security.data.SessionData;
import in.grasshoper.user.domain.User;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private Map<User, SessionData> appUserToSessionDataMap = new HashMap<>();
    private Map<String, SessionData> tokentoSessionDataMap = new HashMap<>();
    private final static Logger logger = LoggerFactory.getLogger("analytics");
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    

    public synchronized void createSession(User user, String token) {
        SessionData existingSession = appUserToSessionDataMap.get(user);
        if (existingSession != null) {
            String existingToken = existingSession.getToken();
//            logger.info(sf.format(DateUtils.getCurrentDateTimeOfTenant().toDate()) + "  Deleting Old Session for User : " + user.getUsername() + " with token : " + existingToken);
          //  appUserToSessionDataMap.remove(user);
           // tokentoSessionDataMap.remove(existingToken);
        }
//        logger.info(sf.format(DateUtils.getCurrentDateTimeOfTenant().toDate()) + "  Creating new token for User : " + user.getUsername() + " with token : " + token);
        SessionData newSession = new SessionData(user, token, DateTime.now());
        appUserToSessionDataMap.put(user, newSession);
        tokentoSessionDataMap.put(token, newSession);
    }

    public synchronized User retrieveUserFromToken(String token) {
        User result = null;
        Integer tokenValidForSeconds = AuthTimeOut;
        SessionData existingSession = tokentoSessionDataMap.get(token);
        if (existingSession != null) {
            DateTime tokenValidUntil = existingSession.getLastUsedDate().plusSeconds(tokenValidForSeconds);
            DateTime now = DateTime.now();
            if (tokenValidUntil.isAfter(now)) {
                existingSession.setLastUsedDate(now);
                result = existingSession.getUser();
            }
        }
        if (result == null) {
//            logger.info(sf.format(DateUtils.getCurrentDateTimeOfTenant().toDate()) + "  Unauthorized access with token : " + token);
        }
        else {
//            logger.info(sf.format(DateUtils.getCurrentDateTimeOfTenant().toDate()) + "  Authorized access for user : " + result.getUsername() + " with token : " + token);
        }
        return result;
    }

    public void deleteSession(String token) {
        SessionData existingSession = tokentoSessionDataMap.get(token);
        if (existingSession != null) {
//            logger.info(sf.format(DateUtils.getCurrentDateTimeOfTenant().toDate()) + "  Logging out user : " + existingSession.getUser().getUsername() + " with token : " + token);
            tokentoSessionDataMap.remove(token);
            appUserToSessionDataMap.remove(existingSession.getUser());
        }
    }

   
}
