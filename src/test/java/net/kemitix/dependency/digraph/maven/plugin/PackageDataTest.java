package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

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
        data = PackageData.newInstance(name);
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
        val data2 = PackageData.newInstance(name2);
        //then
        assertThat(data.hashCode(), is(not(data2.hashCode())));
    }

    /**
     * Two objects with the same name are equal.
     */
    @Test
    public void shouldBeEqualWhenSameName() {
        //given
        val data2 = PackageData.newInstance(name);
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
        val data2 = PackageData.newInstance(name);
        final Set<PackageData> set = new HashSet<>();
        //when
        set.add(data);
        //then
        assertThat(set, hasItem(data2));
    }

    @Test
    public void equalsShouldBeFalseWhenOtherIsNull() {
        //given
        val data = PackageData.newInstance("name");
        //then
        assertThat(data.equals(null), is(false));
    }

    @Test
    public void equalsShouldBeFalseWhenOtherIsNotPackageData() {
        //given
        val data = PackageData.newInstance("name");
        //then
        assertThat(data.equals("not PackageData"), is(false));
    }
}
