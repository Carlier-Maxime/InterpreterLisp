package vvl.lisp.pairs;

import vvl.lisp.LispItem;

public interface LispPair extends LispItem {
    LispItem car();
    LispItem cdr();
}
