package src.br.edu.ifba.encryptation.crypter;

import java.security.KeyPair;

import src.br.edu.ifba.encryptation.exceptions.EncryptionFailure;


public abstract class Crypter {
    
    protected KeyPair chaves = null;
    protected String algoritmoDeEncriptacao = null;


    public Crypter(KeyPair chaves, String algoritmoDeEncriptacao) {
        this.chaves = chaves;
        this.algoritmoDeEncriptacao = algoritmoDeEncriptacao;
    }

    public abstract String encrypt(String dados) throws EncryptionFailure;

    public abstract String decrypt(String encriptacao) throws EncryptionFailure;

}
