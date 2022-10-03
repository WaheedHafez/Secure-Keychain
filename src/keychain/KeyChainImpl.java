package keychain;

import keychain.secretkeys.CryptoService;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

class KeyChainImpl implements KeyChain {

    private final Map<String, String> kvs = new HashMap<>();

    private final CryptoService cryptoService;

    public KeyChainImpl(char[] password) {
        this.cryptoService = new CryptoService(password);
        kvs.put("salt", Base64.getEncoder().encodeToString(cryptoService.getSalt()));
    }

    public KeyChainImpl(char[] password, String repr) {
        JSONObject jsonObject = new JSONObject(repr);
        Map<String, Object> jsonMap = jsonObject.toMap();
        for (var entry : jsonMap.entrySet()) {
            kvs.put(entry.getKey(), (String) entry.getValue());
        }

        String saltEncode = kvs.get("salt");
        byte[] salt = Base64.getDecoder().decode(saltEncode);
        this.cryptoService = new CryptoService(password, salt);
    }

    @Override
    public String[] dump() {
        JSONObject jsonObject = new JSONObject(kvs);
        String[] dump = new String[2];
        dump[0] = jsonObject.toString();
        return dump;
    }

    @Override
    public void set(String name, String value) {
        String nameHash = cryptoService.mac(name);
        kvs.put(nameHash, value);
    }

    @Override
    public String get(String name) {
        String nameHash = cryptoService.mac(name);
        return kvs.get(nameHash);
    }

    @Override
    public boolean remove(String name) {
        String nameHash = cryptoService.mac(name);
        return kvs.remove(nameHash) != null;
    }
}
