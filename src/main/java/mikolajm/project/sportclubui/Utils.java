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

public class Utils {

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

}
