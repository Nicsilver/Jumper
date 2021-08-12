package com.github.nicsilver.jumpertest.action;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SdkIcons;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DynamicActionGroup extends ActionGroup
{
    @NotNull
    @Override
    public AnAction @NotNull [] getChildren(AnActionEvent e)
    {
        
        JumperState jumperState = JumperState.getInstance();
        JumpCaretAction jump2 = new JumpCaretAction("Jump2", "Dynamic Action Demo", SdkIcons.Sdk_default_icon, 2);
        
        assert jumperState != null;
        if (jumperState.jumpCaretActions.size() > 0)
        {
            ActionManager am = ActionManager.getInstance();
            
            JumpCaretAction jump5 = new JumpCaretAction("Jump5", "Dynamic Action Demo", SdkIcons.Sdk_default_icon, 5);
            
            AnAction[] actions = new AnAction[jumperState.jumpCaretActions.size()];
//            ArrayList<Integer> jumpCaretActions = jumperState.jumpCaretActions;
            for (int i = 0; i < jumperState.jumpCaretActions.size(); i++)
            {
                if (jumperState.jumpCaretActions.get(i) != null)
                {
                    
                    System.out.println("DynamicActionGroup.getChildren: " + jumperState.jumpCaretActions.get(i));
//                    actions[i] = jumperState.jumpCaretActions.get(i);
                }
            }
//            System.out.println("DynamicActionGroup.getChildren: item 1: " + actions[1]);
            return actions;
//        am.registerAction("JumpUp" + 2, jump2);
//        return new AnAction[]{
//                jump2, jump5
//        };
        }
        else
        {
            System.out.println("DynamicActionGroup.getChildren: else");
            return new AnAction[]{jump2};
        }
    }
}