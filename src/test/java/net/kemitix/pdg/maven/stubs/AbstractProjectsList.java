package net.kemitix.pdg.maven.stubs;

import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * List of {@link DigraphProjectStub}s.
 *
 * @author pcampbell
 */
public abstract class AbstractProjectsList implements List<MavenProject> {

    private final List<MavenProject> list = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param projectStub the project stub
     */
    public AbstractProjectsList(final DigraphProjectStub projectStub) {
        projectStub.init();
        list.add(projectStub);
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
    public boolean contains(final Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<MavenProject> iterator() {
        return list.iterator();
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
    public boolean add(final MavenProject e) {
        return list.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends MavenProject> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(
            final int index, final Collection<? extends MavenProject> c
                         ) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void replaceAll(final UnaryOperator<MavenProject> operator) {
        list.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super MavenProject> c) {
        list.sort(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public MavenProject get(final int index) {
        return list.get(index);
    }

    @Override
    public MavenProject set(final int index, final MavenProject element) {
        return list.set(index, element);
    }

    @Override
    public void add(final int index, final MavenProject element) {
        list.add(index, element);
    }

    @Override
    public MavenProject remove(final int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<MavenProject> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<MavenProject> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    public List<MavenProject> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<MavenProject> spliterator() {
        return list.spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super MavenProject> filter) {
        return list.removeIf(filter);
    }

    @Override
    public Stream<MavenProject> stream() {
        return list.stream();
    }

    @Override
    public Stream<MavenProject> parallelStream() {
        return list.parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super MavenProject> action) {
        list.forEach(action);
    }

}
