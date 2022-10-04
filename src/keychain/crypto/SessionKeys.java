package keychain.crypto;

import javax.crypto.SecretKey;
import javax.crypto.interfaces.PBEKey;

class SessionKeys {

    private final SecretKey[] sessionKeys;
    private final byte[] salt;

    public SessionKeys(char[] password) {
        var secretKeyFromPassword = new SecretKeyFromPassword(password);
        PBEKey sk = secretKeyFromPassword.dSecretKey;
        this.salt = sk.getSalt();
        sessionKeys = KeyExpansion.expand(sk, 2);
    }

    public SessionKeys(char[] password, byte[] salt) {
        var secretKeyFromPassword = new SecretKeyFromPassword(password, salt);
        PBEKey sk = secretKeyFromPassword.dSecretKey;
        this.salt = sk.getSalt();
        sessionKeys = KeyExpansion.expand(sk, 2);
    }

    public SecretKey encryptionKey() {
        return sessionKeys[0];
    }

    public SecretKey macKey() {
        return sessionKeys[1];
    }

    public byte[] getSalt() {
        return salt;
    }
}
