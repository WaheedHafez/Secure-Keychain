package keychain.crypto;

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

    @Test
    void testGivenTheSalt() {
        var password = "password123!".toCharArray();
        SessionKeys sessionKeysWithoutSalt = new SessionKeys(password);

        SessionKeys sessionKeysGivenSalt = new SessionKeys(
                password,
                sessionKeysWithoutSalt.getSalt()
        );

        assertEquals(
                sessionKeysWithoutSalt.encryptionKey(),
                sessionKeysGivenSalt.encryptionKey()
        );

        assertEquals(
                sessionKeysWithoutSalt.macKey(),
                sessionKeysGivenSalt.macKey()
        );

    }
}