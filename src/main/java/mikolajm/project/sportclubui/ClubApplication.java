package mikolajm.project.sportclubui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mikolajm.project.sportclubui.Util.LoginManager;
import mikolajm.project.sportclubui.screenController.generalScreens.EnterScreenController;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


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
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/loginRegister/EnterScreen.fxml"));
            Parent root = loader.load();
            EnterScreenController enterScreenController = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }catch (IOException exception){
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, exception);
        }

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
