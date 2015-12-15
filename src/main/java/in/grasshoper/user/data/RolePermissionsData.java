package in.grasshoper.user.data;

import java.util.Collection;

public class RolePermissionsData {

    @SuppressWarnings("unused")
    private final Long id;
    @SuppressWarnings("unused")
    private final String name;
    @SuppressWarnings("unused")
    private final String description;
    @SuppressWarnings("unused")
    private final Long parentId;
    @SuppressWarnings("unused")
    private final String parentName;
    @SuppressWarnings("unused")
    private final Collection<RoleData> allowedParents;
    
    @SuppressWarnings("unused")
    private final Collection<PermissionData> permissionUsageData;

    public RolePermissionsData(final Long id, final String name, final String description, final Long parentId, final String parentName,
            final Collection<PermissionData> permissionUsageData, final Collection<RoleData> allowedParents) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissionUsageData = permissionUsageData;
        this.allowedParents = allowedParents;
        this.parentId = parentId;
        this.parentName = parentName;
    }
}