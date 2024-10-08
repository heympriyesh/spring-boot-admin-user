package com.usermanagement.repo;

import com.usermanagement.enitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    UserEntity findByResetPasswordToken(String token);

    @Query(value = "Select * from user_db u where u.category_id In (select id from category where id=?1)", nativeQuery = true)
    List<UserEntity> getUserListByCategoryId(int categoryId);

    @Modifying
    @Query("DELETE FROM userDb u WHERE u.email=:email")
    void deleteUserByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM userDb u WHERE u.id=:id")
    void deleteUserById(@Param("id") int id);

    boolean existsByEmail(String email);

    boolean existsById(int id);

    @Modifying
    @Query(value = "update user_db u set u.role= :roleName where u.id= :id", nativeQuery = true)
    void updateUserRole(@Param("roleName") String roleName,
                        @Param("id") int id);

    @Query(value = "Select * from user_db u where u.id=:id", nativeQuery = true)
    Optional<UserEntity> getUserById(@Param("id") int id);
}
