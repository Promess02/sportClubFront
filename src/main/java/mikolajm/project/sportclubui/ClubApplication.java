package mikolajm.project.sportclubui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@Slf4j
public class ClubApplication extends Application {
    @Getter
    private static ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ClubApplication.class);
        builder.application().setWebApplicationType(WebApplicationType.NONE);
        applicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
        log.info("APP INITIALIZED");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new StackPane());
        LoginManager loginManager = new LoginManager(scene, applicationContext);
        loginManager.showLoginScreen();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Application");
        primaryStage.show();
        log.info("APP STARTED");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
        log.info("APP STOPPED");
    }

}
