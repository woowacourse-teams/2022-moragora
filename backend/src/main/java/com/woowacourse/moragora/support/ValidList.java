package com.woowacourse.moragora.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.validation.Valid;

/**
 * 컨트롤러의 DTO List로 이루어진 파라미터를 대상으로 Bean Validation 을 적용하기 위해 만든 클래스입니다.
 * <p/>
 * 사용 예시:
 * <pre>
 * @RequestBody @Valid final ValidList<BeaconRequest> requestBody
 * </pre>
 */
public class ValidList<E> implements List<E> {

    @Valid
    private List<E> list;

    public ValidList() {
        this.list = new ArrayList<>();
    }

    public static <E> ValidList<E> of(final E... es) {
        final ValidList<E> validList = new ValidList<>();
        for (final E e : es) {
            validList.add(e);
        }
        return validList;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(final List<E> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public E get(final int index) {
        return list.get(index);
    }

    @Override
    public E set(final int index, final E element) {
        return list.set(index, element);
    }

    @Override
    public boolean add(final E e) {
        return list.add(e);
    }

    @Override
    public void add(final int index, final E element) {
        list.add(index, element);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean contains(final Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E remove(final int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
