package keychain.secretkeys;

import javax.crypto.SecretKey;

class SessionKeys {

    private final SecretKey[] sessionKeys;

    public SessionKeys(char[] password) {
        var secretKeyFromPassword = new SecretKeyFromPassword(password);
        SecretKey sk = secretKeyFromPassword.dSecretKey;
        sessionKeys = KeyExpansion.expand(sk, 2);
    }

    public SecretKey encryptionKey() {
        return sessionKeys[0];
    }

    public SecretKey macKey() {
        return sessionKeys[1];
    }
}
