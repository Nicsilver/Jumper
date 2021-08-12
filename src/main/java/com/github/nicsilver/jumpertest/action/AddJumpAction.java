package com.github.nicsilver.jumpertest.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SdkIcons;
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
        
        JumperState.getInstance().jumpCaretActions.add(new JumpCaretAction("Jump" + jump, "Dynamic Action Demo", SdkIcons.Sdk_default_icon, jump));
        
        System.out.println("AddJumpAction.actionPerformed: " + JumperState.getInstance().jumpCaretActions.size());
    }
}
