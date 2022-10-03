package keychain.secretkeys;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoService {

    private final SecretKey macKey;
    private final byte[] salt;

    public CryptoService(char[] password) {
        SessionKeys sessionKeys = new SessionKeys(password);
        this.salt = sessionKeys.getSalt();
        this.macKey = sessionKeys.macKey();
    }

    public CryptoService(char[] password, byte[] salt) {
        SessionKeys sessionKeys = new SessionKeys(password, salt);
        this.salt = sessionKeys.getSalt();
        this.macKey = sessionKeys.macKey();

    }

    public String mac(String plainText) {
        Mac mac = getAndInitMac(macKey);
        byte[] hash = mac.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(hash);
    }

    public byte[] getSalt() {
        return salt;
    }

    private Mac getAndInitMac(SecretKey authKey) {
        var algorithm = "HmacSHA256";
        Mac mac;

        try {
            mac = Mac.getInstance(algorithm);
            mac.init(authKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return mac;
    }
}
