package com.Pavel.passwordrepository;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Locale;

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

    public static String decryptedText;

    @Override
    public void start(Stage stage) throws IOException {
        showEntryStage();
    }

    public void startApplication() {
        launch();
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

        //informationStage.setTitle("Ok, you arent a virus")
        //textArea.insertText(0, decryptedText);
        //textArea.setPrefSize(x - 360, y - 120);
        //AnchorPane.setLeftAnchor(textArea, 20.0);
        //AnchorPane.setRightAnchor(textArea, 150.0);
        //AnchorPane.setBottomAnchor(textArea, 20.0);
        //AnchorPane.setTopAnchor(textArea, 10.0);
        //textArea.setStyle("-fx-highlight-fill: DeepSkyBlue; -fx-highlight-text-fill: white; -fx-font-size:14px;");
        //textArea.setStyle("-fx-highlight-fill: Moccasin; -fx-highlight-text-fill: black; -fx-font-size:14px;");
        //final KeyCombination keyCombinationCTRLF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
//        final KeyCombination keyCombinationCTRLS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
//        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                if (keyCombinationCTRLF.match(keyEvent)){
//                    textFieldForSearching.requestFocus();
//                }
//                else if (keyCombinationCTRLS.match(keyEvent)){
//                    try {
//                        encrypter.encryptText(textArea.getText());
//                    }
//                    catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        mistakeLabel.setLayoutX(x - 133);
//        mistakeLabel.setLayoutY(190);
//        AnchorPane.setRightAnchor(mistakeLabel, 20.0);

        //Button saveFileButton = new Button();
        //saveFileButton.setText("Save");
        //saveFileButton.setLayoutX(x - 133);
//        saveFileButton.setLayoutY(30);
//        saveFileButton.setPrefSize(100, 50);
//        AnchorPane.setRightAnchor(saveFileButton, 20.0);
//        saveFileButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                try {
//                    encrypter.encryptInformationAndWriteItToFile(password, textArea.getText());
//                } catch(Exception ignored) {}
//            }
//        });

        //Label textSearchLabel = new Label("Search text");
        //textSearchLabel.setLayoutX(x - 117);
        //textSearchLabel.setLayoutY(100);
        //AnchorPane.setRightAnchor(textSearchLabel, 60.0);

        //textFieldForSearching.setLayoutX(x - 133);
        //textFieldForSearching.setLayoutY(130);
        //textFieldForSearching.setPrefSize(100, 50);
        //AnchorPane.setRightAnchor(textFieldForSearching, 20.0);
//        textFieldForSearching.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                if(keyEvent.getCode().equals(KeyCode.ENTER)) {
//                    try {
//                        //search(textFieldForSearching.getText().toLowerCase(Locale.ROOT), textArea.getText().toLowerCase(Locale.ROOT));
//                    } catch (Exception ignored) {}
//                }
////                else if (keyCombinationCTRLS.match(keyEvent)) {
////                    try {
////                        encrypter.encryptText(textArea.getText());
////                    } catch (Exception ignored) {
////                    }
////                }
//            }
//        });
        //informationPane.getChildren().add(textArea);
        //informationPane.getChildren().add(saveFileButton);
        //informationPane.getChildren().add(textSearchLabel);
        //informationPane.getChildren().add(textFieldForSearching);
        //informationPane.getChildren().add(mistakeLabel);


        //Scene informationScene = new Scene(informationPane, x - 200, y - 100);
//        informationScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                if (keyCombinationCTRLF.match(keyEvent)){
//                    textFieldForSearching.requestFocus();
//                }
//            }
//        });
        //informationStage.setScene(informationScene);
//        informationStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent windowEvent) {
//                try {
//                    if (!textArea.getText().equals(new String(encrypter.decryptFile(), StandardCharsets.UTF_8))){
//                        try {
//                            //createAlert(encrypter, password);
//                        } catch (Exception ignored){}
//                    }
//                } catch (Exception ignored){}
//            }
//        });
        //informationStage.show();
    }
//    public void search(String searchWord, String text){
//        textArea.selectRange(0, 0);
//        if (needfulText.equals(searchWord)) {
//            copyOfDecryptedText = copyOfDecryptedText.substring(0, copyOfDecryptedText.indexOf(searchWord)) + copyOfDecryptedText.substring(copyOfDecryptedText.indexOf(searchWord) + searchWord.length());
//            textCount++;
//        } else {
//            textCount = 0;
//            copyOfDecryptedText = text;
//        }
//        if (copyOfDecryptedText.contains(searchWord)) {
//            textArea.selectRange(copyOfDecryptedText.indexOf(searchWord) + textCount * searchWord.length(), copyOfDecryptedText.indexOf(searchWord) + textCount * searchWord.length() + searchWord.length());
//            mistakeLabel.setText(null);
//        }
//        else {
//            if (textCount == 0){
//                mistakeLabel.setText("There is no such text");
//            }
//            else {
//                mistakeLabel.setText("There is no more \n  such text");
//            }
//        }
//        textFieldForSearching.requestFocus();
//        needfulText = searchWord;
//    }
}