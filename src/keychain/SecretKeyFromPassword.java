package keychain;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Derives a secret key from a given password
 */
class SecretKeyFromPassword {

    /** The derived secret key */
    final PBEKey dSecretKey;

    SecretKeyFromPassword(char[] password) {
        SecretKeyFactory keyFactory = getKeyFactory();
        PBEKeySpec pbeKeySpec = getKeySpec();
        dSecretKey = (PBEKey) generateSecretKey(keyFactory, pbeKeySpec);
    }

    //********** private helpers ******************//

    private static SecretKeyFactory getKeyFactory() {
        var algorithm = "PBKDF2WithHmacSHA256";

        try {
            return SecretKeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static PBEKeySpec getKeySpec() {
        byte[] salt = generateSalt();
        var iterationCount = 10000;
        var keyLenInBit = 256;

        return new PBEKeySpec(new char[]{}, salt, iterationCount, keyLenInBit);
    }

    private static byte[] generateSalt() {
        var saltSize = 32; // bytes
        byte[] salt = new byte[saltSize];

        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static SecretKey generateSecretKey(SecretKeyFactory keyFactory, PBEKeySpec keySpec) {
        try {
            return keyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
