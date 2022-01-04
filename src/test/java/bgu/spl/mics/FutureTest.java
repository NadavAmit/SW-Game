package bgu.spl.mics;

import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;
/*+===========================================================
 * UPDATED TESTS!
 * future.get method
 * Timeout
 * ===========================================================
 * */

public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp() { future = new Future<>(); }

    @Test
    public void testResolve(){                                             //command
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        //assertTrue(str.equals(future.get()));
    }


    //WE SHOULD CHECK IF IT RETURNS A PROPER VALUE AFTER RESOLVING   v
    @Test
    public void testGet1_1() {                                               //query
        future.resolve("resolved");
        //assertEquals("resolved",future.get());
    }


    //WE SHOULD CHECK IF ISDONE METHOD CHANGES STATUS WHEN WE RESOLVING  v
    @Test
    public void testIsDone() {                                                //query
        assertFalse(future.isDone());      //BEFORE RESOLVING
        future.resolve("I am resolving");
        assertTrue(future.isDone());     //AFTER RESOLVING
    }


    //WE SHOULD CHECK IF IT RETURNS A PROPER VALUE AFTER RESOLVING  v
    @Test
    public void testGet2_1() throws InterruptedException {                                                  //query
        future.resolve("resolved");
        assertEquals("resolved",future.get());
    }

    //WE SHOULD CHECK IF IT RETURNS NULL, IF FUTURE DID NOT RESOLVE   v
    @Test
    public void testGet2_2(){
        //NOT RESOLVING
       Object result =  future.get(1000, TimeUnit.MILLISECONDS);
       assertEquals(result, null);
    }

    //WE SHOULD CHECK IF IT WAITS A UNITS TIME EQUAL TO TIMEOUT   v
   @Test
   @Timeout(value = 1001, unit = TimeUnit.MILLISECONDS)
   void infinity() {
        // fails if execution time exceeds 1000 milliseconds
        //NOT RESOLVING
        future.get(1000,TimeUnit.MILLISECONDS);
    }

    //The @Timeout annotation allows one to declare that a test, test factory, test template,
    // or lifecycle method should fail if its execution time exceeds a given duration.
    // The time unit for the duration defaults to seconds but is configurable.
}
