package com.ardc.ld.repository;

import com.ardc.ld.model.User;
import com.ardc.ld.model.UserRolId;
import com.ardc.ld.model.UsersRoles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRolesRepository extends CrudRepository<UsersRoles, UserRolId> {
    @Query("select o from UsersRoles o where o.user = ?1")
    List<UsersRoles> findUserRolesByUserId(User user);
}
