package in.grasshoper.user.data;

import java.io.Serializable;
import java.util.Collection;

/**
 * Immutable data object for role data.
 */
public class RoleData implements Serializable {

    private final Long id;
    private final String name;
    private final String description;
    private final Long parentId;
    private final String parentName;
    @SuppressWarnings("unused")
    private final Collection<RoleData> allowedParents;

    public RolePermissionsData toRolePermissionData(final Collection<PermissionData> permissionUsageData) {
        return new RolePermissionsData(this.id, this.name, this.description, parentId, parentName, permissionUsageData, allowedParents);
    }

	public RoleData(final Long id, final String name, final String description,
			final Long parentId, final String parentName,
			final Collection<RoleData> allowedParents) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
		this.parentName = parentName;
		this.allowedParents = allowedParents;
	}

    public static RoleData template(){
    	final Collection<RoleData> allowedParents = null;
    	return new RoleData(null, null, null, null, null, allowedParents);
    }
    
    public static RoleData appendedTemplate(final RoleData role, final Collection<RoleData> allowedParents) {
        return new RoleData(role.id, role.name, role.description, role.parentId, role.parentName, allowedParents);
    }
    
    public boolean hasIdentifyOf(final Long roleId) {
        return this.id.equals(roleId);
    }

    @Override
    public boolean equals(final Object obj) {
        final RoleData role = (RoleData) obj;
        return this.id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public String getName() {
        return this.name;
    }
    
    public String getParentName() {
    	return this.parentName;
    }
    
    
    public Long getId() {
        return this.id;
    }

    public boolean hasParent() {
        if (this.parentId == null) { return false; }
        return true;
    }

}
