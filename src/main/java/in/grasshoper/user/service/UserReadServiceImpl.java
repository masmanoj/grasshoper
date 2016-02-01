package in.grasshoper.user.service;

import in.grasshoper.user.data.RoleData;
import in.grasshoper.user.data.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class UserReadServiceImpl implements UserReadService {
	private final JdbcTemplate jdbcTemplate;
	private final RoleReadService roleReadService;
	private final UserRowMapper userRowMapper;
	@Autowired
	private UserReadServiceImpl(final DataSource dataSource,
			final RoleReadService roleReadService) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.roleReadService = roleReadService;
		this.userRowMapper = new UserRowMapper(this.roleReadService);
	}
	
	@Override
	public Collection<UserData> retriveAll(Integer limit, Integer offset){
		String sql = "select " + this.userRowMapper.schema() ;
		
		if(limit != null){
			if(offset !=null){
				sql += "limit "+offset+", "+limit;
			}else
				sql += "limit "+limit;
		}
			
		return this.jdbcTemplate.query(sql, this.userRowMapper);
	}
	
	@Override
	public UserData retriveOne(Long userId){
		String sql = "select " + this.userRowMapper.schema() ;
		sql += " where id = ? ";	
		return this.jdbcTemplate.queryForObject(sql, this.userRowMapper, userId);
	}
	
	@Override
	public Collection<UserData> retriveAllActivePrivateUsers(){
		String sql = "select " + this.userRowMapper.schema() ;
		sql += " where  is_active = true and is_public_user = false";	
		return this.jdbcTemplate.query(sql, this.userRowMapper);
	}
	
	
	protected static final class UserRowMapper implements RowMapper<UserData> {
		private final RoleReadService roleReadService;
		public UserRowMapper(final RoleReadService roleReadService) {
			this.roleReadService = roleReadService;
		}
        @Override
        public UserData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

            final Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final String email = rs.getString("email");
            final String phoneNum = rs.getString("phone_num");
            final String imageUrl = rs.getString("image_url");
            final Boolean isActive = rs.getBoolean("is_active");
            final Boolean isPasswordChangeNeeded = rs.getBoolean("is_password_change_needed_on_next_login");
            final Boolean isPublicUser = rs.getBoolean("is_public_user");
            
            final Collection<RoleData> roles = this.roleReadService.retrieveUserRoles(id);
            
            return UserData.createNew(id, name, email, phoneNum, imageUrl, isActive, 
            		isPasswordChangeNeeded, isPublicUser, roles);
        }

        public String schema() {
            return " id, name, email, phone_num,image_url, is_active,"+
            		"is_password_change_needed_on_next_login,is_public_user from g_user ";
        }
    }
}
