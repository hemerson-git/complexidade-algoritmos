package src.br.edu.ifba.encryptation.keys;

import java.security.KeyPair;
import java.security.SecureRandom;

import src.br.edu.ifba.encryptation.exceptions.KeyGenerationFailure;

public interface KeysGenerator<RandomnessGenerator extends SecureRandom> {
    
    public void initialize(RandomnessGenerator randomnessGenerator, String encryptionAlgorithm);

    public KeyPair generateKeys() throws KeyGenerationFailure;

    public void generateKeys(String privateKeyFile, String publicKeyFile) throws KeyGenerationFailure;

    public void finalize() throws KeyGenerationFailure;

}
