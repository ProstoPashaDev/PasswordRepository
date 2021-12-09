package com.example.passwordrepository;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

public class Encrypter {

    private final String wayToKeyStore = "C://Users/Hramo/.keystore";
    private final String alias = "test1";
    private final String wayToFile = "E://Храмов Павел/Программирование в java/PasswordRepository/src/PasswordRepository.txt";

    void encryptInformationAndWriteItToFile(String password, String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, CertificateException, IOException, KeyStoreException, UnrecoverableEntryException, IllegalBlockSizeException, BadPaddingException {
        PublicKey publicKey = getPublicKey(password);
        Cipher encryptionCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(wayToFile));
        BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(wayToFile));
        System.out.println(text);
        byte[] encryptedText = encryptionCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        System.out.println(encryptedText);
        bufferedWriter.write(encryptedText);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    byte[] decryptFile(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, UnrecoverableEntryException, CertificateException, KeyStoreException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(password));

        byte[] symbolsFromFile;
        byte[] line = new byte[0];

        Path path = Paths.get(wayToFile);
        symbolsFromFile = Files.readAllBytes(path);
        System.out.println(symbolsFromFile);
        byte[] decryptedText = null;
        if (!Arrays.equals(symbolsFromFile, line)){
            System.out.println(symbolsFromFile);
            decryptedText = cipher.doFinal(symbolsFromFile);
        }
        System.out.println(decryptedText);
        System.out.println("We are here");
        return decryptedText;
    }
    
    PublicKey getPublicKey(String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {
        KeyStore keyStore = optionKeyStoreParam(password);
        System.out.println("OK");
        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
        System.out.println(publicKey);
        return publicKey;
    }
    PrivateKey getPrivateKey(String password) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        KeyStore keyStore = optionKeyStoreParam(password);
        KeyStore.ProtectionParameter passwordPR = new KeyStore.PasswordProtection(password.toCharArray());
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, passwordPR);
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();
        return privateKey;
    }
    KeyStore optionKeyStoreParam(String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        FileInputStream inputStream = new FileInputStream(wayToKeyStore);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(inputStream, password.toCharArray());
        return keyStore;
    }
}
