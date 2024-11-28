package com.Pavel.passwordrepository;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;

public class PasswordRepository extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        showEntryStage();
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void showEntryStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CheckUser.fxml"));
        Stage entryStage = loader.load();
        entryStage.show();
    }

    public void showPasswordRepository() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PasswordRepository.fxml"));
        Stage informationStage = loader.load();
        informationStage.show();
    }
}