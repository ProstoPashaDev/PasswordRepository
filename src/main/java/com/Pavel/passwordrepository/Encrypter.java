package com.Pavel.passwordrepository;

import javafx.scene.control.Alert;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Encrypter {

    private String pathToKeyStore = "";
    private String alias = "";
    private boolean fl = true;
    //private final String PATH_DATAFILE = "E://Khramov Pavel/Programming in java/PasswordRepository/src/PasswordRepository.dat";
    //private final String PATH_DATAFILE = "C://Khramov Pavel/Project/Java/PasswordRepository/src/main/resources/PasswordRepository.dat";
    private final String PATH_DATAFILE = "PasswordRepository.dat";
    //private final String PATH_ENCRYPTED_KEY = "E://Khramov Pavel/Programming in java/PasswordRepository/src/key.dat";
    //private final String PATH_ENCRYPTED_KEY = "C://Khramov Pavel/Project/Java/PasswordRepository/src/main/resources/key.dat";
    private final String PATH_ENCRYPTED_KEY = "key.dat";
    //private final String PATH_CONFIGURATION = "C://Khramov Pavel/Project/Java/PasswordRepository/src/main/resources/PasswordRepository.cfg";
    private final String PATH_CONFIGURATION = "PasswordRepository.cfg";
    private static String password;

    public byte[] encryptText(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, CertificateException, IOException, KeyStoreException, UnrecoverableEntryException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, ParseException {
        SecretKey aesKey = getAESKey(Encrypter.password);
        Cipher aesGCMCipher = Cipher.getInstance("AES/GCM/Nopadding");
        GCMParameterSpec staticParameterSpec = new GCMParameterSpec(128, new byte[12]);
        aesGCMCipher.init(Cipher.ENCRYPT_MODE, aesKey, staticParameterSpec);
        byte[] encryptedText = aesGCMCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return encryptedText;
    }

    public void saveText(String text) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnrecoverableEntryException, IllegalBlockSizeException, CertificateException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, ParseException, InvalidKeyException {
        byte[] encryptedText = encryptText(text);
        BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(PATH_DATAFILE));
        bufferedWriter.write(encryptedText);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public String getDecryptedText() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, UnrecoverableEntryException, CertificateException, KeyStoreException, InvalidAlgorithmParameterException, ParseException {
        SecretKey aesKey = getAESKey(Encrypter.password);

        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/Nopadding");
        GCMParameterSpec staticParameterSpec = new GCMParameterSpec(128, new byte[12]);
        decryptionCipher.init(Cipher.DECRYPT_MODE, aesKey, staticParameterSpec);

        Path path = Paths.get(PATH_DATAFILE);
        byte[] symbolsFromFile = Files.readAllBytes(path);
        byte[] decryptedBytes = new byte[0];
        if (!Arrays.equals(symbolsFromFile, new byte[0])){
            decryptedBytes = decryptionCipher.doFinal(symbolsFromFile);
        }

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void setPassword(String password) {
        Encrypter.password = password;
    }


    PublicKey getPublicKey(String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = optionKeyStoreParam(password);
        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
        return publicKey;
    }
    PrivateKey getPrivateKey(String password) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException, ParseException {
        KeyStore keyStore = optionKeyStoreParam(password);
        KeyStore.ProtectionParameter passwordPR = new KeyStore.PasswordProtection(password.toCharArray());
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, passwordPR);
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        LocalDateTime localDateTime = LocalDateTime.now();
        Enumeration<String> aliases = keyStore.aliases();
        String alias = aliases.nextElement();
        Date keyStoreTime = ((X509Certificate) keyStore.getCertificate(alias)).getNotAfter();

        //Date date = new SimpleDateFormat("MMM", Locale.US).parse(String.valueOf(keyStoreTime).substring(4, 7));
        Date date = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US).parse(String.valueOf(keyStoreTime));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);



        if (((cal.get(Calendar.MONTH) + 1 - localDateTime.getMonthValue() <= 2 && cal.get(Calendar.YEAR) == localDateTime.getYear()) || (cal.get(Calendar.MONTH) + 1 == 1 && localDateTime.getMonthValue() == 11 && cal.get(Calendar.YEAR) == localDateTime.getYear() + 1) || ((cal.get(Calendar.MONTH) + 1 == 1 || cal.get(Calendar.MONTH) + 1 == 2) && localDateTime.getMonthValue() == 12 && cal.get(Calendar.YEAR) == localDateTime.getYear() + 1)) && fl){
            fl = false;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning alert");
            alert.setHeaderText("Validity keystore:");
            if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
                if (cal.get(Calendar.MONTH) + 1 < 10) {
                    alert.setContentText("The keystore ends at 0" + cal.get(Calendar.DAY_OF_MONTH) + ".0" + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR));
                } else {
                    alert.setContentText("The keystore ends at 0" + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR));
                }
            }
            else {
                if (cal.get(Calendar.MONTH) + 1 < 10) {
                    alert.setContentText("The keystore ends at " + cal.get(Calendar.DAY_OF_MONTH) + ".0" + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR));
                } else {
                    alert.setContentText("The keystore ends at " + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR));
                }
            }
            alert.showAndWait();
        }
        return privateKey;
    }
    KeyStore optionKeyStoreParam(String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        getPathToKeystoreAndAlias();
        FileInputStream inputStream = new FileInputStream(pathToKeyStore);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(inputStream, password.toCharArray());
        return keyStore;
    }
    private void getPathToKeystoreAndAlias() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(PATH_CONFIGURATION));
        pathToKeyStore = properties.getProperty("Path_to_keystore");
        alias = properties.getProperty("Alias");
    }
    private SecretKey getAESKey(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, IOException, UnrecoverableEntryException, CertificateException, KeyStoreException, ParseException {
        Path path = Paths.get(PATH_ENCRYPTED_KEY);
        byte[] encryptedAESKey = Files.readAllBytes(path);
        if (Arrays.equals(encryptedAESKey, new byte[0])){
            generateAESKey(password);
            encryptedAESKey = Files.readAllBytes(path);
        }
        return decryptAESKey(password, encryptedAESKey);
    }
    private void generateAESKey(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, InvalidKeyException, IllegalBlockSizeException {
        Cipher encryptionCipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA1andMGF1Padding");
        encryptionCipher.init(Cipher.WRAP_MODE, getPublicKey(password));

        KeyGenerator aesKeyGenerator = KeyGenerator.getInstance("AES");
        aesKeyGenerator.init(128);
        SecretKey aesKey = aesKeyGenerator.generateKey();
        byte[] wrappedKey = encryptionCipher.wrap(aesKey);
        BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(PATH_ENCRYPTED_KEY));

        bufferedWriter.write(wrappedKey);
        bufferedWriter.flush();
        bufferedWriter.close();
    }
    private SecretKey decryptAESKey(String password, byte[] encryptedAESKey) throws NoSuchPaddingException, NoSuchAlgorithmException, UnrecoverableEntryException, CertificateException, IOException, KeyStoreException, InvalidKeyException, ParseException {
        Cipher decryptionCipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA1andMGF1Padding");
        decryptionCipher.init(Cipher.UNWRAP_MODE, getPrivateKey(password));
        SecretKey aesKey = (SecretKey) decryptionCipher.unwrap(encryptedAESKey, "AES", Cipher.SECRET_KEY);
        return aesKey;
    }
}


