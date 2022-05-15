package com.project.cruit.repository;

import com.project.cruit.domain.User;
import com.project.cruit.domain.proposal.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findAllBySender(User user);

    List<Proposal> findAllByReceiver(User user);

    // 유저에게 제안할 수 있는 권한이 있는가? 프로젝트 생성자 / 파트 리더
    @Query("select count(*) " +
            "from Part pa left join pa.userParts up join pa.project pr " +
            "where " +
            "pa.id = :partId and (up.isLeader = true or pr.proposer.id = :proposerId)")
    Long isAvailableToProposeToUser(@Param("partId")Long partId, @Param("proposerId")Long proposerId);
}
