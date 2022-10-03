package keychain.secretkeys;

public class CryptoService {

    private final byte[] salt;
    private final MacService macService;

    public CryptoService(char[] password) {
        SessionKeys sessionKeys = new SessionKeys(password);
        this.salt = sessionKeys.getSalt();
        this.macService = new MacService(sessionKeys.macKey());
    }

    public CryptoService(char[] password, byte[] salt) {
        SessionKeys sessionKeys = new SessionKeys(password, salt);
        this.salt = sessionKeys.getSalt();
        this.macService = new MacService(sessionKeys.macKey());
    }

    public String mac(String plainText) {
        return macService.mac(plainText);

    }

    public byte[] getSalt() {
        return salt;
    }

}
