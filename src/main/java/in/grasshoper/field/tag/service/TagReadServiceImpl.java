package in.grasshoper.field.tag.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TagReadServiceImpl implements TagReadService {
	private final JdbcTemplate jdbcTemplate;
	@Autowired
	private TagReadServiceImpl(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
