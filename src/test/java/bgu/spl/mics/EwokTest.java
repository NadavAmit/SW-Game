package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Ewok;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
/*+===========================================================
* UPDATED TESTS!
* testAcquire
* testRelease
* ===========================================================
* */
class EwokTest {

    private Ewok ewok;

    @BeforeEach
    void setUp() {
        ewok = new Ewok(1);
    }


    //CHECK DEFAULT STATUS          v
    //WE SHOULD CHECK IF IT CHANGES STATUS AFTER AQUIRE OPERATION        v
    @Test
    void testAcquire() {
        assertTrue(ewok.isAvailable());
        ewok.acquire();
        assertFalse(ewok.isAvailable());
    }

    //WE SHOULD CHECK IF IT CHANGES STATUS           v
    @Test
    void testRelease() {
        ewok.acquire();
        assertFalse(ewok.isAvailable());
        ewok.release();
        assertTrue(ewok.isAvailable());
    }

}