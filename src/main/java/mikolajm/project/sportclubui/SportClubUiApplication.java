package mikolajm.project.sportclubui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "mikolaj.project.backendapp")
public class SportClubUiApplication {

    public static void main(String[] args) {
        ClubApplication.main(args);
    }

}
