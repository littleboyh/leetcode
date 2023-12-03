package com.hqhe;


import java.util.ArrayList;
import java.util.List;

public class SpanCacheKey {
    private String key;

    public SpanCacheKey(String key) {
        this.key = key;
    }

    public List<TraceSegment> createValue() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "SpanCacheKey{" +
                "key='" + key + '\'' +
                '}';
    }
}
