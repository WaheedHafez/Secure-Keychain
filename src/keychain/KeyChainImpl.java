package keychain;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class KeyChainImpl implements KeyChain {

    private final Map<String, String> kvs = new HashMap<>();

    public KeyChainImpl() {

    }

    public KeyChainImpl(String repr) {
        JSONObject jsonObject = new JSONObject(repr);
        Map<String, Object> jsonMap = jsonObject.toMap();
        for (var entry : jsonMap.entrySet()) {
            kvs.put(entry.getKey(), (String) entry.getValue());
        }
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
        kvs.put(name, value);
    }

    @Override
    public String get(String name) {
        return kvs.get(name);
    }

    @Override
    public boolean remove(String name) {
        return kvs.remove(name) != null;
    }
}
