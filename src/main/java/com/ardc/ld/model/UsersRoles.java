package com.ardc.ld.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "system_user_role")
public class UsersRoles implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private UserRolId id;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Role role;

}
