package com.Pavel.passwordrepository;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class PasswordRepositoryController {

    static private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static private final double X = screenSize.getWidth();
    //private DoubleProperty prefHeight;
    static private final double Y = screenSize.getHeight();

    private final KeyCombination KEY_COMBINATION_CTRLF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
    private final KeyCombination KEY_COMBINATION_CTRLS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

    @FXML
    TextArea textArea;
    @FXML
    Scene pwdRepositoryScene;
    @FXML
    Stage pwdRepositoryStage;
    @FXML
    AnchorPane pwdRepositoryAnchorPane;
    @FXML
    Label searchTextLabel;
    @FXML
    TextField searchTextField;
    @FXML
    Label fileSavedLabel;
    @FXML
    Label mistakeSearchLabel;

    //Variables for search algorithm
    int textCount = 0;
    String needfulText = "";
    String copyOfDecryptedText;

    @FXML
    public void initialize(WindowEvent windowEvent) throws InvalidAlgorithmParameterException, UnrecoverableEntryException, NoSuchPaddingException, IllegalBlockSizeException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, BadPaddingException, ParseException, InvalidKeyException, NoSuchProviderException {
        Encrypter encrypter = new Encrypter();
        pwdRepositoryStage.setWidth(X - 200);
        pwdRepositoryStage.setHeight(Y - 100);
        copyOfDecryptedText = encrypter.getDecryptedText();
        String decryptedText = copyOfDecryptedText;
        textArea.setText(decryptedText);
    }


    @FXML
    public void processShortCut(KeyEvent keyEvent) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnrecoverableEntryException, IllegalBlockSizeException, CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, BadPaddingException, ParseException, InvalidKeyException, NoSuchProviderException {
        if (KEY_COMBINATION_CTRLF.match(keyEvent)) {
            searchFocus();
        } else if (KEY_COMBINATION_CTRLS.match(keyEvent)) {
            onSaveClicked();
        }
    }
    @FXML
    public void onSaveClicked() throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnrecoverableEntryException, IllegalBlockSizeException, CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, ParseException, InvalidKeyException, NoSuchProviderException {
        saveText();
        animation();
    }

    public void animation() {
        /* Set up a Timeline animation */
        fileSavedLabel.setVisible(true);
        KeyValue initKeyValue = new KeyValue(fileSavedLabel.opacityProperty(), 1.0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue = new KeyValue(fileSavedLabel.opacityProperty(), 0.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(1.5), endKeyValue);
        Timeline timeline = new Timeline(initFrame, endFrame);
        timeline.play();
    }

    @FXML
    public void searchFocus() {
        searchTextField.requestFocus();
    }

    public void saveText() throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnrecoverableEntryException, IllegalBlockSizeException, CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, ParseException, InvalidKeyException, NoSuchProviderException {
        Encrypter encrypter = new Encrypter();
        encrypter.saveText(textArea.getText());
    }

    public void search(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            //TODO search method
            try {
                search(searchTextField.getText().toLowerCase(Locale.ROOT), textArea.getText().toLowerCase(Locale.ROOT));
            } catch (StringIndexOutOfBoundsException ignored) {}
        }
    }
    @FXML
    public void checkTextChanged(WindowEvent windowEvent) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnrecoverableEntryException, IllegalBlockSizeException, CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, ParseException, InvalidKeyException, NoSuchProviderException {
        Encrypter encrypter = new Encrypter();
        if (!textArea.getText().equals(encrypter.getDecryptedText())) {
            createAlert();
        }
    }

    /**
     * Search algorithm O(text*searchWord)
     * @param searchWord word that we need to find
     * @param text source text
     */
    private void search(String searchWord, String text) {
        textArea.selectRange(0, 0);

        if (needfulText.equals(searchWord)) {
            copyOfDecryptedText = copyOfDecryptedText.substring(0, copyOfDecryptedText.indexOf(searchWord)) + copyOfDecryptedText.substring(copyOfDecryptedText.indexOf(searchWord) + searchWord.length());
            textCount++;
        } else {
            textCount = 0;
            copyOfDecryptedText = text;
        }
        if (copyOfDecryptedText.contains(searchWord)) {
            textArea.selectRange(copyOfDecryptedText.indexOf(searchWord) + textCount * searchWord.length(), copyOfDecryptedText.indexOf(searchWord) + textCount * searchWord.length() + searchWord.length());
            mistakeSearchLabel.setText(null);
        }
        else {
            if (textCount == 0) {
                mistakeSearchLabel.setText("There is no \n such text");
            }
            else {
                mistakeSearchLabel.setText("There is no \nmore such text");
            }
        }
        //textFieldForSearching.requestFocus();
        needfulText = searchWord;
    }

    private void createAlert() throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnrecoverableEntryException, IllegalBlockSizeException, CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, ParseException, InvalidKeyException, NoSuchProviderException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "File has been modified, save changes?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Save file");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.YES)){
            onSaveClicked();
        }
    }
    
    /**
     * Selecting text algorithm, skipping "_", "@" etc.
     */
    @FXML
    private void copyText(MouseEvent mouseEvent) {
        int caretPos = textArea.getCaretPosition();
        int leftBorder = Math.max(caretPos - 2, 0);
        int rightBorder = Math.max(caretPos - 1, 0);
        if (mouseEvent.getClickCount() == 2) {
            String text = textArea.getText();
            while (checkIndex(leftBorder, text) || checkIndex(rightBorder, text)) {
                if (checkIndex(leftBorder, text)) {
                    leftBorder--;
                }
                if (checkIndex(rightBorder, text)) {
                    rightBorder++;
                }
            }
            textArea.selectRange(Math.max(leftBorder + 1, 0), rightBorder);
        }
    }

    private boolean checkIndex(int index, String text) {
        return index != 0 && index != text.length() && text.charAt(index) != ' ' && text.charAt(index) != '\n';
    }
}
