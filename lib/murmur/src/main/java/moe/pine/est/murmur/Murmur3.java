package moe.pine.est.murmur;

import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class Murmur3 {
    private final long seed;

    public String hash128(String str) {
        final byte[] data = str.getBytes(StandardCharsets.UTF_8);
        final long[] hashes =
                com.sangupta.murmur.Murmur3.hash_x64_128(data, data.length, seed);
        return String.format("%016x%016x", hashes[0], hashes[1]);
    }
}
