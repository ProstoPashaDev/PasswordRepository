package com.Pavel.passwordrepository;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    public void specifyEncrypter() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnrecoverableEntryException, CertificateException, NoSuchAlgorithmException, BadPaddingException, KeyStoreException, ParseException, InvalidKeyException, NoSuchProviderException {
        PasswordRepository pwdRepository = new PasswordRepository();
        Encrypter encrypter = new Encrypter();
        try {
            String password = passwordField.getText();
            Encrypter.setPassword(password);
            //TODO naming
            //Only for compiler optimization, because we decrypt before opening window
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
            specifyEncrypter();
        }
    }
}
