package com.project.cruit.repository;

import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPartRepository extends JpaRepository<UserPart, Long> {
    long countByPartAndIsLeader(Part part, boolean isLeader);
    UserPart findByPartAndUser(Part part, User member);

    User findByPartAndIsLeader(Part part, boolean isLeader);

    List<UserPart> findAllByUser(User user);
}
