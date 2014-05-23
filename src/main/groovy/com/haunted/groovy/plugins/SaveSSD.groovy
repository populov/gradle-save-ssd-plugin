package com.haunted.groovy.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

public class SaveSSD implements Plugin<Project> {
    private String saveSSDRoot = '/tmp/gradle/'
    private String tag = "SaveSSD"
    private Project project
    void apply(Project project) {
        this.project = project

        String version = SaveSSD.getPackage().getImplementationVersion();
        if (version != null)
            tag = "${tag} (${version})";

        if (project.hasProperty("saveSSD")) {
            if (Boolean.parseBoolean(project.saveSSD) && getOSName() != 'windows') {
                createLink(project.ant)
            }
        }

        project.task("saveSsd") << {
            createLink(project.ant)
        }
    }

    def createLink(AntBuilder ant) {
        File link = project.buildDir;
        if (link.exists()) {
            if (link.listFiles().length != 0)
                return;
            println "${tag}: Delete ${link}"
            link.delete()
        }

        File target = getTmpBuildDir()

        if (target.mkdirs())
            println "${tag}: Created directory ${target}";
        ant.symlink(link: link.absolutePath, resource: target.absolutePath, overwrite: true)
        println "${tag}: Created link ${link} --> ${target}"
    }

    static String getOSName() {
        String osNameProperty = System.getProperty('os.name')
        osNameProperty = osNameProperty.toLowerCase()
        if (osNameProperty.indexOf('linux') >= 0) {
            return 'linux'
        } else if (osNameProperty.indexOf('windows') >= 0) {
            return 'windows'
        } else if (osNameProperty.indexOf('mac') >= 0) {
            return 'osx'
        }
        return 'unknown'
    }

    File getTmpBuildDir() {
        String path = project.name
        Project p = project.getParent()
        while (p != null) {
            path = p.name + '/' + path
            p = p.getParent()
        }

        if (project.hasProperty("saveSSDRoot")) {
            String newRoot = project.saveSSDRoot;
            if (newRoot != null && !newRoot.isEmpty()) {
                if (!newRoot.endsWith("/"))
                    newRoot = newRoot + "/";
                saveSSDRoot = newRoot;
            }
        }

        return new File(saveSSDRoot + path)
    }
}