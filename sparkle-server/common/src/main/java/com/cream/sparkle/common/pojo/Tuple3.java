package com.cream.sparkle.common.pojo;

import java.util.Objects;

public class Tuple3<A, B, C> {
    public final A first;
    public final B second;
    public final C third;

    public Tuple3(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        return Objects.equals(first, tuple3.first) && Objects.equals(second, tuple3.second) && Objects.equals(third, tuple3.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
