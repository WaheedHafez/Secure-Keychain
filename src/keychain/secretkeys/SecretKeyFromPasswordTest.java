package keychain.secretkeys;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecretKeyFromPasswordTest {

    @Test
    void createdSuccessfully() {
        String password = "password123!";
        var secretKeyFromPassword = new SecretKeyFromPassword(password.toCharArray());
        assertNotNull(secretKeyFromPassword.dSecretKey);
    }

}