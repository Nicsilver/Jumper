package com.github.nicsilver.jumpertest.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class AddJumpAction extends AnAction
{
    public AddJumpAction()
    {
        super();
    }
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        int jump = new Random().nextInt(10);

//        JumpCaretAction jumpCaretAction = new JumpCaretAction("Jump " + jump, "Jump " + jump, SdkIcons.Sdk_default_icon, jump);
        
        JumperState.getInstance().jumpCaretActions.add(jump);
        
        ActionManager.getInstance().registerAction("Jump " + jump, new JumpCaretAction(jump));
        ActionManager.getInstance()
        
        System.out.println("AddJumpAction.actionPerformed: " + JumperState.getInstance().jumpCaretActions.size());
    }
}
