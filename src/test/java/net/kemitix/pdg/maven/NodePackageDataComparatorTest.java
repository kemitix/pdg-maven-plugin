package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.node.EmptyNodeException;
import net.kemitix.node.Nodes;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NodePackageDataComparator}.
 *
 * @author pcampbell
 */
public class NodePackageDataComparatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private NodePackageDataComparator comparator;

    @Before
    public void setUp() throws Exception {
        comparator = new NodePackageDataComparator();
    }

    @Test
    public void compareAlphaComesBeforeBetaWhenAlphaBeta() throws Exception {
        //given
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaPackage = PackageData.newInstance("beta");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //when
        val result = comparator.compare(alphaNode, betaNode);
        //then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void compareAlphaComesBeforeBetaWhenBetaAlpha() throws Exception {
        //given
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaPackage = PackageData.newInstance("beta");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //when
        val result = comparator.compare(betaNode, alphaNode);
        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void compareAlphaEqualsBetaWhenPackageNameNamesAreSame() throws Exception {
        //given
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaPackage = PackageData.newInstance("alpha");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //when
        val result = comparator.compare(betaNode, alphaNode);
        //then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void compareShouldThrowExceptionWhenAlphaNodeIsEmpty() {
        //given
        exception.expect(EmptyNodeException.class);
        val alphaNode = Nodes.unnamedRoot((PackageData) null);
        val betaPackage = PackageData.newInstance("beta");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //when
        comparator.compare(betaNode, alphaNode);
    }

    @Test
    public void compareShouldThrowExceptionWhenBetaNodeIsEmpty() {
        //given
        exception.expect(EmptyNodeException.class);
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaNode = Nodes.unnamedRoot((PackageData) null);
        //when
        comparator.compare(betaNode, alphaNode);
    }

    @Test
    public void compareShouldThrowExceptionWhenAlphaBetaNodesBothEmpty() {
        //given
        exception.expect(EmptyNodeException.class);
        val alphaNode = Nodes.unnamedRoot((PackageData) null);
        val betaNode = Nodes.unnamedRoot((PackageData) null);
        //when
        comparator.compare(betaNode, alphaNode);
    }

}
