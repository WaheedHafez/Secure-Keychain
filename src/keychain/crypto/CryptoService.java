package keychain.crypto;

public class CryptoService {

    private final byte[] salt;
    private final MacService macService;
    private final EncryptionService encryptionService;
    private final HashService hashService;

    public CryptoService(char[] password) {
        SessionKeys sessionKeys = new SessionKeys(password);
        this.salt = sessionKeys.getSalt();
        this.macService = new MacService(sessionKeys.macKey());
        this.encryptionService = new EncryptionService(sessionKeys.encryptionKey());
        hashService = new HashService();
    }

    public CryptoService(char[] password, byte[] salt) {
        SessionKeys sessionKeys = new SessionKeys(password, salt);
        this.salt = sessionKeys.getSalt();
        this.macService = new MacService(sessionKeys.macKey());
        this.encryptionService = new EncryptionService(sessionKeys.encryptionKey());
        hashService = new HashService();
    }

    public String mac(String plainText) {
        return macService.mac(plainText);

    }

    public byte[] getSalt() {
        return salt;
    }

    public String encrypt(String plainText) {
        return encryptionService.encrypt(plainText);
    }

    public String decrypt(String cipherText) {
        return encryptionService.decrypt(cipherText);
    }

    public String hash(String plainText) {
        return hashService.hash(plainText);
    }

    public boolean hashesTo(String plainText, String givenHash) {
        String computedHash = hashService.hash(plainText);
        return computedHash.equals(givenHash);
    }
}
