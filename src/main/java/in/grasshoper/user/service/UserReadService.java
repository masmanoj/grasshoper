package in.grasshoper.user.service;

import in.grasshoper.user.data.UserData;

import java.util.Collection;

public interface UserReadService {

	Collection<UserData> retriveAll(Integer limit, Integer offset);

	UserData retriveOne(Long userId);

	Collection<UserData> retriveAllActivePrivateUsers();

}
