package com.example.passwordrepository;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class PasswordRepository extends Application {

    static private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static private final double x = screenSize.getWidth();
    static private final double y = screenSize.getHeight() - 55;

    @Override
    public void start(Stage stage) {
        Encrypter encrypter = new Encrypter();
        createEntryStageAndWidgets(encrypter);
    }

    public static void main(String[] args) {
        launch();
    }

    private static void createEntryStageAndWidgets(Encrypter encrypter){
        Stage entryStage = new Stage();
        entryStage.setTitle("You are a virus, arent you?");

        Pane entryPane = new Pane();
        PasswordField passwordField = new PasswordField();

        Label wrongPasswordLabel = new Label();
        wrongPasswordLabel.setLayoutX(5);
        wrongPasswordLabel.setLayoutY(40);

        Button checkPasswordBtn = new Button();
        checkPasswordBtn.setText("Check Password");
        checkPasswordBtn.setLayoutX(220);
        checkPasswordBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if(getPublicKeyReEncryptFileAndPrintFile(passwordField, encrypter, wrongPasswordLabel, passwordField.getText())){
                        entryStage.close();
                    }
                } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {}
            }
        });

        entryPane.getChildren().add(passwordField);
        entryPane.getChildren().add(checkPasswordBtn);
        entryPane.getChildren().add(wrongPasswordLabel);

        Scene entryScene = new Scene(entryPane, 500, 300);
        entryScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)){
                    try {
                        if(getPublicKeyReEncryptFileAndPrintFile(passwordField, encrypter, wrongPasswordLabel, passwordField.getText())){
                            entryStage.close();
                        }
                    } catch (Exception e){}
                }
            }
        });

        entryStage.setScene(entryScene);
        entryStage.show();
    }

    private static Boolean getPublicKeyReEncryptFileAndPrintFile(PasswordField passwordField, Encrypter encrypter, Label wrongPasswordLabel, String password) throws UnrecoverableEntryException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PublicKey publicKey = null;
        try {
            publicKey = encrypter.getPublicKey(password);
        }
        catch (Exception e){}
        if (publicKey != null){
            System.out.println("Hello");
            byte[] encryptedBytes = encrypter.decryptFile(password);
            String encryptedText = "";
            if (encryptedBytes != null){
                encryptedText = new String(encryptedBytes);
            }
            createStageWithReEncryptedInformationFromFile(encryptedText, encrypter, password);
            return true;
        }
        else {
            System.out.println("EXCEPTION");
            wrongPasswordLabel.setText("Wrong password, try again");
            return false;
        }
    }
    private static void createStageWithReEncryptedInformationFromFile(String encryptedText, Encrypter encrypter, String password){

        Stage informationStage = new Stage();
        informationStage.setTitle("Ok, you arent a virus");

        Pane informationPane = new Pane();

        TextArea textArea = new TextArea();
        textArea.insertText(0, encryptedText);
        textArea.setPrefSize(x - 200, y - 100);

        Button encryptFileBtn = new Button();
        encryptFileBtn.setLayoutX(x - 100);
        encryptFileBtn.setLayoutY(100);
        encryptFileBtn.setText("Encrypt information");
        encryptFileBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    encrypter.encryptInformationAndWriteItToFile(password, textArea.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        informationPane.getChildren().add(textArea);
        informationPane.getChildren().add(encryptFileBtn);

        Scene informationScene = new Scene(informationPane, x, y);
        informationStage.setScene(informationScene);
        informationStage.show();
    }
}