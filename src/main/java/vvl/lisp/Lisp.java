package vvl.lisp;

import org.jetbrains.annotations.NotNull;

/**
 * A simple abstraction for a lisp interpreter.
 * 
 * @author leberre
 *
 */
public interface Lisp {

    /**
     * Parse a textual lisp expression and cut it into operator and operands for
     * further evaluation.
     * 
     * @param expr expression
     * @return a single lisp element or a lisp expression
     * @throws LispError
     *             if the expression is not a valid Lisp/scheme expression
     */
    @NotNull LispItem parse(@NotNull String expr) throws LispError;

    /**
     * Evaluate the expression returned by the {@link #parse(String)} method.
     * 
     * @param ex
     *            the result of the {@link #parse(String)} method
     * @return a lisp evaluation of the parameter
     * @throws LispError
     *             if the expression cannot be evaluated
     */
    @NotNull LispItem evaluate(@NotNull LispItem ex) throws LispError;

    /**
     * Evaluate a lisp expression.
     * 
     * @param expr
     *            a lisp expression
     * @return a
     * @throws LispError
     *             if the expression is malformed or cannot be evaluated.
     */
    default @NotNull LispItem eval(@NotNull String expr) throws LispError {
        return evaluate(parse(expr));
    }
    
    /**
     * Create a new instance of the interpreter.
     * 
     * @return a new lisp interpreter.
     */
    static @NotNull Lisp makeInterpreter() {
    	return new LispImpl();
    }
}
