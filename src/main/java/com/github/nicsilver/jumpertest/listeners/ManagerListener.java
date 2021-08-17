package com.github.nicsilver.jumpertest.listeners;

import com.github.nicsilver.jumpertest.action.JumpManager;
import com.github.nicsilver.jumpertest.action.JumperState;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class ManagerListener implements ProjectManagerListener, DumbAware
{
    @Override
    public void projectOpened(@NotNull Project project)
    {
        ProjectManagerListener.super.projectOpened(project);
        
        JumperState.getInstance().jumpCaretActions.forEach(JumpManager::RegisterJumpAction);
    }
}
