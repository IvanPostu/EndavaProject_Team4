package com.webapp.sportmeetingpoint.persistance;

import com.webapp.sportmeetingpoint.domain.entities.UserSystem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSystemRepository extends CrudRepository<UserSystem, Long> {


  @Query("SELECT u FROM UserSystem u WHERE u.email = :email1")
  Optional<UserSystem> findByEmail(@Param("email1") final String email);

  @Query("SELECT u FROM UserSystem u WHERE u.password = :password1")
  Optional<UserSystem> findByPassword(@Param("password1") final String password);

  List<UserSystem> findAll();

  UserSystem findById(Integer id);


}
