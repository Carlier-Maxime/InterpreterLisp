package vvl.lisp;

public interface LispPair extends LispItem {
    LispItem car();
    LispItem cdr();
}
