package src.br.edu.ifba.encryptation.impl;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import src.br.edu.ifba.encryptation.crypter.Crypter;
import src.br.edu.ifba.encryptation.exceptions.EncryptionFailure;



public class CrypterImpl extends Crypter {

    public CrypterImpl(KeyPair chaves, String algoritmoDeEncriptacao) {
        super(chaves, algoritmoDeEncriptacao);
    }

    @Override
    public String encrypt(String dados) throws EncryptionFailure {
        String encriptacao = "";

        synchronized (encriptacao) {
            try {
                Cipher cifrador = Cipher.getInstance(algoritmoDeEncriptacao);
                cifrador.init(Cipher.ENCRYPT_MODE, chaves.getPublic());

                byte[] cifragem = cifrador.doFinal(dados.getBytes(StandardCharsets.UTF_8));
                encriptacao = Base64.getEncoder().encodeToString(cifragem);
            }
            catch (NoSuchAlgorithmException e) {
                throw new EncryptionFailure("falha encriptando dados: " + e.getMessage());
            } catch (NoSuchPaddingException e) {
                throw new EncryptionFailure("falha encriptando dados: " + e.getMessage());
            } catch (InvalidKeyException e) {
                throw new EncryptionFailure("falha encriptando dados: " + e.getMessage());
            } catch (IllegalBlockSizeException e) {
                throw new EncryptionFailure("falha encriptando dados: " + e.getMessage());
            } catch (BadPaddingException e) {
                throw new EncryptionFailure("falha encriptando dados: " + e.getMessage());
            }

        }
        
        return encriptacao;
    }

    @Override
    public String decrypt(String encriptacao) throws EncryptionFailure {
        String dados = null;

        try {
            Cipher cifrador = Cipher.getInstance(algoritmoDeEncriptacao);
            cifrador.init(Cipher.DECRYPT_MODE, chaves.getPrivate());

            byte[] bytes = Base64.getDecoder().decode(encriptacao);
            byte[] bytesDecriptados = cifrador.doFinal(bytes);

            dados = new String(bytesDecriptados, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new EncryptionFailure("falha encriptando dados: " + e.getMessage());
        }

        return dados;
    }

}
