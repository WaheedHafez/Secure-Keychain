package keychain;

import keychain.crypto.CryptoService;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

class KeyChainImpl implements KeyChain {

    private final Map<String, String> kvs = new HashMap<>();

    private final CryptoService cryptoService;

    private final JSONObject json;

    public KeyChainImpl(char[] password) {
        this.cryptoService = new CryptoService(password);
        this.json = new JSONObject();

        json.put("salt", HexFormat.of().formatHex(cryptoService.getSalt()));
    }

    public KeyChainImpl(char[] password, String repr) {
        this.json = new JSONObject(repr);

        String saltEncoded = (String) json.get("salt");;
        byte[] salt = HexFormat.of().parseHex(saltEncoded);
        this.cryptoService = new CryptoService(password, salt);

        JSONObject kvsJson = json.getJSONObject("kvs");
        Map<String, Object> jsonMap = kvsJson.toMap();

        for (var entry : jsonMap.entrySet()) {
            this.kvs.put(entry.getKey(), (String) entry.getValue());
        }
    }

    @Override
    public String[] dump() {
        json.put("kvs", new JSONObject(kvs));
        String[] dump = new String[2];
        dump[0] = json.toString();
        return dump;
    }

    @Override
    public void set(String name, String value) {
        String nameHash = cryptoService.mac(name);
        String valueEncrypted = cryptoService.encrypt(value);
        kvs.put(nameHash, valueEncrypted);
    }

    @Override
    public String get(String name) {
        String nameHash = cryptoService.mac(name);
        String encryptedValue = kvs.get(nameHash);
        return encryptedValue != null ? cryptoService.decrypt(encryptedValue) : null;
    }

    @Override
    public boolean remove(String name) {
        String nameHash = cryptoService.mac(name);
        return kvs.remove(nameHash) != null;
    }
}
