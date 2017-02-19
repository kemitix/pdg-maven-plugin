package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;
import net.kemitix.node.Node;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultTreeFilter}
 */
public class DefaultTreeFilterTest {

    private NodePathGenerator nodePathGenerator;

    /**
     * Assert that the node has all the dependencies that it needs.
     *
     * @param node         The node with a list of those it uses
     * @param dependencies The dependencies that node should be using
     */
    private static void assertDependency(final Node<PackageData> node, final List<Node<PackageData>> dependencies) {
        val uses = node.getData()
                       .getUses()
                       .stream()
                       .map(Node::getData)
                       .map(PackageData::getName)
                       .collect(Collectors.toList());
        // uses: list of names of packages used by node
        dependencies.stream()
                    .map(Node::getName)
                    .forEach(dependencyName -> assertThat(uses).as(node.getName() + " -> " + dependencyName)
                                                               .contains(dependencyName));
    }

    @Before
    public void setUp() {
        nodePathGenerator = new DefaultNodePathGenerator();
    }

    @Test
    public void shouldGenerateDefaultTree() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //then
        assertThat(baseNode).as("has base node")
                            .isNotNull();
        assertThat(baseNode.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = baseNode.getChildren();
        assertThat(children).as("blackjack contains cli, game and model")
                            .extracting(Node::getName)
                            .containsExactlyInAnyOrder("cli", "game", "model");
        val childMap = new HashMap<String, Node<PackageData>>(4);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        val cli = childMap.get("cli");
        val game = childMap.get("game");
        val model = childMap.get("model");
        cli.getChildren()
           .forEach(c -> c.findData()
                          .ifPresent(d -> childMap.put(d.getName(), c)));
        val console = childMap.get("console");

        assertThat(childMap).containsKeys("cli", "game", "model", "console");

        // cli depends on only game, model and console
        assertDependency(cli, Arrays.asList(game, model, console));
        // game depends on only console and model
        assertDependency(game, Arrays.asList(console, model));
    }

    @Test
    public void shouldExcludeGame() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("game", "", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains cli and model (no game)")
                            .extracting(Node::getName)
                            .containsExactlyInAnyOrder("cli", "model");
        val childMap = new HashMap<String, Node<PackageData>>(3);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        val cli = childMap.get("cli");
        assertThat(cli.getChildren()).as("cli has child")
                                     .hasSize(1);
        val model = childMap.get("model");
        cli.getChildren()
           .forEach(c -> c.findData()
                          .ifPresent(d -> childMap.put(d.getName(), c)));
        val console = childMap.get("console");

        assertThat(childMap).as("has nodes cli and model")
                            .containsKeys("cli", "model")
                            .as("has node console")
                            .containsKeys("console")
                            .as("has no node game")
                            .doesNotContainKey("game");

        // cli depends on only model and console
        assertDependency(cli, Arrays.asList(model, console));
    }

    @Test
    public void shouldExcludeModel() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("model", "", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains cli and game")
                            .extracting(Node::getName)
                            .containsExactlyInAnyOrder("cli", "game");
        val childMap = new HashMap<String, Node<PackageData>>(3);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        val cli = childMap.get("cli");
        val game = childMap.get("game");
        assertThat(childMap).doesNotContainKey("model");
        cli.getChildren()
           .forEach(c -> c.findData()
                          .ifPresent(d -> childMap.put(d.getName(), c)));
        val console = childMap.get("console");

        assertThat(childMap).containsKeys("cli", "game", "console");

        // cli depends on only game and console
        assertDependency(cli, Arrays.asList(game, console));
        // game depends on only console
        assertDependency(game, Collections.singletonList(console));
    }

    @Test
    public void shouldExcludeConsole() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("console", "", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains cli, game and model")
                            .extracting(Node::getName)
                            .containsExactlyInAnyOrder("cli", "game", "model");
        val childMap = new HashMap<String, Node<PackageData>>(4);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        val cli = childMap.get("cli");
        val game = childMap.get("game");
        val model = childMap.get("model");
        cli.getChildren()
           .forEach(c -> c.findData()
                          .ifPresent(d -> childMap.put(d.getName(), c)));
        assertThat(childMap).doesNotContainKey("console");

        assertThat(childMap).containsKeys("cli", "game", "model");

        // cli depends on only game and model
        assertDependency(cli, Arrays.asList(game, model));
        // game depends on only model
        assertDependency(game, Collections.singletonList(model));
    }

    @Test
    public void shouldExcludeCli() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("cli", "", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains game and model")
                            .extracting(Node::getName)
                            .containsExactlyInAnyOrder("game", "model");
        val childMap = new HashMap<String, Node<PackageData>>(4);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));

        val game = childMap.get("game");
        val model = childMap.get("model");
        assertThat(childMap).doesNotContainKey("cli");
        assertThat(childMap).doesNotContainKey("console");

        assertThat(childMap).containsKeys("game", "model");

        // game depends on only model
        assertDependency(game, Collections.singletonList(model));
    }

    // shouldIncludeGame -- includes everything as game touches everything else
    @Test
    public void shouldIncludeGame() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("", "game", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains cli, game and model")
                            .extracting(Node::getName)
                            .containsExactlyInAnyOrder("cli", "game", "model");
        val childMap = new HashMap<String, Node<PackageData>>(4);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        val cli = childMap.get("cli");
        val game = childMap.get("game");
        val model = childMap.get("model");
        cli.getChildren()
           .forEach(c -> c.findData()
                          .ifPresent(d -> childMap.put(d.getName(), c)));
        val console = childMap.get("console");

        assertThat(childMap).containsKeys("cli", "game", "model", "console");

        // cli depends on only game, model and console
        assertDependency(cli, Arrays.asList(game, model, console));
        // game depends on only console and model
        assertDependency(game, Arrays.asList(console, model));
    }

    // shouldIncludeCli -- includes everything as cli touches everything else
    @Test
    public void shouldIncludeCli() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("", "cli", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains cli, game and model")
                            .extracting(node -> node.findData()
                                                    .map(PackageData::getName)
                                                    .orElse("foo"))
                            .containsExactlyInAnyOrder("cli", "game", "model");
        val childMap = new HashMap<String, Node<PackageData>>(4);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        val cli = childMap.get("cli");
        val game = childMap.get("game");
        val model = childMap.get("model");
        cli.getChildren()
           .forEach(c -> c.findData()
                          .ifPresent(d -> childMap.put(d.getName(), c)));
        val console = childMap.get("console");

        assertThat(childMap).containsKeys("cli", "game", "model", "console");

        // cli depends on only game, model and console
        assertDependency(cli, Arrays.asList(game, model, console));
        // game depends on only console and model
        assertDependency(game, Arrays.asList(console, model));
    }

    // shouldIncludeModel -- excludes console
    @Test
    public void shouldIncludeModel() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("", "model", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains cli, game and model")
                            .extracting(node -> node.findData()
                                                    .map(PackageData::getName)
                                                    .orElse("foo"))
                            .containsExactlyInAnyOrder("cli", "game", "model");
        val childMap = new HashMap<String, Node<PackageData>>(4);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        val cli = childMap.get("cli");
        val game = childMap.get("game");
        val model = childMap.get("model");
        cli.getChildren()
           .forEach(c -> c.findData()
                          .ifPresent(d -> childMap.put(d.getName(), c)));
        assertThat(childMap).doesNotContainKey("console");

        assertThat(childMap).containsKeys("cli", "game", "model");

        // cli depends on only game and model
        assertDependency(cli, Arrays.asList(game, model));
        // game depends on only model
        assertDependency(game, Collections.singletonList(model));
    }

    // shouldIncludeConsole -- excludes model
    @Test
    public void shouldIncludeConsole() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("", "console", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains cli and game")
                            .extracting(node -> node.findData()
                                                    .map(PackageData::getName)
                                                    .orElse("foo"))
                            .containsExactlyInAnyOrder("cli", "game");
        val childMap = new HashMap<String, Node<PackageData>>(3);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        val cli = childMap.get("cli");
        val game = childMap.get("game");
        assertThat(childMap).doesNotContainKey("model");
        cli.getChildren()
           .forEach(c -> c.findData()
                          .ifPresent(d -> childMap.put(d.getName(), c)));
        val console = childMap.get("console");

        assertThat(childMap).containsKeys("cli", "game", "console");

        // cli depends on only game and console
        assertDependency(cli, Arrays.asList(game, console));
        // game depends on only console
        assertDependency(game, Collections.singletonList(console));
    }

    /**
     * shouldExcludeCliIncludeModel -- exclude cli, console, but include game and model
     */
    @Test
    public void shouldExcludeCliIncludeModel() throws Exception {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        //when
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("cli", "model", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        assertThat(filtered).as("has base node")
                            .isNotNull();
        assertThat(filtered.findData()).as("base node is blackjack")
                                       .isNotEmpty()
                                       .map(PackageData::getName)
                                       .contains("blackjack");
        val children = filtered.getChildren();
        assertThat(children).as("blackjack contains game and model")
                            .extracting(node -> node.findData()
                                                    .map(PackageData::getName)
                                                    .orElse("foo"))
                            .containsExactlyInAnyOrder("game", "model");
        val childMap = new HashMap<String, Node<PackageData>>(4);
        children.forEach(c -> c.findData()
                               .ifPresent(d -> childMap.put(d.getName(), c)));
        assertThat(childMap).doesNotContainKey("cli");
        val game = childMap.get("game");
        val model = childMap.get("model");

        assertThat(childMap).containsKeys("game", "model");

        // game depends on only model
        assertDependency(game, Collections.singletonList(model));
    }

    @Test
    public void usesInFilteredTreeShouldShareNewRoot() {
        //given
        val dependencyData = TestBlackJackDependencyData.getDependencyData();
        val baseNode = dependencyData.getBaseNode();
        baseNode.stream()
                .forEach(node -> node.setName(node.getData()
                                                  .getName()));
        //when
        /// include all nodes
        val treeFilter = new DefaultTreeFilter(GraphFilter.of("", "", nodePathGenerator), nodePathGenerator);
        val filtered = treeFilter.filterTree(baseNode);
        //then
        SoftAssertions.assertSoftly(softly -> filtered.stream()
                                                      .map(Node::getData)
                                                      .flatMap(data -> data.getUses()
                                                                           .stream())
                                                      .forEach(use -> softly.assertThat(use.isDescendantOf(filtered))
                                                                            .isTrue()));
    }
}
