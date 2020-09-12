import model.businesslogic.Cook;
import model.services.CookALotDataManagerStub;
import model.services.CookALotServiceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CookTest {

    private static Cook instance;
    private static CookALotDataManagerStub cookDM;

    @BeforeAll
    static void globalSetup(){
        cookDM = (CookALotDataManagerStub) CookALotServiceProvider.getInstance().getDataManager();
    }

    @BeforeEach
    void setup(){
        instance = new Cook("cook_test", cookDM.generateCookId());
        cookDM.reset();
    }

    @AfterEach
    void tearDown(){
        instance = null;
    }

    @Test
    void setName(){
        String name = "name1";
        instance.setName(name);
        assertNotNull(instance);
        assertEquals(name, instance.getName());
    }

    @Test
    void getName(){
        String name = instance.getName();
        assertNotNull(name);
        assertEquals("cook_test", name);
    }

    @Test
    void setId(){
        String id = "id1";
        instance.setId(id);
        assertNotNull(instance);
        assertEquals(id, instance.getId());
    }

    @Test
    void getId(){
        String id = instance.getId();
        assertNotNull(id);
    }
}