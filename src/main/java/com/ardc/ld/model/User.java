package com.ardc.ld.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "system_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    protected Long userId;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private Boolean active;
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private User creator;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "system_user_role", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id",
                            nullable = false, updatable = false, insertable = false)})
    private List<Role> roles;
}
