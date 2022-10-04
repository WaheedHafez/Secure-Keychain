package keychain.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class EncryptionService {

    private final SecretKey key;
    private final Cipher cipher;

    public EncryptionService(SecretKey secretKey) {
        var aesGcm = "AES/GCM/NOPADDING";
        try {
            cipher = Cipher.getInstance(aesGcm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }

        this.key = new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    public String encrypt(String plainText) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptionBytes = cipher.doFinal(plainText.getBytes());
            AlgorithmParameters algorithmParameters = cipher.getParameters();
            AESGCMCipherText aesgcmCipherText = new AESGCMCipherText(encryptionBytes, algorithmParameters);
            return aesgcmCipherText.json;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String cipherText) {
        AESGCMCipherText aesgcmCipherText = new AESGCMCipherText(cipherText);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, aesgcmCipherText.algorithmParameters);

            byte[] plainBytes = cipher.doFinal(aesgcmCipherText.encryptionBytes);
            return new String(plainBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

}
