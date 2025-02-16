package src.br.edu.ifba.encryptation;

import java.security.KeyPair;

import src.br.edu.ifba.encryptation.crypter.Crypter;
import src.br.edu.ifba.encryptation.impl.CrypterImpl;
import src.br.edu.ifba.encryptation.impl.KeyGeneratorImpl;
import src.br.edu.ifba.encryptation.keys.KeysGenerator;
import src.br.edu.ifba.encryptation.randomness.RealRandomnessGenerator;

public class App {

    private static final String VIDEO_PATH = "C:/Users/hemer/OneDrive/Área de Trabalho/IFBA/complexidade_algoritmos/printer_version2/encryptation/video/aquarium.mp4";
    private static final String ENCRYPTION_ALGORITHM = "RSA";

    private static final String PRIVATE_KEY_PATH = "C:/Users/hemer/OneDrive/Área de Trabalho/IFBA/complexidade_algoritmos/printer_version2/server/key/private.key";
    private static final String PUBLIC_KEY_PATH = "C:/Users/hemer/OneDrive/Área de Trabalho/IFBA/complexidade_algoritmos/printer_version2/printer/key/public.key";

    public static void main(String[] args) throws Exception {
        KeysGenerator<RealRandomnessGenerator> keyGenerator = new KeyGeneratorImpl();
        keyGenerator.initialize(new RealRandomnessGenerator(VIDEO_PATH), ENCRYPTION_ALGORITHM);

        for (int i = 0; i < 10; i++) {
            System.out.println("******* generating a new key pair #" + (i + 1) + " *******");
            KeyPair keys = keyGenerator.generateKeys();

            Crypter crypter = new CrypterImpl(keys, ENCRYPTION_ALGORITHM);
            String encrypted = crypter.encrypt("Algorithm complexity");
            System.out.println("encrypted: " + encrypted);

            String decrypted = crypter.decrypt(encrypted);
            System.out.println("decrypted: " + decrypted);
        }

        keyGenerator.generateKeys(PRIVATE_KEY_PATH, PUBLIC_KEY_PATH);
        keyGenerator.finalize();
    }
}
