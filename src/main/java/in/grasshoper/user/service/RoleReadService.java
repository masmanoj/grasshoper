package in.grasshoper.user.service;

import in.grasshoper.user.data.RoleData;

import java.util.Collection;

public interface RoleReadService {

	Collection<RoleData> retrieveUserRoles(Long userId);

}
