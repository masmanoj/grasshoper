package in.grasshoper.user.domain;

import in.grasshoper.core.exception.GeneralPlatformRuleException;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.user.data.RoleData;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }, name = "unq_name") })
public class Role extends AbstractPersistable<Long> {

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Role parent;
    
    @Column(name = "hierarchy", nullable = false, length = 100)
    private String hierarchy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "g_role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private final Set<Permission> permissions = new HashSet<>();

    public static Role fromJson(final JsonCommand command, final Role parentRole) {
        final String name = command.stringValueOfParameterNamed("name");
        final String description = command.stringValueOfParameterNamed("description");
        return new Role(name, description, parentRole);
    }

    protected Role() {
        //
    }

    public Role(final String name, final String description, final Role parentRole) {
        this.name = name.trim();
        this.description = description.trim();
        this.parent = parentRole;
    }

    public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(7);

        final String nameParamName = "name";
        
        final String parentIdParamName = "parentId";

        if (this.parent != null && command.isChangeInLongParameterNamed(parentIdParamName, this.parent.getId())) {
            final Long newValue = command.longValueOfParameterNamed(parentIdParamName);
            if(this.getId() == newValue){
            	throw new GeneralPlatformRuleException("error.msg.role.cannot.be.parrent.to.iself ",  "Role cannot be parent to itself" + this.name, this.name);
            }
            actualChanges.put(parentIdParamName, newValue);
        }else{
        	final Long newValue = command.longValueOfParameterNamed(parentIdParamName);
        	actualChanges.put(parentIdParamName, newValue);
        }
        
        if (command.isChangeInStringParameterNamed(nameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(nameParamName);
            actualChanges.put(nameParamName, newValue);
            this.name = newValue;
        }

        final String descriptionParamName = "description";
        if (command.isChangeInStringParameterNamed(descriptionParamName, this.description)) {
            final String newValue = command.stringValueOfParameterNamed(descriptionParamName);
            actualChanges.put(descriptionParamName, newValue);
            this.description = newValue;
        }

        return actualChanges;
    }

    public boolean updatePermission(final Permission permission, final boolean isSelected) {
        boolean changed = false;
        if (isSelected) {
            changed = addPermission(permission);
        } else {
            changed = removePermission(permission);
        }

        return changed;
    }

    private boolean addPermission(final Permission permission) {
        return this.permissions.add(permission);
    }

    private boolean removePermission(final Permission permission) {
        return this.permissions.remove(permission);
    }

    public Collection<Permission> getPermissions() {
        return this.permissions;
    }

    public boolean hasPermissionTo(final String permissionCode) {
        boolean match = false;
        for (final Permission permission : this.permissions) {
            if (permission.hasCode(permissionCode)) {
                match = true;
                break;
            }
        }
        return match;
    }

    public void update(final Role newParent) {

        this.parent = newParent;
        generateHierarchy();
    }

    
    public RoleData toData() {
        return new RoleData(getId(), this.name, this.description, null, null, null);
    }
    
    public Boolean hasParent() {
        if (this.parent != null) return true;
        return false;
    }

    
    public Role getParent() {
        return this.parent;
    }

    
    public String getName() {
        return this.name;
    }
    
    public void generateHierarchy() {

        if (this.parent != null) {
            this.hierarchy = this.parent.hierarchyOf(getId());
        } else {
            this.hierarchy = ".";
        }
    }
    
    private String hierarchyOf(final Long id) {
        return this.hierarchy + id.toString() + ".";
    }
    public String gethierarchy(){
    	return this.hierarchy;
    }
}