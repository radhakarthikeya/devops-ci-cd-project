package com.feedback;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class BrokenDemoTest {
    @Test
    void intentionallyFails() {
        fail("Demo: deliberately broken test");
    }
}
