package org.example.pichetest.repository;

import java.util.Optional;
import org.example.pichetest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    <S extends User> S save(S entity);

    @Override
    Optional<User> findById(Long aLong);

    Optional<User> findByEmail(String email);
}
