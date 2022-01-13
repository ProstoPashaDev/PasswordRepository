package com.Pavel.passwordrepository;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Properties;

public class Encrypter {

    private String pathToKeyStore = "";
    private String alias = "";
    final private String pathToFile = "E://Khramov Pavel/Programming in java/PasswordRepository/src/PasswordRepository.dat";
    //final private String pathToFile = "PasswordRepository.dat";
    final private String pathToEncryptedKey = "E://Khramov Pavel/Programming in java/PasswordRepository/src/key.dat";
    //final private String pathToEncryptedKey = "key.dat";

    void encryptInformationAndWriteItToFile(String password, String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, CertificateException, IOException, KeyStoreException, UnrecoverableEntryException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {
        //PublicKey publicKey = getPublicKey(password);
        //Cipher encryptionCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
       // Cipher encryptionCipher = new NullCipher();
        //Cipher.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        /*
        Cipher encryptionCipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(pathToFile));
        byte[] encryptedText = encryptionCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        bufferedWriter.write(encryptedText);
        bufferedWriter.flush();
        bufferedWriter.close();
        */



        SecretKey aesKey = getAESKey(password);
        Cipher aesGCMCipher = Cipher.getInstance("AES/GCM/Nopadding");
        GCMParameterSpec staticParameterSpec = new GCMParameterSpec(128, new byte[12]);
        aesGCMCipher.init(Cipher.ENCRYPT_MODE, aesKey, staticParameterSpec);
        byte[] encryptedText = aesGCMCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(pathToFile));

        bufferedWriter.write(encryptedText);
        bufferedWriter.flush();
        bufferedWriter.close();

    }

    byte[] decryptFile(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, UnrecoverableEntryException, CertificateException, KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException {
        //Security.addProvider(new BouncyCastleProvider());
        //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //KeyFactory keyFactory = ECKeyFactory.;
        //Cipher cipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        /*cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(password));
        byte[] symbolsFromFile;
        Path path = Paths.get(pathToFile);
        symbolsFromFile = Files.readAllBytes(path);
        byte[] decryptedText = null;
        if (!Arrays.equals(symbolsFromFile, new byte[0])){
            decryptedText = cipher.doFinal(symbolsFromFile);
        }
        return decryptedText;*/



        SecretKey aesKey = getAESKey(password);

        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/Nopadding");
        GCMParameterSpec staticParameterSpec = new GCMParameterSpec(128, new byte[12]);
        decryptionCipher.init(Cipher.DECRYPT_MODE, aesKey, staticParameterSpec);

        Path path = Paths.get(pathToFile);
        byte[] symbolsFromFile = Files.readAllBytes(path);
        byte[] decryptedText = new byte[0];
        if (!Arrays.equals(symbolsFromFile, new byte[0])){
            decryptedText = decryptionCipher.doFinal(symbolsFromFile);
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
    KeyStore optionKeyStoreParam(String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        getPathToKeystoreAndAlias();
        FileInputStream inputStream = new FileInputStream(pathToKeyStore);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(inputStream, password.toCharArray());
        return keyStore;
    }
    private void getPathToKeystoreAndAlias() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("E://Khramov Pavel/Programming in java/PasswordRepository/src/PasswordRepository.cfg"));
        //properties.load(new FileInputStream("PasswordRepository.cfg"));
        pathToKeyStore = properties.getProperty("Path_to_keystore");
        alias = properties.getProperty("Alias");
    }
    private SecretKey getAESKey(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IOException, UnrecoverableEntryException, CertificateException, KeyStoreException {
        Path path = Paths.get(pathToEncryptedKey);
        byte[] encryptedAESKey = Files.readAllBytes(path);
        if (Arrays.equals(encryptedAESKey, new byte[0])){
            generateAESKey(password);
            encryptedAESKey = Files.readAllBytes(path);
        }
        return decryptAESKey(password, encryptedAESKey);
    }
    private void generateAESKey(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, UnrecoverableEntryException, CertificateException, IOException, KeyStoreException, InvalidKeyException, IllegalBlockSizeException {
        Cipher encryptionCipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA1andMGF1Padding");
        encryptionCipher.init(Cipher.WRAP_MODE, getPublicKey(password));

        KeyGenerator aesKeyGenerator = KeyGenerator.getInstance("AES");
        aesKeyGenerator.init(128);
        SecretKey aesKey = aesKeyGenerator.generateKey();
        byte[] wrappedKey = encryptionCipher.wrap(aesKey);
        BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(pathToEncryptedKey));

        bufferedWriter.write(wrappedKey);
        bufferedWriter.flush();
        bufferedWriter.close();
    }
    private SecretKey decryptAESKey(String password, byte[] encryptedAESKey) throws NoSuchPaddingException, NoSuchAlgorithmException, UnrecoverableEntryException, CertificateException, IOException, KeyStoreException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher decryptionCipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA1andMGF1Padding");
        decryptionCipher.init(Cipher.UNWRAP_MODE, getPrivateKey(password));
        SecretKey aesKey = (SecretKey) decryptionCipher.unwrap(encryptedAESKey, "AES", Cipher.SECRET_KEY);
        return aesKey;
    }
}
