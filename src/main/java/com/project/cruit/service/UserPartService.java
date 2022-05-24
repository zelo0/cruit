package com.project.cruit.service;

import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.repository.UserPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPartService {
    private final UserPartRepository userPartRepository;

    @Transactional
    public void saveUserPart(User user, Part part) {
        userPartRepository.save(new UserPart(user, part));
    }

    public Boolean hasPartLeader(Part part) {
        return userPartRepository.countByPartAndIsLeader(part, true) > 0;
    }

    public UserPart findByPartAndUser(Part part, User member) {
        return userPartRepository.findByPartAndUser(part, member);
    }

    public User findLeaderOfPart(Part part) {
        return userPartRepository.findByPartAndIsLeader(part, true);
    }

    public List<UserPart> findAllByUser(User user) {
        return userPartRepository.findAllByUser(user);
    }

    public UserPart findLeaderUserPartByPartId(Long partId) {
        return userPartRepository.findLeaderUserPartByPartId(partId);
    }

    public UserPart findByPartIdAndUserId(Long partId, Long userId) {
        return userPartRepository.findByPartIdAndUserId(partId, userId);
    }
}
