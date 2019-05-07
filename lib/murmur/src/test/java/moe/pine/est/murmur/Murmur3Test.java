package moe.pine.est.murmur;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Murmur3Test {
    private static final long MURMUR3_SEED = 0L;

    private Murmur3 murmur3;

    @Before
    public void setUp() {
        murmur3 = new Murmur3(MURMUR3_SEED);
    }

    @Test
    public void hash128Test() {
        assertEquals("b4963f3f3fad78673ba2744126ca2d52", murmur3.hash128("abc"));
    }
}
