package in.grasshoper.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A custom copy of {@link AbstractAuditable} to override the column names used
 * on database.
 * 
 * Abstract base class for auditable entities. Stores the audition values in
 * persistent fields.
 * 
 * @param <U>
 *            the auditing type. Typically some kind of user.
 * @param <PK>
 *            the type of the auditing type's identifier
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractAuditableCustom<U, PK extends Serializable> extends AbstractPersistable<PK> implements Auditable<U, PK> {
	
    private static final long serialVersionUID = 141481953116476081L;

    @OneToOne
    @JoinColumn(name = "created_user_id")
    private U createdBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne
    @JoinColumn(name = "lastmodifiedby_id")
    private U lastModifiedBy;

    @Column(name = "lastmodified_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Auditable#getCreatedBy()
     */
    @Override
    public U getCreatedBy() {

        return this.createdBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.domain.Auditable#setCreatedBy(java.lang.Object)
     */
    @Override
    public void setCreatedBy(final U createdBy) {

        this.createdBy = createdBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Auditable#getCreatedDate()
     */
    @Override
    public DateTime getCreatedDate() {

        return null == this.createdDate ? null : new DateTime(this.createdDate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.domain.Auditable#setCreatedDate(org.joda.time
     * .DateTime)
     */
    @Override
    public void setCreatedDate(final DateTime createdDate) {

        this.createdDate = null == createdDate ? null : createdDate.toDate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Auditable#getLastModifiedBy()
     */
    @Override
    public U getLastModifiedBy() {

        return this.lastModifiedBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.domain.Auditable#setLastModifiedBy(java.lang
     * .Object)
     */
    @Override
    public void setLastModifiedBy(final U lastModifiedBy) {

        this.lastModifiedBy = lastModifiedBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Auditable#getLastModifiedDate()
     */
    @Override
    public DateTime getLastModifiedDate() {

        return null == this.lastModifiedDate ? null : new DateTime(this.lastModifiedDate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.domain.Auditable#setLastModifiedDate(org.joda
     * .time.DateTime)
     */
    @Override
    public void setLastModifiedDate(final DateTime lastModifiedDate) {

        this.lastModifiedDate = null == lastModifiedDate ? null : lastModifiedDate.toDate();
    }
}
