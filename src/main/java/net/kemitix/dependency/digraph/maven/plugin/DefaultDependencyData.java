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

//    private final Map<PackagedClass, Set<PackagedClass>> classUses;
    /**
     * Default Constructor.
     */
    public DefaultDependencyData() {
        this.packageUses = new HashMap<>();
//        this.classUses = new HashMap<>();
    }

    @Override
    public void addDependency(
            final String userPackage, final String userClass,
            final String usedPackage, final String usedClass) {
        // package dependency
        if (!packageUses.containsKey(userPackage)) {
            packageUses.put(userPackage, new HashSet<>());
        }
        packageUses.get(userPackage).add(usedPackage);
//        // class dependency
//        PackagedClass user = new PackagedClass(userPackage, userClass);
//        PackagedClass used = new PackagedClass(usedPackage, usedClass);
//        if (!classUses.containsKey(user)) {
//            classUses.put(user, new HashSet<>());
//        }
//        classUses.get(user).add(used);
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

//    /**
//     * Pairs a class name with its containing package name.
//     */
//    @Getter
//    private static class PackagedClass {
//
//        private final String thePackage;
//        private final String theClass;
//
//        /**
//         * Constuctor.
//         *
//         * @param thePackage the name of the package containing the class
//         * @param theClass   the name of the class within the package
//         */
//        @SuppressWarnings("hiddenfield")
//        PackagedClass(final String thePackage, final String theClass) {
//            this.thePackage = thePackage;
//            this.theClass = theClass;
//        }
//    }
    @Override
    public List<String> getUserPackages() {
        return new ArrayList<>(packageUses.keySet());
    }

    @Override
    public List<String> getUsedPackages(final String user) {
        return new ArrayList<>(packageUses.get(user));
    }
}
