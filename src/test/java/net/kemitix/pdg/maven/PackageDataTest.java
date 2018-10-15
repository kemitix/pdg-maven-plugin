package net.kemitix.pdg.maven;

import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PackageData}.
 *
 * @author pcampbell
 */
public class PackageDataTest {

    private PackageData data;

    private String name;

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
        assertThat(data.getName()).isEqualTo(name);
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
        assertThat(data.hashCode()).isNotEqualTo(data2.hashCode());
    }

    /**
     * Two objects with the same name are equal.
     */
    @Test
    public void shouldBeEqualWhenSameName() {
        //given
        val data2 = PackageData.newInstance(name);
        //then
        assertThat(data).isEqualTo(data2)
                        .isNotSameAs(data2);
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
        assertThat(set).contains(data2);
    }

    @Test
    public void equalsShouldBeFalseWhenOtherIsNull() {
        //given
        val data = PackageData.newInstance("name");
        //then
        assertThat(data).isNotNull();
    }

    @Test
    public void equalsShouldBeFalseWhenOtherIsNotPackageData() {
        //given
        val data = PackageData.newInstance("name");
        //then
        assertThat(data).isNotEqualTo("not PackageData");
    }
}
