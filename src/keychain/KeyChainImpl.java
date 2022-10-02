package keychain;

import java.util.HashMap;
import java.util.Map;

class KeyChainImpl implements KeyChain {

    private final Map<String, String> kvs = new HashMap<>();

    @Override
    public String[] dump() {
        throw new RuntimeException("TODO");
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
