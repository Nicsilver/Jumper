package com.github.nicsilver.jumpertest.services

import com.github.nicsilver.jumpertest.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
