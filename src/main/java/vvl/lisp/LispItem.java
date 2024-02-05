package vvl.lisp;

import vvl.util.ConsList;

/**
 * A marker class for representing a lisp value.
 * 
 * @author leberre
 *
 */
public interface LispItem {
    LispItem eval(ConsList<LispItem> items) throws LispError;
}
