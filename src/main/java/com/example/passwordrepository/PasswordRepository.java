package com.example.passwordrepository;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

    static private TextArea textArea = new TextArea();
    static private TextField textFieldForSearching = new TextField();

    static private String needfulText = "";
    static private int textCount = 0;
    static private String copyOfDecryptedText;

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
                    if(getPublicKeyReEncryptFileAndPrintFile(encrypter, wrongPasswordLabel, passwordField.getText())){
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
                        if(getPublicKeyReEncryptFileAndPrintFile(encrypter, wrongPasswordLabel, passwordField.getText())){
                            entryStage.close();
                        }
                    } catch (Exception e){}
                }
            }
        });

        entryStage.setScene(entryScene);
        entryStage.show();
    }

    private static Boolean getPublicKeyReEncryptFileAndPrintFile(Encrypter encrypter, Label wrongPasswordLabel, String password) throws UnrecoverableEntryException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PublicKey publicKey = null;
        String decryptedText = "";
        try {
            publicKey = encrypter.getPublicKey(password);
        }
        catch (Exception e){}
        if (publicKey != null){
            byte[] decryptedBytes = encrypter.decryptFile(password);

            if (decryptedBytes != null){
                decryptedText = new String(decryptedBytes);
            }
            copyOfDecryptedText = decryptedText;
            createStageWithDecryptedInformationFromFile(decryptedText, encrypter, password);
            return true;
        }
        else {
            System.out.println("EXCEPTION");
            wrongPasswordLabel.setText("Wrong password, try again");
            return false;
        }
    }
    private static void createStageWithDecryptedInformationFromFile(String decryptedText, Encrypter encrypter, String password){
        Stage informationStage = new Stage();
        informationStage.setTitle("Ok, you arent a virus");

        Pane informationPane = new Pane();
        Scene informationScene = new Scene(informationPane, x, y);

        textArea.insertText(0, decryptedText);
        textArea.setPrefSize(x - 200, y - 100);
        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.SHIFT)){
                    textFieldForSearching.requestFocus();
                }
            }
        });
        Label mistakeLabel = new Label();
        mistakeLabel.setLayoutX(x - 200);
        mistakeLabel.setLayoutY(160);

        textFieldForSearching.setLayoutX(x - 200);
        textFieldForSearching.setLayoutY(100);
        textFieldForSearching.setPrefSize(100, 50);
        textFieldForSearching.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.ENTER)){
                    try {
                        search(textFieldForSearching.getText(), textArea.getText());
                        mistakeLabel.setText("");
                    } catch (Exception e){
                        mistakeLabel.setText("There a no such word");
                    }
                }
            }
        });
        informationScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.SHIFT)){

                    System.out.println("HI");
                }
            }
        });

        informationPane.getChildren().add(textArea);
        informationPane.getChildren().add(textFieldForSearching);
        informationPane.getChildren().add(mistakeLabel);


        informationStage.setScene(informationScene);
        informationStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    encrypter.encryptInformationAndWriteItToFile(password, textArea.getText());
                } catch (Exception e) {}
            }
        });
        informationStage.show();
    }
    private static void search(String text, String decryptedText){
        textArea.requestFocus();
        if (needfulText.equals(text)){
            copyOfDecryptedText = copyOfDecryptedText.substring(0, copyOfDecryptedText.indexOf(text)) + copyOfDecryptedText.substring(copyOfDecryptedText.indexOf(text) + text.length());
            textCount++;
        }
        else {
            textCount = 0;
        }
        textArea.positionCaret(copyOfDecryptedText.indexOf(text) + textCount * text.length());
        needfulText = text;
    }
}