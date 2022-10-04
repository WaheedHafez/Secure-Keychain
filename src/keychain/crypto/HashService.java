package keychain.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

class HashService {

    private final MessageDigest digest;

    public HashService() {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String hash(String plainText) {
        digest.reset();
        byte[] hashBytes = this.digest.digest(plainText.getBytes());
        return HexFormat.of().formatHex(hashBytes);
    }
}
