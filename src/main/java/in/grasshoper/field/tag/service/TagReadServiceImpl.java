package in.grasshoper.field.tag.service;

import in.grasshoper.field.tag.data.SubTagData;
import in.grasshoper.field.tag.data.TagData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class TagReadServiceImpl implements TagReadService {
	private final JdbcTemplate jdbcTemplate;
	@Autowired
	private TagReadServiceImpl(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Collection<TagData> retriveAllTags(){
		final TagRowMapper rowMapper = new TagRowMapper(null);
		final String sql = "select " + rowMapper.schema() +  " where is_internal= false " ;
		return this.jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public TagData retriveOneTag(final Long tagId){
		final TagRowMapper rowMapper = new TagRowMapper(retriveAllSubTagsForTag(tagId));
		final String sql = "select " + rowMapper.schema() + " where id = ?" ;
		return this.jdbcTemplate.queryForObject(sql, rowMapper, tagId);
	}
	
	private static final class TagRowMapper implements RowMapper<TagData>{
		private final Collection<SubTagData> subTags;
		
		public TagRowMapper(final Collection<SubTagData> subTags){
			this.subTags = subTags;
		}
		
		public String schema(){
			return " id, tag, label from g_tag ";
		}
		@Override
		public TagData mapRow(final ResultSet rs,  @SuppressWarnings("unused") final int rowNum) throws SQLException {
			final Long id = rs.getLong("id");
			final String tag = rs.getString("tag");
			final String label = rs.getString("label");
			return TagData.createNew(id, tag, label, this.subTags);
		}	
	}
	
	
	

	@Override
	public Collection<SubTagData> retriveAllSubTagsForTag(Long tagId) {
		final SubTagRowMapper rowMapper = new SubTagRowMapper();
		final String sql = "select " + rowMapper.schema() + " where t.id = ? order by st.display_order" ;
		return this.jdbcTemplate.query(sql, rowMapper, tagId);
	}
	
	@Override
	public Collection<SubTagData> retriveAllSubTagsForTag(final String tag) {
		final SubTagRowMapper rowMapper = new SubTagRowMapper();
		final String sql = "select " + rowMapper.schema() + " where t.tag = ? order by st.display_order" ;
		return this.jdbcTemplate.query(sql, rowMapper, tag);
	}

	@Override
	public SubTagData retriveOneSubTag(Long subTagId) {
		final SubTagRowMapper rowMapper = new SubTagRowMapper();
		final String sql = "select " + rowMapper.schema() + " where st.id = ?" ;
		return this.jdbcTemplate.queryForObject(sql, rowMapper, subTagId);
	}
	
	@Override
	public SubTagData retriveOneSubTag(String tag, String subTag) {
		final SubTagRowMapper rowMapper = new SubTagRowMapper();
		final String sql = "select " + rowMapper.schema() + " where t.tag = ? and st.sub_tag = ? " ;
		return this.jdbcTemplate.queryForObject(sql, rowMapper, tag, subTag);
	}
	
	@Override
	public SubTagData retriveOneInternalSubTagsForTagAndSubTagLabel(final String tag, final String subTagLabel) {
		final SubTagRowMapper rowMapper = new SubTagRowMapper();
		final String sql = "select " + rowMapper.schema() + " where t.tag = ? and st.label = ? " ;
		return this.jdbcTemplate.queryForObject(sql, rowMapper, tag, subTagLabel);
	}
	
	private static final class SubTagRowMapper implements RowMapper<SubTagData>{
		
		public String schema(){
			return new StringBuilder()
			.append(" t.id tagId, t.tag tag, t.label tagLabel, ")
			.append(" st.id subTagid, st.sub_tag subTag, st.label subTagLabel, ")
			.append(" st.display_order displayOrder ")

			.append(" from g_sub_tag st ")
			.append(" inner join g_tag t on t.id = st.tag_id ").toString();
		}
		@Override
		public SubTagData mapRow(final ResultSet rs,  @SuppressWarnings("unused") final int rowNum) throws SQLException {
			final Long tagId = rs.getLong("tagId");
			final String tag = rs.getString("tag");
			final String tagLabel = rs.getString("tagLabel");
			final Long id = rs.getLong("subTagid");
			final String subTag = rs.getString("subTag");
			final String subTagLabel = rs.getString("subTagLabel");
			final Integer displayOrder = rs.getInt("displayOrder");
			return SubTagData.createNew(id, tagId, subTag, subTagLabel,
					displayOrder, tag, tagLabel);
		}	
	}


}
