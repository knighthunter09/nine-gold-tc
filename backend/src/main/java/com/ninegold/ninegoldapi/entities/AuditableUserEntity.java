package com.ninegold.ninegoldapi.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * The auditable user entity.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class AuditableUserEntity extends AuditableEntity {
    /**
     * The created by.
     */
    @Column(name = "created_by", insertable = true, updatable = false)
    private long createdBy;

    /**
     * The last modified by.
     */
    @Column(name = "last_modified_by")
    private long lastModifiedBy;
}

