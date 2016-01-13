package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link PackageData}.
 *
 * @author pcampbell
 */
public class PackageDataTest {

    /**
     * Class under test.
     */
    private PackageData data;

    private String name;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        name = "name";
        data = new PackageData(name);
    }

    /**
     * Name is what was set.
     */
    @Test
    public void shouldGetName() {
        assertThat(data.getName(), is(name));
    }

    /**
     * Two objects with different names have different hashcodes.
     */
    @Test
    public void shouldCreateUniqueHashCode() {
        //given
        String name2 = "name2";
        PackageData data2 = new PackageData(name2);
        //then
        assertThat(data.hashCode(), is(not(data2.hashCode())));
    }

    /**
     * Two objects with the same name are equal.
     */
    @Test
    public void shouldBeEqualWhenSameName() {
        //given
        PackageData data2 = new PackageData(name);
        //then
        assertThat(data, is(data2));
    }

    /**
     * When an object is in a Set, then another object with the same name is
     * considered to be in the Set.
     */
    @Test
    public void shouldBeInSetIfNameMatches() {
        //given
        final PackageData data2 = new PackageData(name);
        final Set<PackageData> set = new HashSet<>();
        //when
        set.add(data);
        //then
        assertThat(set, hasItem(data2));
    }

}
