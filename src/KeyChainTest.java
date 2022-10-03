import keychain.KeyChain;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KeyChainTest {

    private static final String password = "password123!";
    private static final Map<String, String> kvs = new HashMap<>();

    static {
        kvs.put("service1", "value1");
        kvs.put("service2", "value2");
        kvs.put("service3", "value3");
    }

    @Test
    void inits_without_an_error() {
        KeyChain.init(password);
    }

    @Test
    void can_set_and_retrieve_a_password() {
        // given
        KeyChain keyChain = KeyChain.init(password);
        var url = "www.stanford.edu";
        var pw = "sunetpassword";

        // when
        keyChain.set(url, pw);

        // then
        assertEquals(pw, keyChain.get(url));
    }

    @Test
    void can_set_and_retrieve_multiple_passwords() {
        // given
        KeyChain keyChain = KeyChain.init(password);
        for (var entry : kvs.entrySet()) {
            keyChain.set(entry.getKey(), entry.getValue());
        }

        // then
        for (var k : kvs.keySet()) {
            assertEquals(kvs.get(k), keyChain.get(k));
        }
    }

    @Test
    void returns_null_for_non_existent_passwords() {
        // given
        KeyChain keyChain = KeyChain.init(password);
        for (var entry : kvs.entrySet()) {
            keyChain.set(entry.getKey(), entry.getValue());
        }

        // then
        assertNull(keyChain.get("www.stanford.edu"));
    }

    @Test
    void can_remove_a_password() {
        // given
        KeyChain keyChain = KeyChain.init(password);
        for (var entry : kvs.entrySet()) {
            keyChain.set(entry.getKey(), entry.getValue());
        }

        // then
        var name = "service1";
        assertNotNull(keyChain.get(name));
        assertTrue(keyChain.remove(name));
        assertNull(keyChain.get(name));
    }

    @Test
    void returns_false_if_there_is_no_password_for_the_domain_being_removed() {
        // given
        KeyChain keyChain = KeyChain.init(password);
        for (var entry : kvs.entrySet()) {
            keyChain.set(entry.getKey(), entry.getValue());
        }

        // then
        var name = "www.stanford.edu";
        assertFalse(keyChain.remove(name));
    }

    //********** Test dump and restore *********//

    @Test
    void can_dump_and_restore_the_database() {
        // given
        KeyChain keyChain = KeyChain.init(password);
        for (var entry : kvs.entrySet()) {
            keyChain.set(entry.getKey(), entry.getValue());
        }

        // when
        String[] dump = keyChain.dump();
        String contents = dump[0];
        String checksum = dump[1];

        KeyChain newKeyChain = KeyChain.load(password, contents, checksum);

        // then
        assertDoesNotThrow(() -> {
            new JSONObject(contents);
        });

        for (var k : kvs.keySet()) {
            assertEquals(kvs.get(k), newKeyChain.get(k));
        }
    }

    //********* Test security *******************//

    @Test
    void does_not_store_domain_names_and_passwords_in_the_clear() {
        // given
        KeyChain keyChain = KeyChain.init(password);
        var url = "www.stanford.edu";
        var pw = "sunetpassword";
        keyChain.set(url, pw);

        // when
        String[] dump = keyChain.dump();
        String contents = dump[0];

        // then
        assertFalse(contents.contains(password));
        assertFalse(contents.contains(url));
        assertFalse(contents.contains(pw));
    }

    //*********** setup and teardown ***********//

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

}