package mikolajm.project.sportclubui;

import lombok.Getter;
import mikolaj.project.backendapp.model.Calendar;
import mikolaj.project.backendapp.model.*;
import mikolaj.project.backendapp.repo.CalendarRepo;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolaj.project.backendapp.repo.UserRepo;
import mikolaj.project.backendapp.service.ActivityService;
import mikolaj.project.backendapp.service.CalendarService;
import mikolaj.project.backendapp.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private final ActivityService activityService;
    private Map<Activity, Boolean> listOfActivities;
    private final CalendarService calendarService;

    @Autowired
    public CurrentSessionUser(MembershipService membershipService,
                              MemberRepo memberRepo,
                              TrainerRepo trainerRepo,
                              UserRepo userRepo, CalendarRepo calendarRepo, ActivityService activityService,
                              CalendarService calendarService) {
        this.calendarService = calendarService;
        this.membershipService = membershipService;
        this.memberRepo = memberRepo;
        this.trainerRepo = trainerRepo;
        this.userRepo = userRepo;
        this.calendarRepo = calendarRepo;
        this.activityService = activityService;
    }

    public void getAlreadySignedList(){
        listOfActivities = new HashMap<>();
        List<Activity> listOfAllActivities = activityService.getAllActivities().getData().orElse(new ArrayList<>());
        for(Activity activity: listOfAllActivities){
            if(!calendarRepo.findCalendarsByActivityAndMember(activity,member).isEmpty())
                listOfActivities.put(activity,true);
            else listOfActivities.put(activity,false);
        }
    }
    public void setUser(User user) {
        Optional<User> userDb = userRepo.findByEmailIgnoreCase(user.getEmail());
        if(userDb.isEmpty()) throw new RuntimeException("Couldn't find the user in Current Session user class");
        this.user = userDb.get();
        Optional<Member> memberOptional = memberRepo.findMemberByUser(userDb.get());
        memberOptional.ifPresent(value -> member = value);
        Optional<Trainer> trainerOptional = trainerRepo.findTrainerByUser(userDb.get());
        trainerOptional.ifPresent(value -> trainer = value);
        Optional<Membership> membershipOptional = membershipService.getMembershipForUser(user.getEmail()).getData();
        membershipOptional.ifPresent(value -> {
            membership=value;
            membershipStatus = true;
        });
        creditCard = user.getCreditCard();

        if(memberOptional.isPresent())
        loadCalendarList();
        else calendarList = new ArrayList<>();
    }

    public void loadCalendarList(){
            calendarService.getEntriesForMember(member).getData().ifPresent(calendars -> calendarList=calendars);
    }

    public void loadCreditCard(){
        creditCard = userRepo.findById(user.getId()).get().getCreditCard();
    }

    public void loadMembership(){
        Optional<Member> memberOptional = memberRepo.findMemberByUser(user);
        memberOptional.ifPresent(value -> member = value);
        Optional<Membership> membershipOptional = membershipService.getMembershipForUser(user.getEmail()).getData();
        membershipOptional.ifPresent(value -> {
            membership=value;
            membershipStatus = true;
        });
        if(memberOptional.isPresent())
            loadCalendarList();
        else calendarList = new ArrayList<>();
    }

}
