package in.grasshoper.user.service;

import in.grasshoper.core.security.exception.PlatformUnknownDBException;
import in.grasshoper.user.data.RoleData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;



@Service
public class RoleReadServiceImpl implements RoleReadService {
	private final JdbcTemplate jdbcTemplate;
	private final RoleMapper roleRowMapper;
	@Autowired
	private RoleReadServiceImpl(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.roleRowMapper = new RoleMapper();
	}

	@Override
    public Collection<RoleData> retrieveUserRoles(final Long userId) {
        try {
            final String sql = "select " + this.roleRowMapper.schema() + " inner join g_user_role"
                    + " ar on ar.role_id = r.id where ar.user_id= ?";

            return this.jdbcTemplate.query(sql, this.roleRowMapper, new Object[] { userId });
        } catch (DataAccessException exception){
        	exception.printStackTrace();
            throw new PlatformUnknownDBException();
        }
    }
	
	 protected static final class RoleMapper implements RowMapper<RoleData> {

        @Override
        public RoleData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

            final Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final String description = rs.getString("description");
            final Long parentId = rs.getLong("parent");
            final String parentName = rs.getString("parentName");
            final Collection<RoleData> parentRoles = null;
            return new RoleData(id, name, description, parentId, parentName, parentRoles);
        }

        public String schema() {
            return " r.id as id, r.name as name, r.description as description, r.parent_id as parent, parent.name as parentName "
                    + "from g_role r LEFT JOIN g_role AS parent ON parent.id = r.parent_id ";
        }
    }

}
