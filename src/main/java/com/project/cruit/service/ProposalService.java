package com.project.cruit.service;

import com.project.cruit.domain.User;
import com.project.cruit.domain.proposal.Proposal;
import com.project.cruit.domain.proposal.TopProposal;
import com.project.cruit.domain.status.PartStatus;
import com.project.cruit.domain.status.ProposalStatus;
import com.project.cruit.exception.AlreadyClosedPartException;
import com.project.cruit.exception.NotPermitException;
import com.project.cruit.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final MailService mailService;

    /* 제안을 생성하면서 notification도 생성 */
    @Transactional
    public void saveAndMakeNotification(Proposal proposal, String message) throws MessagingException {
        proposalRepository.save(proposal);

        notificationService.createProposalNotification(proposal, proposal.getReceiver(), message);

        // 제안을 받는 사람에게 제안이 왔다고 메일 보내기
        mailService.sendProposalMail(proposal.getReceiver().getEmail(), message);
    }

    @Transactional
    public void setAcceptedAndAddToMember(Proposal proposal) {
        proposal.setStatus(ProposalStatus.ACCEPTED);

        // 해당 파트의 모집이 마감된 상태면 승인 눌러도 멤버로 추가되지 않게 하기
        if (proposal.getPart().getStatus() == PartStatus.COMPLETED) {
            throw new AlreadyClosedPartException();
        }

        // notification 새로 생성
        if (proposal instanceof TopProposal) {
            // 프로젝트에 참여하고 싶다는 요청 보낸 거였으면
            // part에 멤버로 추가
            proposal.getPart().addMemberWithIsLeader(proposal.getSender(), proposal.getIsLeaderProposal());
//                    .getUserParts().add(new UserPart(proposal.getSender(), proposal.getPart(), proposal.getIsLeaderProposal()));
        } else {
            // 프로젝트에 참여해달라는 요청 보낸 거였으면
            // part에 멤버로 추가
            proposal.getPart().addMemberWithIsLeader(proposal.getReceiver(), proposal.getIsLeaderProposal());
//                    .getUserParts().add(new UserPart(proposal.getReceiver(), proposal.getPart(), proposal.getIsLeaderProposal()));
        }
        // 제안을 보냈던 사람에게 승인됐다고 알림 보내기
        notificationService.createProposalNotification(proposal, proposal.getSender(), "'" + proposal.getReceiver().getName() + "'님이 프로젝트 제안을 승낙했습니다");
    }

    @Transactional
    public void setRefused(Proposal proposal) {
        proposal.setStatus(ProposalStatus.REFUSED);
        // 제안을 보냈던 사람에게 거절됐다고 앎림 보내기
        notificationService.createProposalNotification(proposal, proposal.getSender(), "'" + proposal.getReceiver().getName() + "'님이 프로젝트 제안을 거절했습니다");
    }

    public Proposal findById(Long id) {
        return proposalRepository.findById(id).get();
    }

    public List<Proposal> getSentProposalsOfUser(Long userId) {
        User user = userService.findById(userId);
        return proposalRepository.findAllBySender(user);
    }

    public List<Proposal> getReceivedProposalsOfUser(Long userId) {
        User user = userService.findById(userId);
        return proposalRepository.findAllByReceiver(user);
    }

    public void checkAuthorityToPropose(Long partId, Long proposerId) {
        // 유저에게 제안할 수 있는 권한이 있는가? 프로젝트 생성자 / 파트 리더
        Boolean hasAuthority = proposalRepository.isAvailableToProposeToUser(partId, proposerId) > 0L;
        if (!hasAuthority) {
            throw new NotPermitException("프로젝트 제안자 또는 파트 리더만 가능합니다");
        }
    }
}
