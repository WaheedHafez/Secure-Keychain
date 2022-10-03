package keychain.secretkeys;

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
        PBEKeySpec pbeKeySpec = getKeySpec(password);
        dSecretKey = (PBEKey) generateSecretKey(keyFactory, pbeKeySpec);
    }

    public SecretKeyFromPassword(char[] password, byte[] salt) {
        SecretKeyFactory keyFactory = getKeyFactory();
        PBEKeySpec pbeKeySpec = getKeySpec(password, salt);
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

    private static PBEKeySpec getKeySpec(char[] password) {
        byte[] salt = generateSalt();

        return getKeySpec(password, salt);
    }

    private static PBEKeySpec getKeySpec(char[] password, byte[] salt) {
        var iterationCount = 10000;
        var keyLenInBit = 256;

        return new PBEKeySpec(password, salt, iterationCount, keyLenInBit);
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
