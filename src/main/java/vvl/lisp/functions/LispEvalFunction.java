package vvl.lisp.functions;

import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.lisp.LispParams;

@FunctionalInterface
public interface LispEvalFunction {
    LispItem apply(LispParams items) throws LispError;
}
