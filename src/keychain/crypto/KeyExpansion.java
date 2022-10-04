package keychain.crypto;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class KeyExpansion {

    static SecretKey[] expand(SecretKey sk, int n) {
        // inspired from https://github.com/square/keywhiz/blob/master/hkdf/src/main/java/keywhiz/hkdf/Hkdf.java#L102

        SecretKey[] secretKeys = new SecretKey[n];

        Mac prf = getMac();
        try {
            prf.init(sk);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < n; i++) {
            prf.reset();
            byte[] derivedKey = prf.doFinal(new byte[]{(byte) i});
            secretKeys[i] = new SecretKeySpec(derivedKey, "");
        }

        return secretKeys;
    }

    private static Mac getMac() {
        var algorithm = "HmacSHA256";

        try {
            return Mac.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
