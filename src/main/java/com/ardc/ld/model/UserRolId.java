package com.ardc.ld.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UserRolId implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "user_id")
    private long userId;
    @Basic(optional = false)
    @Column(name = "role_id")
    private long roleId;

    public UserRolId() {
    }

    public UserRolId(long userId, long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
