package org.example.annoymousclass;

import org.junit.jupiter.api.Test;

public class AnnoyClassTest {
    @Test
    public void test() {
       AnnoyClass annoyClass = new AnnoyClass();
       // [org.example.annoymousclass.AnnoyClass$1] works. varString:Hello, count: 0
       //[org.example.annoymousclass.AnnoyClass$1] works. varString:Hello, count: 1
       annoyClass.doWork();
    }
}
