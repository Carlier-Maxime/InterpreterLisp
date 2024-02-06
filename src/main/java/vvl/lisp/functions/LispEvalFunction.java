package vvl.lisp.functions;

import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.lisp.pairs.LispParams;

@FunctionalInterface
public interface LispEvalFunction {
    LispItem apply(LispParams params) throws LispError;
}
