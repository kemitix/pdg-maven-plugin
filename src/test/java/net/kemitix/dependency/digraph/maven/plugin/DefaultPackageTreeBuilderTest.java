package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import net.kemitix.node.Node;
import net.kemitix.node.NodeException;

/**
 * Tests for {@link DefaultPackageTreeBuilder}.
 *
 * @author pcampbell
 */
public class DefaultPackageTreeBuilderTest {

    /**
     * Class under test.
     */
    private DefaultPackageTreeBuilder builder;

    private final String basePackage = "base";

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        builder = new DefaultPackageTreeBuilder();
        builder.init(basePackage);
    }

    /**
     * Test what an empty tree looks like.
     */
    @Test
    public void shouldReturnEmptyTree() {
        //when
        Node<String> tree = builder.getTree();
        //then
        assertNotNull(tree);
        assertThat(tree.getChildren().size(), is(0));
        assertThat(tree.getParent(), is(Optional.empty()));
    }

    /**
     * Test that adding a new package is added to the tree.
     */
    @Test
    public void shouldAddPackageToTree() {
        //given
        final String subpackage = "subpackage";
        //when
        builder.addPackages(basePackage + "." + subpackage);
        Node<String> tree = builder.getTree();
        final Optional<Node<String>> found = tree.findChild(subpackage);
        //then
        assertTrue(found.isPresent());
        assertThat(found.get().getData(), is(Optional.of(subpackage)));
    }

    /**
     * Test that adding multiple packages are added to the tree.
     */
    @Test
    public void shouldAddMultiplePackagesToTree() {
        //given
        final String alpha = "alpha";
        final String beta = "beta";
        final String delta = "delta";
        //when
        builder.addPackages(
                basePackage + "." + beta,
                basePackage + "." + alpha + "." + delta);
        Node<String> tree = builder.getTree();
        //then
        assertTrue(tree.findChild(beta).isPresent());
        final Optional<Node<String>> foundAlpha = tree.findChild(alpha);
        assertTrue(foundAlpha.isPresent());
        assertTrue(foundAlpha.get().findChild(delta).isPresent());
    }

    /**
     * Test that adding package outwith the base package throws an exception.
     */
    @Test(expected = NodeException.class)
    public void shouldThrowNEWhenAddPackageOutwithBase() {
        //when
        builder.addPackages("outwith.base");
    }

    /**
     * Test that adding the base package is safe.
     */
    @Test
    public void shouldAddPackageBaseSafely() {
        //when
        builder.addPackages(basePackage);
        //then
        shouldReturnEmptyTree();
    }

    /**
     * Test that attempting to set the base package to null causes an NPE to be
     * thrown.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenInitNull() {
        //when
        builder.init(null);
    }

    /**
     * Test that attempting to call addPackages with one of the packages being
     * null causes an NPE to be thrown.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenAddPackagesContainsNull() {
        //when
        builder.addPackages(basePackage + "." + "other", null);
    }
}
