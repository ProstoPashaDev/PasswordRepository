package com.Pavel.passwordrepository;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;

public class Encrypter {

    private String wayToKeyStore = "";
    private final String alias = "test1";
    private String wayToFile = "";

    void encryptInformationAndWriteItToFile(String password, String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, CertificateException, IOException, KeyStoreException, UnrecoverableEntryException, IllegalBlockSizeException, BadPaddingException {
        PublicKey publicKey = getPublicKey(password);
        Cipher encryptionCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(wayToFile));
        byte[] encryptedText = encryptionCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        bufferedWriter.write(encryptedText);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    byte[] decryptFile(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, UnrecoverableEntryException, CertificateException, KeyStoreException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(password));

        byte[] symbolsFromFile;
        Path path = Paths.get(wayToFile);
        symbolsFromFile = Files.readAllBytes(path);
        byte[] decryptedText = null;
        if (!Arrays.equals(symbolsFromFile, new byte[0])){
            decryptedText = cipher.doFinal(symbolsFromFile);
        }
        return decryptedText;
    }
    
    PublicKey getPublicKey(String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {
        KeyStore keyStore = optionKeyStoreParam(password);
        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
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
        getWayToFileAndToKeystore();
        FileInputStream inputStream = new FileInputStream(wayToKeyStore);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(inputStream, password.toCharArray());
        return keyStore;
    }
    private void getWayToFileAndToKeystore() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("E://Храмов Павел/Программирование в java/PasswordRepository/src/WayToFile.txt"));
        bufferedReader.readLine();
        wayToFile = bufferedReader.readLine();
        bufferedReader.readLine();
        wayToKeyStore = bufferedReader.readLine();
    }
}
