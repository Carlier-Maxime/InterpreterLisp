package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.lisp.pairs.LispParams;

@FunctionalInterface
public interface LispEvalFunction {
    LispItem apply(@NotNull LispParams params) throws LispError;
}
