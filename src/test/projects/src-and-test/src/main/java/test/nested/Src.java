package test.nested;

import test.other.Imported;

import static test.other.Static.method;
import static test.other.StaticAll.alpha;
import static test.other.StaticAll.beta;

public class Src {

    Imported imported;

    public void test() {
        method(imported);
        alpha();
        beta();
    }

}
