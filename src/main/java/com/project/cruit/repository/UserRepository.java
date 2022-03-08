package com.project.cruit.repository;

import com.project.cruit.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);

    User findByEmail(String email);

    /* time check 필요 - Is there any more efficient way? */
    @Query("select u from User u join u.userStacks us join us.stack s where s.name in :stackFilterList")
    Page<User> findByStackFilter(@Param("stackFilterList") List<String> stackFilterList, Pageable pageable);

    Page<User> findByCanBeLeader(boolean leaderFilter, Pageable pageable);

    @Query("select u from User u join u.userStacks us join us.stack s where s.name in :stackFilterList and u.canBeLeader = :leaderFilter")
    Page<User> findByStackFilterAndCanBeLeader(@Param("stackFilterList") List<String> stackFilterList, @Param("leaderFilter") boolean leaderFilter, Pageable pageable);

    User findByName(String name);
}
