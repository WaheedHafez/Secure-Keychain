package keychain.secretkeys;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoServiceTest {

    @Test
    void mac() {
        var password = "pw123456";
        CryptoService cryptoService = new CryptoService(password.toCharArray());
        var plainText = "www.google.com";

        String hash = cryptoService.mac(plainText);

        assertTrue(hash.length() > 0);
    }

    @Test
    void macWithSamePasswordButDifferentSalts() {
        var password = "pw123456".toCharArray();
        CryptoService cryptoService1 = new CryptoService(password);
        CryptoService cryptoService2 = new CryptoService(password);

        var plainText = "www.google.com";

        assertNotEquals(
                cryptoService1.mac(plainText),
                cryptoService2.mac(plainText)
        );
    }

    @Test
    void macWithSamePasswordAndSameSalt() {
        var password = "pw123456".toCharArray();
        CryptoService cryptoService1 = new CryptoService(password);
        byte[] salt = cryptoService1.getSalt();
        CryptoService cryptoService2 = new CryptoService(password, salt);

        var plainText = "www.google.com";

        assertEquals(
                cryptoService1.mac(plainText),
                cryptoService2.mac(plainText)
        );
    }
}