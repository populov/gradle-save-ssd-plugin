package com.haunted.groovy.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

public class SaveSSD implements Plugin<Project> {
    private String tmpRoot = '/tmp/gradle/'
    private Project project
    void apply(Project project) {
        this.project = project
        if (project.hasProperty("saveSSD")) {
            println "saveSSD = ${project.saveSSD}"
            if (Boolean.parseBoolean(project.saveSSD) && getOSName() != 'windows') {
                createLink(project.ant)
            }
        }

        project.tasks("saveSsd") << {
            createLink(project.ant)
        }
    }

    def createLink(AntBuilder ant) {
        File link = project.buildDir;
        println "checking ${link}..."
        if (link.exists()) {
            if (link.listFiles().length != 0)
                return;
            link.delete()
        }

        File target = getTmpBuildDir()
        if (target.mkdirs())
            println "Created directory " + target;
        ant.symlink(link: link.absolutePath, resource: target.absolutePath, overwrite: true)
        println "Creating link ${link} --> ${target}"
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
        return new File(tmpRoot + path)
    }
}