package keychain.secretkeys;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KeyExpansionTest {

    @Test
    void expand() {
        SecretKey secretKey = new SecretKeyFromPassword("password1234!".toCharArray()).dSecretKey;
        int n = 5;
        SecretKey[] secretKeys = KeyExpansion.expand(secretKey, n);
        assertEquals(n, secretKeys.length);

        for (SecretKey k : secretKeys) {
            assertNotNull(k);
        }
    }
}