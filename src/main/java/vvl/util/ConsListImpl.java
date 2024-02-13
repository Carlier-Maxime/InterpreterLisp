package vvl.util;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class ConsListImpl<E> implements ConsList<E>, Iterable<E> {
    private final Cons<E, ConsList<E>> list;

    public ConsListImpl() {
        this(null, null);
    }

    public ConsListImpl(E first) {
        this(first, null);
    }

    private ConsListImpl(E first, ConsList<E> list) {
        if (list==null && first!=null) this.list = new Cons<>(first, ConsList.nil());
        else this.list = new Cons<>(first, list);
    }

    @Override
    @NotNull
    public ConsList<E> prepend(E e) {
        return new ConsListImpl<>(e, this);
    }

    @Override
    @NotNull
    public ConsList<E> append(E e) {
        ConsList<E> l = new ConsListImpl<>(e);
        E[] elems = this.toArrayTyped();
        for (int i=elems.length-1; i>=0; i--) l=l.prepend(elems[i]);
        return l;
    }

    @Override
    public boolean isEmpty() {
        return list.left()==null && list.right()==null;
    }

    @Override
    public E car() {
        if (isEmpty()) throw new NoSuchElementException();
        return list.left();
    }

    @Override
    @NotNull
    public ConsList<E> cdr() {
        ConsList<E> l = list.right();
        return l==null ? ConsList.nil() : l;
    }

    @Override
    public int size() {
        if (isEmpty()) return 0;
        return (list.right()==null) ? 1 : list.right().size()+1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsListImpl<?> consList = (ConsListImpl<?>) o;
        return list.equals(consList.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("(");
        var empty = true;
        for (E e : this) {
            if (!empty) sb.append(' ');
            else empty=false;
            sb.append(e);
        }
        sb.append(')');
        return sb.toString();
    }
}
