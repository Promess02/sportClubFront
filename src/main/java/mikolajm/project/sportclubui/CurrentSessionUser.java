package mikolajm.project.sportclubui;

import javafx.scene.image.Image;
import lombok.Getter;
import mikolaj.project.backendapp.enums.MembershipStatus;
import mikolaj.project.backendapp.model.Calendar;
import mikolaj.project.backendapp.model.*;
import mikolaj.project.backendapp.repo.*;
import mikolaj.project.backendapp.service.ActivityService;
import mikolaj.project.backendapp.service.CalendarService;
import mikolaj.project.backendapp.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@Getter
public class CurrentSessionUser {
    private User user;
    private Member member;
    private Trainer trainer;
    private Membership membership;
    private CreditCard creditCard;
    private List<Calendar> calendarList;
    private boolean membershipStatus = false;
    private final MembershipService membershipService;
    private final MemberRepo memberRepo;
    private final TrainerRepo trainerRepo;
    private final UserRepo userRepo;
    private final CalendarRepo calendarRepo;
    private final MembershipRepo membershipRepo;
    private final ActivityService activityService;
    private Map<Activity, Boolean> listOfActivities;
    private final CalendarService calendarService;

    @Autowired
    public CurrentSessionUser(MembershipService membershipService,
                              MemberRepo memberRepo,
                              TrainerRepo trainerRepo,
                              UserRepo userRepo, CalendarRepo calendarRepo, ActivityService activityService,
                              CalendarService calendarService, MembershipRepo membershipRepo) {
        this.membershipRepo = membershipRepo;
        this.calendarService = calendarService;
        this.membershipService = membershipService;
        this.memberRepo = memberRepo;
        this.trainerRepo = trainerRepo;
        this.userRepo = userRepo;
        this.calendarRepo = calendarRepo;
        this.activityService = activityService;
    }

    public List<Activity> getAlreadySignedList(){
        listOfActivities = new HashMap<>();
        List<Activity> listOfAllActivities = activityService.getAllActivities().getData().orElse(new ArrayList<>());
        List<Activity> listOfSignedActivities = new ArrayList<>();
        for(Activity activity: listOfAllActivities){
            if(!calendarRepo.findCalendarsByActivityAndMember(activity,member).isEmpty()){
                listOfActivities.put(activity,true);
                listOfSignedActivities.add(activity);
            }
            else listOfActivities.put(activity,false);
        }
        return listOfSignedActivities;
    }
    public void setUser(User user) {
        Optional<User> userDb = userRepo.findByEmailIgnoreCase(user.getEmail());
        if(userDb.isEmpty()) throw new RuntimeException("Couldn't find the user in Current Session user class");
        this.user = userDb.get();
        Optional<Member> memberOptional = memberRepo.findMemberByUser(userDb.get());
        member = memberOptional.orElse(null);
        Optional<Trainer> trainerOptional = trainerRepo.findTrainerByUser(userDb.get());
        trainer = trainerOptional.orElse(null);
        if(trainerOptional.isPresent()) member = null;
        if(memberOptional.isPresent()) trainer = null;
        Optional<Membership> membershipOptional = membershipService.getMembershipForUser(user.getEmail()).getData();
        if(membershipOptional.isPresent()){
            membership = membershipOptional.get();
            if(membership.getEndDate().isBefore(LocalDate.now())){
                Utils utils = new Utils();
                utils.showErrorMessage("your membership has expired");
                membership.setMembershipStatus(MembershipStatus.EXPIRED);
                membershipRepo.save(membership);
                membershipStatus = false;
            }
             else membershipStatus = true;
        }else{
            membership = null;
            membershipStatus = false;
        }
        creditCard = user.getCreditCard();

        if(memberOptional.isPresent() || trainerOptional.isPresent())
        loadCalendarList();
        else calendarList = new ArrayList<>();
    }

    public void updateUserImage(String dbUrl){
        user.setProfileImageUrl(dbUrl);
        userRepo.save(user);
    }

    public void loadCalendarList(){
            if(member!=null)
                calendarService.getEntriesForMember(member).getData().ifPresent(calendars -> calendarList=calendars);
            if(trainer!=null)
                calendarService.getEntriesForTrainer(trainer).getData().ifPresent(calendars -> calendarList=calendars);
    }

    public void logout(){
        user = null;
        member = null;
        trainer = null;
        membership = null;
        creditCard = null;
        calendarList = null;
        membershipStatus = false;
    }

    public void loadCreditCard(){
        creditCard = userRepo.findById(user.getId()).get().getCreditCard();
    }

    public void loadMembership(){
        Optional<Member> memberOptional = memberRepo.findMemberByUser(user);
        memberOptional.ifPresent(value -> member = value);
        Optional<Membership> membershipOptional = membershipService.getMembershipForUser(user.getEmail()).getData();
        membershipOptional.ifPresent(value -> {
            membership = value;
            if(membership.getEndDate().isBefore(LocalDate.now())){
                Utils utils = new Utils();
                utils.showErrorMessage("your membership has expired");
                membership.setMembershipStatus(MembershipStatus.EXPIRED);
                membershipRepo.save(membership);
                membershipStatus = false;
            }
            else membershipStatus = true;
        });
        if(memberOptional.isPresent())
            loadCalendarList();
        else calendarList = new ArrayList<>();
    }

}
