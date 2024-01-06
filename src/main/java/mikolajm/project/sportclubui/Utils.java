package mikolajm.project.sportclubui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mikolajm.project.sportclubui.screenController.ErrorMessageController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public String imagesPath = "/home/mikolajmichalczyk/IdeaProjects/sportClub/sportClubUi/src/main/resources/images/";

    public void showErrorMessage(String error){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/errorMsg.fxml"));
            Parent root = loader.load();
            ErrorMessageController errorMessageController = loader.getController();
            errorMessageController.setError(error);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }catch (IOException exception){
            throw new RuntimeException("failed loading error view");
        }
    }

    public void saveImage(Path sourcePath, String destinationPath) {
        try {
            Files.copy(sourcePath, Path.of(destinationPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public LocalTime formatStringToTime(String timeString){
        if (!isValidTimeFormat(timeString)) {
            throw new IllegalArgumentException("Invalid time format. Use HH:mm format.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(timeString, formatter);
    }
    public boolean isValidTimeFormat(String timeString){
        Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
        Matcher matcher = TIME_PATTERN.matcher(timeString);
        return matcher.matches();
    }

    public boolean isInteger(String string){
        return string.matches("\\d+");
    }

    public boolean isDouble(String string){
        return string.matches("\\d+\\.\\d+");
    }


}
