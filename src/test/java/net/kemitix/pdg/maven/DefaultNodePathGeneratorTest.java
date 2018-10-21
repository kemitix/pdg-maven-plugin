package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.node.Nodes;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class DefaultNodePathGeneratorTest implements WithAssertions {

    private final NodePathGenerator generator = new DefaultNodePathGenerator();

    /**
     * Test path for root node.
     */
    @Test
    void testGetPathForRoot() {
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
    void testGetPathForChild() {
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
    void testGetPathForGrandchild() {
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
