package src.br.edu.ifba.encryptation.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import src.br.edu.ifba.encryptation.exceptions.KeyGenerationFailure;
import src.br.edu.ifba.encryptation.keys.KeysGenerator;
import src.br.edu.ifba.encryptation.randomness.RealRandomnessGenerator;

public class KeyGeneratorImpl implements KeysGenerator<RealRandomnessGenerator> {

    private static final int ENCRYPTION_KEY_SIZE = 2048;

    private RealRandomnessGenerator randomnessGenerator = null;
    private String encryptionAlgorithm = null;

    @Override
    public void initialize(RealRandomnessGenerator randomnessGenerator, String encryptionAlgorithm) {
        this.randomnessGenerator = randomnessGenerator;
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    @Override
    public KeyPair generateKeys() throws KeyGenerationFailure {
        KeyPair keys = null;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(encryptionAlgorithm);
            keyPairGenerator.initialize(ENCRYPTION_KEY_SIZE, randomnessGenerator);

            keys = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new KeyGenerationFailure("Key generation failure: " + e.getMessage());
        }

        return keys;
    }

    @Override
    public void generateKeys(String privateKeyFile, String publicKeyFile) throws KeyGenerationFailure {
        KeyPair keys = generateKeys();

        byte[] bytes = keys.getPublic().getEncoded();
        writeToFile(publicKeyFile, bytes);

        bytes = keys.getPrivate().getEncoded();
        writeToFile(privateKeyFile, bytes);
    }

    private void writeToFile(String file, byte[] bytes) throws KeyGenerationFailure {
        FileOutputStream stream;
        try {
            File f = new File(file);
            if (f.exists()) {
                f.delete();
            }
            
            stream = new FileOutputStream(f);
            stream.write(bytes);
            stream.close();
        } catch (FileNotFoundException e) {
            throw new KeyGenerationFailure("Key generation failure: " + e.getMessage());
        } catch (IOException e) {
            throw new KeyGenerationFailure("Key generation failure: " + e.getMessage());
        }
    }

    @Override
    public void finalize() throws KeyGenerationFailure {
        randomnessGenerator.finalize();
    }

}
