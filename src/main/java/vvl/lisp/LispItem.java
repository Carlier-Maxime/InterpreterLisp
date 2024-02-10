package vvl.lisp;

import org.jetbrains.annotations.NotNull;

/**
 * A marker class for representing a lisp value.
 * 
 * @author leberre
 *
 */
public interface LispItem {
    LispItem eval(@NotNull LispContext context) throws LispError;
}
