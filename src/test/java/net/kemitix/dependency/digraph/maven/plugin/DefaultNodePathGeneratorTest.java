package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;
import net.kemitix.node.NodeItem;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DefaultNodePathGenerator}.
 *
 * @author Paul Campbell
 */
public class DefaultNodePathGeneratorTest {

    /**
     * Class under test.
     */
    private NodePathGenerator generator;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        generator = new DefaultNodePathGenerator();
    }

    /**
     * Test path for root node.
     */
    @Test
    public void testGetPathForRoot() {
        //given
        final PackageData rootData = new PackageData("root");
        final Node<PackageData> root = new NodeItem<>(rootData);
        //when
        final String result = generator.getPath(root, root, ".");
        //then
        assertThat(result, is(""));
    }

    /**
     * Test path for child of root.
     */
    @Test
    public void testGetPathForChild() {
        //given
        // root
        final PackageData rootData = new PackageData("root");
        final Node<PackageData> root = new NodeItem<>(rootData);
        // child
        final String childName = "child";
        final PackageData childData = new PackageData(childName);
        final Node<PackageData> child = new NodeItem<>(childData, root);
        //when
        final String result = generator.getPath(child, root, ".");
        //then
        assertThat(result, is(childName));
    }

    /**
     * Test path for grandchild of root.
     */
    @Test
    public void testGetPathForGrandchild() {
        //given
        // root
        final PackageData rootData = new PackageData("root");
        final Node<PackageData> root = new NodeItem<>(rootData);
        // child
        final String childName = "child";
        final PackageData childData = new PackageData(childName);
        final Node<PackageData> child = new NodeItem<>(childData, root);
        // grandchild
        final String grandchildName = "grandchild";
        final PackageData grandchildData = new PackageData(grandchildName);
        final Node<PackageData> grandchild
                = new NodeItem<>(grandchildData, child);
        //when
        final String result = generator.getPath(grandchild, root, ".");
        //then
        assertThat(result, is(childName + "." + grandchildName));
    }

}
