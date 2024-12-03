package com.Pavel.passwordrepository;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.ParseException;

public class PasswordRepository extends Application {

    private Screen screen = Screen.getPrimary();
    private double X = screen.getVisualBounds().getWidth();
    private double Y = screen.getVisualBounds().getHeight();

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
        Stage passwordRepositoryStage = loader.load();
        passwordRepositoryStage.show();
    }
}