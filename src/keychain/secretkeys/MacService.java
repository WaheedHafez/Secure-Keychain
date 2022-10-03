package keychain.secretkeys;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class MacService {

    private final Mac mac;

    public MacService(SecretKey macKey) {
        var algorithm = "HmacSHA256";
        Mac mac;

        try {
            mac = Mac.getInstance(algorithm);
            mac.init(macKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        this.mac = mac;
    }

    public String mac(String plainText) {
        mac.reset();
        byte[] hash = mac.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
