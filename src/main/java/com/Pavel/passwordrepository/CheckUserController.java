package com.Pavel.passwordrepository;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.ParseException;

public class CheckUserController {
    @FXML
    Stage entryStage;
    @FXML
    Label entryPasswordLbl;
    @FXML
    Label wrongPasswordLbl;
    @FXML
    PasswordField passwordField;
    @FXML
    Button checkPasswordBtn;
    @FXML
    TextField passwordFieldToSeePwd;
    @FXML
    Button seePwdButton;
    @FXML
    Button hidePwdButton;

    private boolean seePwd = false;

    @FXML
    public void authorize() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnrecoverableEntryException, CertificateException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, ParseException, InvalidKeyException, NoSuchProviderException {
        PasswordRepository pwdRepository = new PasswordRepository();
        Encrypter encrypter = new Encrypter();
        try {
            String password = seePwd
                    ? passwordFieldToSeePwd.getText()
                    : passwordField.getText();
            Encrypter.setPassword(password);
            //TODO naming
            //Checking password validity
            encrypter.getDecryptedText();
            pwdRepository.showPasswordRepository();
            entryStage.close();
        } catch (IOException e) {
            wrongPwdAnimation();
        }
    }

    public void wrongPwdAnimation() {
        /* Set up a Timeline animation */
        wrongPasswordLbl.setVisible(true);
        KeyValue initKeyValue = new KeyValue(wrongPasswordLbl.opacityProperty(), 1.0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue = new KeyValue(wrongPasswordLbl.opacityProperty(), 0.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(2), endKeyValue);
        Timeline timeline = new Timeline(initFrame, endFrame);
        //timeline.setCycleCount(1);
        timeline.play();
    }


    @FXML
    public void decryptFileEnt(KeyEvent keyEvent) throws InvalidAlgorithmParameterException, UnrecoverableEntryException, NoSuchPaddingException, IllegalBlockSizeException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, BadPaddingException, ParseException, InvalidKeyException, NoSuchProviderException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            authorize();
        }
    }

    public void seePassword(MouseEvent mouseEvent) {

        passwordField.setVisible(false);
        passwordFieldToSeePwd.setVisible(true);
        passwordFieldToSeePwd.setText(passwordField.getText());
        seePwdButton.setVisible(false);
        hidePwdButton.setVisible(true);
        seePwd = true;
        passwordFieldToSeePwd.requestFocus();
        passwordFieldToSeePwd.selectPositionCaret(passwordFieldToSeePwd.getText().length());
    }

    public void hidePassword(MouseEvent mouseEvent) {
        passwordFieldToSeePwd.setVisible(false);
        passwordField.setVisible(true);
        passwordField.setText(passwordFieldToSeePwd.getText());
        hidePwdButton.setVisible(false);
        seePwdButton.setVisible(true);
        seePwd = false;
        passwordField.requestFocus();
        passwordField.selectPositionCaret(passwordField.getText().length());
    }
}
