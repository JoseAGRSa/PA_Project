package pa.tests;

import pa.tests.domain.Identify;

public class TestE {
    public static void main(String[] args) {
        Object objects = new Object[] { 123, "Foo", 1.2};
        System.out.println(Identify.it(objects));
    }
}
