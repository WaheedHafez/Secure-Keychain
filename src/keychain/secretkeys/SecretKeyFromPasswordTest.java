package keychain.secretkeys;

import org.junit.jupiter.api.Test;

import javax.crypto.interfaces.PBEKey;

import static org.junit.jupiter.api.Assertions.*;

class SecretKeyFromPasswordTest {

    @Test
    void createdSuccessfully() {
        String password = "password123!";
        var secretKeyFromPassword = new SecretKeyFromPassword(password.toCharArray());
        assertNotNull(secretKeyFromPassword.dSecretKey);
    }
    
    @Test
    void saltIsGiven() {
        // salt is no given
        String password = "password123!";
        var secretKeyFromPassword = new SecretKeyFromPassword(password.toCharArray());
        PBEKey dSecretKey = secretKeyFromPassword.dSecretKey;
        
        // test the same key is generated given the salt
        testCanGenerateSameKeyGivenTheSalt(password, dSecretKey, dSecretKey.getSalt());
    }

    private void testCanGenerateSameKeyGivenTheSalt(String password, PBEKey dSecretKey, byte[] salt) {
        SecretKeyFromPassword secretKeyGivenSalt = new SecretKeyFromPassword(password.toCharArray(), salt);
        assertNotSame(dSecretKey, secretKeyGivenSalt.dSecretKey);
        assertEquals(dSecretKey, secretKeyGivenSalt.dSecretKey);
    }

}