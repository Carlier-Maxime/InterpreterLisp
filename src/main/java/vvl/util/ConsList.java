package vvl.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Implementation of a list using the {@link vvl.util.Cons} data structure.
 * <br><br>
 * The implementation of ConsList must be immutable, i.e. each call to the
 * {@link #append(Object)} or {@link #prepend(Object)} methods must return a new
 * list without changing the state of the current list. This is unlike the
 * default behavior of {@link java.util.List} behavior.
 * 
 * @author leberre
 *
 * @param <E> the type of the elements in the list
 */
public interface ConsList<E> extends Iterable<E> {

	/**
	 * Insert a new element e in front of the list.
	 * 
	 * @param e an element.
	 * @return a new list containing e in front of the current one.
	 */
	@NotNull ConsList<E> prepend(E e);

	/**
	 * Insert a new element e at the end of the list
	 * 
	 * @param e an element
	 * @return a new list containing e at the end of the current one.
	 */
	@NotNull ConsList<E> append(E e);

	/**
	 * Check if the list is empty or not.
	 * 
	 * @return true if the list is empty, else false.
	 */
	boolean isEmpty();

	/**
	 * Retrieve the first element of the list.
	 * 
	 * @return the first element of the list.
	 */
	E car();

	/**
	 * Return the sublist corresponding to all but the first element.
	 * 
	 * @return all but the first element of the list.
	 */
	@NotNull ConsList<E> cdr();

	/**
	 * Returns the size of the list (the number of elements it contains).
	 * 
	 * @return the number of elements in the list.
	 */
	int size();

	/**
	 * Create a new list by applying a function to each element of the list.
	 * 
	 * @param f a function
	 * @return a list where each element is the result of applying f to an element
	 *         of the original list.
	 */
	default @NotNull <T> ConsList<T> map(@NotNull Function<E, T> f) {
		if (isEmpty()) return ConsList.nil();
		return cdr().map(f).prepend(f.apply(car()));
	}

	/**
	 * Performs a reduction on the elements of this list, using the provided
	 * identity value and an associative accumulation function, and returns the
	 * reduced value.
	 * 
	 * @param identity    the identity value for the accumulating function
	 * @param accumulator an associative, stateless function for combining two
	 *                    values
	 * @return the result of the reduction
	 */
	default E reduce(E identity, @NotNull BinaryOperator<E> accumulator) {
		E result = identity;
		for (E element : this) result = accumulator.apply(result, element);
		return result;
	}

	/**
	 * Translates the ConsList as an array of Object. The type of the
	 * 
	 * @return all the elements of the list in an array of objects
	 */
	default Object[] toArray() {
		var array = new Object[size()];
		int i = 0;
		for (Object o : this) array[i++] = o;
		return array;
	}

	default E[] toArrayTyped() {
		@SuppressWarnings("unchecked")
		E[] array = (E[]) toArray();
		return array;
	}

	@NotNull
	default Iterator<E> iterator() {
		final var consList = this;
		return new Iterator<>() {
			private ConsList<E> list = consList;
			@Override
			public boolean hasNext() {
				return list != null && !list.isEmpty();
			}

			@Override
			public E next() {
				if (!hasNext()) throw new NoSuchElementException();
				E e = list.car();
				list = list.cdr();
				return e;
			}
		};
	}

	/**
	 * Create a new empty list.
	 * 
	 * @return an empty list
	 */
	static @NotNull <T> ConsList<T> nil() {
		return new ConsListImpl<>();
	}

	/**
	 * Create a new list containing a single element
	 * 
	 * @param t an object
	 * @return a list containing only t
	 */
	static @NotNull <T> ConsList<T> singleton(T t) {
		return new ConsListImpl<>(t);
	}

	/**
	 * Create a new list containing the elements given in parameter
	 * 
	 * @param ts a variable number of elements
	 * @return a list containing those elements
	 */
	@SafeVarargs
	static @NotNull <T> ConsList<T> asList(T... ts) {
		ConsList<T> list = nil();
		for (int i = ts.length - 1; i >= 0; i--) list = list.prepend(ts[i]);
		return list;
	}
}
