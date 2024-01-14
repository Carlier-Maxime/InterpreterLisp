package vvl.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public class ConsListImpl<E> implements ConsList<E>, Iterable<E> {
    private final Cons<E, ConsList<E>> list;

    public ConsListImpl() {
        this(null, null);
    }

    public ConsListImpl(E first) {
        this(first, null);
    }

    private ConsListImpl(E first, ConsList<E> list) {
        this.list = new Cons<>(first, list);
    }

    @Override
    public ConsList<E> prepend(E e) {
        return new ConsListImpl<>(e, this);
    }

    @Override
    public ConsList<E> append(E e) {
        ConsList<E> l = new ConsListImpl<>(e);
        E[] elems = this.toArrayTyped();
        for (int i=elems.length-1; i>=0; i--) l.prepend(elems[i]);
        return null;
    }

    @Override
    public boolean isEmpty() {
        return car()==null && cdr()==null;
    }

    @Override
    public E car() {
        return list.left();
    }

    @Override
    public ConsList<E> cdr() {
        return list.right();
    }

    @Override
    public int size() {
        return (isEmpty()) ? 0 : list.right().size()+1;
    }

    @Override
    public <T> ConsList<T> map(Function<E, T> f) {
        if (isEmpty()) return ConsList.nil();
        if (list.right()==null) return new ConsListImpl<>(f.apply(list.left()), null);
        return new ConsListImpl<>(f.apply(list.left()), list.right().map(f));
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        final ConsList<E> consList = this;
        return new Iterator<>() {
            private ConsList<E> list = consList;
            @Override
            public boolean hasNext() {
                return list != null && !list.isEmpty();
            }

            @Override
            public E next() {
                E e = list.car();
                list = list.cdr();
                return e;
            }
        };
    }
}
