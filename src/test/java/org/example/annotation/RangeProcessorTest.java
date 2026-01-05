package org.example.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RangeProcessorTest {

   @Test
   public void test() throws Exception{
        Parent parent = new Parent("");
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            RangeProcessor.process(parent);
        });
    }
}

class Parent {
    @Range(min = 1, max = 10)
    public String name;

    Parent(String name) {
        this.name = name;
    }
}
