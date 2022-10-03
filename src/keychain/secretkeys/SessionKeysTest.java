package keychain.secretkeys;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class SessionKeysTest {

    @Test
    void test() {
        var password = "password123!";
        SessionKeys sessionKeys = new SessionKeys(password.toCharArray());

        SecretKey encKey = sessionKeys.encryptionKey();
        SecretKey authKey = sessionKeys.macKey();

        assertNotEquals(encKey, authKey);
    }
}