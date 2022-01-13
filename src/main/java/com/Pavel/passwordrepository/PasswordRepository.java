package com.Pavel.passwordrepository;

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
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Optional;

public class PasswordRepository extends Application {

    static private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static private final double x = screenSize.getWidth();
    static private final double y = screenSize.getHeight() - 55;

    static private TextArea textArea;
    static private TextField textFieldForSearching;

    static private String needfulText = "";
    static private int textCount = 0;
    static private String copyOfDecryptedText;
    static private Label mistakeLabel;

    @Override
    public void start(Stage stage) {
        textArea = new TextArea();
        textFieldForSearching = new TextField();
        mistakeLabel = new Label();
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
        passwordField.setLayoutY(25);

        Label entryPasswordLabel = new Label();
        entryPasswordLabel.setLayoutX(5);
        entryPasswordLabel.setText("Enter password");

        Label wrongPasswordLabel = new Label();
        wrongPasswordLabel.setLayoutX(5);
        wrongPasswordLabel.setLayoutY(65);

        Button checkPasswordBtn = new Button();
        checkPasswordBtn.setText("Check Password");
        checkPasswordBtn.setLayoutX(220);
        checkPasswordBtn.setLayoutY(25);
        checkPasswordBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if(getPublicKeyReEncryptFileAndPrintFile(encrypter, wrongPasswordLabel, passwordField.getText())){
                        entryStage.close();
                    }
                } catch (Exception ignored) {}
            }
        });

        entryPane.getChildren().add(entryPasswordLabel);
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
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        entryStage.setScene(entryScene);
        entryStage.show();
    }

    private static Boolean getPublicKeyReEncryptFileAndPrintFile(Encrypter encrypter, Label wrongPasswordLabel, String password) throws UnrecoverableEntryException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {
        PublicKey publicKey = null;
        String decryptedText = "";
        try {
            publicKey = encrypter.getPublicKey(password);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (publicKey != null){
            byte[] decryptedBytes = encrypter.decryptFile(password);

            if (decryptedBytes != null){
                decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
            }
            copyOfDecryptedText = decryptedText;
            System.out.println(decryptedText);
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

        textArea.insertText(0, decryptedText);
        textArea.setPrefSize(x - 150, y - 20);
        final KeyCombination keyCombinationCTRLF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombinationCTRLS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyCombinationCTRLF.match(keyEvent)){
                    textFieldForSearching.requestFocus();
                }
                else if (keyCombinationCTRLS.match(keyEvent)){
                    try {
                        encrypter.encryptInformationAndWriteItToFile(password, textArea.getText());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        mistakeLabel.setLayoutX(x - 133);
        mistakeLabel.setLayoutY(190);

        Button saveFileButton = new Button();
        saveFileButton.setText("Save");
        saveFileButton.setLayoutX(x - 133);
        saveFileButton.setLayoutY(30);
        saveFileButton.setPrefSize(100, 50);
        saveFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    encrypter.encryptInformationAndWriteItToFile(password, textArea.getText());
                } catch(Exception ignored) {}
            }
        });

        Label textSearchLabel = new Label("Search text");
        textSearchLabel.setLayoutX(x - 117);
        textSearchLabel.setLayoutY(100);

        textFieldForSearching.setLayoutX(x - 133);
        textFieldForSearching.setLayoutY(130);
        textFieldForSearching.setPrefSize(100, 50);
        textFieldForSearching.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                    try {
                        search(textFieldForSearching.getText(), textArea.getText());
                    } catch (Exception ignored) {}
                }
                else if (keyCombinationCTRLS.match(keyEvent)) {
                    try {
                        encrypter.encryptInformationAndWriteItToFile(password, textArea.getText());
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        informationPane.getChildren().add(saveFileButton);
        informationPane.getChildren().add(textSearchLabel
        );
        informationPane.getChildren().add(textArea);
        informationPane.getChildren().add(textFieldForSearching);
        informationPane.getChildren().add(mistakeLabel);

        Scene informationScene = new Scene(informationPane, x, y);
        informationScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyCombinationCTRLF.match(keyEvent)){
                    textFieldForSearching.requestFocus();
                }
            }
        });
        informationStage.setScene(informationScene);
        informationStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    if (!textArea.getText().equals(new String(encrypter.decryptFile(password), StandardCharsets.UTF_8))){
                        try {
                            createAlert(encrypter, password);
                        } catch (Exception ignored){}
                    }
                } catch (Exception ignored){}
            }
        });
        informationStage.show();
    }
    private static void search(String text, String decryptedText){
        textArea.requestFocus();
        if (needfulText.equals(text)) {
            copyOfDecryptedText = copyOfDecryptedText.substring(0, copyOfDecryptedText.indexOf(text)) + copyOfDecryptedText.substring(copyOfDecryptedText.indexOf(text) + text.length());
            textCount++;
        } else {
            textCount = 0;
            copyOfDecryptedText = decryptedText;
        }
        if (copyOfDecryptedText.contains(text)) {
            textArea.positionCaret(copyOfDecryptedText.indexOf(text) + textCount * text.length());
            mistakeLabel.setText(null);
        }
        else {
            if (textCount == 0){
                mistakeLabel.setText("There is no such text");
            }
            else {
                mistakeLabel.setText("There is no more \n  such text");
            }
        }
        needfulText = text;
    }
    private static void createAlert(Encrypter encrypter, String password) throws NoSuchPaddingException, UnrecoverableEntryException, IllegalBlockSizeException, CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, BadPaddingException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "File has been modified, save changes?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Save file");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.YES)){
            encrypter.encryptInformationAndWriteItToFile(password, textArea.getText());
        }
    }
}