package net.kemitix.pdg.maven.stubs;

import lombok.experimental.Delegate;
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

abstract class AbstractProjectsList implements List<MavenProject> {

    @Delegate
    private final List<MavenProject> list = new ArrayList<>();

    AbstractProjectsList(final DigraphProjectStub projectStub) {
        projectStub.init();
        list.add(projectStub);
    }

}
