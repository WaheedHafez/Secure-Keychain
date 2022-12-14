package keychain.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoServiceTest {

    //****** Test hash() *********//
    @Test
    void hashSameMessageTwice() {
        var password = "pw123456";
        CryptoService cryptoService = new CryptoService(password.toCharArray());

        var plainText = "CryptoServiceTest";

        String hash1 = cryptoService.hash(plainText);
        String hash2 = cryptoService.hash(plainText);

        assertEquals(hash1, hash2);
    }

    @Test
    void hashDifferentMessagesOutputsDifferentHashes() {
        var password = "pw123456";
        CryptoService cryptoService = new CryptoService(password.toCharArray());

        var plainText1 = "plainText1";
        var plainText2 = "plainText2";

        String hash1 = cryptoService.hash(plainText1);
        String hash2 = cryptoService.hash(plainText2);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void hashesTo() {
        var password = "pw123456";
        CryptoService cryptoService = new CryptoService(password.toCharArray());

        var plainText = "plainText";

        String hash = cryptoService.hash(plainText);

        assertTrue(cryptoService.hashesTo(plainText, hash));

        var plainText1 = "plainText1";
        assertFalse(cryptoService.hashesTo(plainText1, hash));
    }

    //****** Test encrypt() *********//
    @Test
    void encrypt() {
        var password = "pw123456";
        CryptoService cryptoService = new CryptoService(password.toCharArray());
        var plainTest = "password1234";

        String cipherText = cryptoService.encrypt(plainTest);

        assertTrue(cipherText.length() > 0);
    }

    @Test
    void encryptThenDecrypt() {
        var password = "pw123456";
        CryptoService cryptoService = new CryptoService(password.toCharArray());
        var plainText = "password1234";

        assertEquals(
                plainText,
                cryptoService.decrypt(cryptoService.encrypt(plainText)));
    }


    @Test
    void encryptThenDecrypt_DifferentCryptoServices_butSamePasswordAndSameSalt() {
        var password = "pw123456".toCharArray();
        CryptoService cryptoService1 = new CryptoService(password);
        byte[] salt = cryptoService1.getSalt();
        CryptoService cryptoService2 = new CryptoService(password, salt);

        var plainText = "password1234";

        String cipherText1 = cryptoService1.encrypt(plainText);
        String cipherText2 = cryptoService2.encrypt(plainText);

        assertNotEquals(cipherText1, cipherText2);

        assertEquals(plainText, cryptoService1.decrypt(cipherText1));
        assertEquals(plainText, cryptoService2.decrypt(cipherText2));
    }

    @Test
    void encryptWithOneCryptoServiceThenDecryptWithAnotherCryptoService() {
        var password = "pw123456".toCharArray();
        CryptoService cryptoService1 = new CryptoService(password);
        CryptoService cryptoService2 = new CryptoService(password);

        var plainText = "password1234";

        String cipherText1 = cryptoService1.encrypt(plainText);
        try {
            cryptoService2.decrypt(cipherText1);
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //****** Test mac() *********//
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