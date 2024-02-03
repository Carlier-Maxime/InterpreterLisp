package vvl.lisp.functions;

import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.util.ConsList;

@FunctionalInterface
public interface LispEvalFunction {
    LispItem apply(ConsList<LispItem> items) throws LispError;
}
