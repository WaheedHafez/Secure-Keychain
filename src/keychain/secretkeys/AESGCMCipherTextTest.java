package keychain.secretkeys;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AESGCMCipherTextTest {

    @Test
    void testDifferentConstructorsButEqualObjects() throws NoSuchAlgorithmException, IOException {
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("GCM");
        algorithmParameters.init(paramsBytes);
        AESGCMCipherText aesgcmCipherText1 = new AESGCMCipherText(encBytes, algorithmParameters);
        AESGCMCipherText aesgcmCipherText2 = new AESGCMCipherText(aesgcmCipherText1.json);

        assertEquals(aesgcmCipherText1.json, aesgcmCipherText2.json);
        assertArrayEquals(aesgcmCipherText1.encryptionBytes, aesgcmCipherText2.encryptionBytes);
        assertArrayEquals(
                aesgcmCipherText1.algorithmParameters.getEncoded(),
                aesgcmCipherText2.algorithmParameters.getEncoded()
        );
    }

    private byte[] encBytes = new byte[]{
            46, 56, -101, -103, 76, -75, -126, -100, 53, -123, 113, 17, -91, 82, -79, 126, -113, 119, -35, -35, 98, 56, -24, -79, 121, -95, -2, 58
    };

    private byte[] paramsBytes = new byte[] {
            48, 17, 4, 12, 108, -54, 100, -121, -119, 121, -127, 23, 78, 16, 84, 84, 2, 1, 16
    };

}