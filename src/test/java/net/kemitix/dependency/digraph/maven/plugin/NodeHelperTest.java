package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;
import org.assertj.core.internal.cglib.core.ReflectUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

import net.kemitix.node.Nodes;

/**
 * Tests for {@link NodeHelper}.
 *
 * @author pcampbell
 */
public class NodeHelperTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getRequiredDataReturnsData() throws Exception {
        //given
        val data = new PackageData("data");
        val node = Nodes.unnamedRoot(data);
        //when
        val result = NodeHelper.getRequiredData(node);
        //then
        assertThat(result).isSameAs(data);
    }

    @Test
    public void getRequiredDataShouldThrowExceptionIfEmpty() throws Exception {
        //given
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Node has no package data");
        val node = Nodes.unnamedRoot((PackageData) null);
        //when
        NodeHelper.getRequiredData(node);
    }

    @Test
    public void exercisePrivateDefaultConstructor() {
        ReflectUtils.newInstance(NodeHelper.class);
    }

}
