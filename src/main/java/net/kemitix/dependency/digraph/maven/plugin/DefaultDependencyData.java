package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import org.apache.maven.plugin.logging.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation for storing package and class dependency data.
 *
 * @author pcampbell
 */
public class DefaultDependencyData implements DependencyData {

    @Getter
    private final Map<String, Set<String>> packageUses;

    /**
     * Default Constructor.
     */
    public DefaultDependencyData() {
        this.packageUses = new HashMap<>();
    }

    @Override
    public void addDependency(
            final String userPackage,
            final String usedPackage) {
        // package dependency
        if (!packageUses.containsKey(userPackage)) {
            packageUses.put(userPackage, new HashSet<>());
        }
        if (!userPackage.equals(usedPackage)) {
            packageUses.get(userPackage).add(usedPackage);
        }
    }

    @Override
    public void dumpDependencies(final Log log) {
        log.info("Packages:");
        packageUses.forEach((String user, Set<String> used) -> {
            used.forEach((String t) -> {
                log.info("* " + user + " => " + t);
            });
        });
    }

    @Override
    public List<String> getUserPackages() {
        return new ArrayList<>(packageUses.keySet());
    }

    @Override
    public List<String> getUsedPackages(final String user) {
        return new ArrayList<>(packageUses.get(user));
    }
}
