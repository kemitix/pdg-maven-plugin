package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.node.Nodes;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        val rootData = PackageData.newInstance("root");
        val root = Nodes.unnamedRoot(rootData);
        //when
        val result = generator.getPath(root, root, ".");
        //then
        assertThat(result).isEmpty();
    }

    /**
     * Test path for child of root.
     */
    @Test
    public void testGetPathForChild() {
        //given
        // root
        val rootData = PackageData.newInstance("root");
        val root = Nodes.unnamedRoot(rootData);
        // child
        val childName = "child";
        val childData = PackageData.newInstance(childName);
        val child = Nodes.unnamedChild(childData, root);
        //when
        val result = generator.getPath(child, root, ".");
        //then
        assertThat(result).isEqualTo(childName);
    }

    /**
     * Test path for grandchild of root.
     */
    @Test
    public void testGetPathForGrandchild() {
        //given
        // root
        val rootData = PackageData.newInstance("root");
        val root = Nodes.unnamedRoot(rootData);
        // child
        val childName = "child";
        val childData = PackageData.newInstance(childName);
        val child = Nodes.unnamedChild(childData, root);
        // grandchild
        val grandchildName = "grandchild";
        val grandchildData = PackageData.newInstance(grandchildName);
        val grandchild = Nodes.unnamedChild(grandchildData, child);
        //when
        val result = generator.getPath(grandchild, root, ".");
        //then
        assertThat(result).isEqualTo(childName + "." + grandchildName);
    }
}
