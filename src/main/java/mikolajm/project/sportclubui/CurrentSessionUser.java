package mikolajm.project.sportclubui;

import lombok.Getter;
import lombok.Setter;
import mikolaj.project.backendapp.DTO.ServiceResponse;
import mikolaj.project.backendapp.model.*;
import mikolaj.project.backendapp.repo.CreditCardRepo;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolaj.project.backendapp.repo.UserRepo;
import mikolaj.project.backendapp.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Getter
public class CurrentSessionUser {
    private User user;
    private Member member;
    private Trainer trainer;
    private Membership membership;
    private CreditCard creditCard;
    private boolean membershipStatus = false;
    private final MembershipService membershipService;
    private final MemberRepo memberRepo;
    private final TrainerRepo trainerRepo;
    private final UserRepo userRepo;

    @Autowired
    public CurrentSessionUser(MembershipService membershipService,
                              MemberRepo memberRepo,
                              TrainerRepo trainerRepo,
                              UserRepo userRepo) {
        this.membershipService = membershipService;
        this.memberRepo = memberRepo;
        this.trainerRepo = trainerRepo;
        this.userRepo = userRepo;
    }

    public void setUser(User user) {
        Optional<User> userDb = userRepo.findByEmailIgnoreCase(user.getEmail());
        if(userDb.isEmpty()) throw new RuntimeException("Couldn't find the user in Current Session user class");
        this.user = userDb.get();
        Optional<Member> memberOptional = memberRepo.findMemberByUserId(user.getId());
        memberOptional.ifPresent(value -> member = value);
        Optional<Trainer> trainerOptional = trainerRepo.findTrainerByUserId(user.getId());
        trainerOptional.ifPresent(value -> trainer = value);
        Optional<Membership> membershipOptional = membershipService.getMembershipForUser(user.getEmail()).getData();
        membershipOptional.ifPresent(value -> {
            membership=value;
            membershipStatus = true;
        });
        creditCard = user.getCreditCard();
    }

}
