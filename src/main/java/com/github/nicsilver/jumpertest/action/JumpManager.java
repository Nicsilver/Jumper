package com.github.nicsilver.jumpertest.action;

import com.intellij.openapi.extensions.PluginId;
import icons.SdkIcons;

import static com.intellij.openapi.actionSystem.ActionManager.getInstance;

public class JumpManager
{
    public static void AddJumpAction(int jumpAmount)
    {
        if (!JumperState.getInstance().jumpCaretActions.contains(jumpAmount))
        {
            JumperState.getInstance().jumpCaretActions.add(jumpAmount);
        }
        JumpManager.RegisterJumpAction(jumpAmount);
    }
    
    public static void RemoveJumpAction(int jumpAmount)
    {
        JumperState.getInstance().jumpCaretActions.remove((Object) jumpAmount);
        UnregisterJumpAction(jumpAmount);
    }
    
    public static void RegisterJumpAction(int jumpAmount)
    {
        JumpCaretAction jumpUp = new JumpCaretAction(jumpAmount + " Jump Up", "Jump Up " + jumpAmount,
                SdkIcons.Sdk_default_icon, jumpAmount, JumpCaretAction.EditorActions.UP);
        JumpCaretAction jumpDown = new JumpCaretAction(jumpAmount + " Jump Down", "Jump Down " + jumpAmount,
                SdkIcons.Sdk_default_icon, jumpAmount, JumpCaretAction.EditorActions.DOWN);
        JumpCaretAction jumpUpWithSelection = new JumpCaretAction(jumpAmount + " Jump Up Selection", "Jump Up Selection " + jumpAmount,
                SdkIcons.Sdk_default_icon, jumpAmount, JumpCaretAction.EditorActions.UP_WITH_SELECTION);
        JumpCaretAction jumpDownWithSelection = new JumpCaretAction(jumpAmount + " Jump Down Selection", "Jump Up Selection " + jumpAmount,
                SdkIcons.Sdk_default_icon, jumpAmount, JumpCaretAction.EditorActions.DOWN_WITH_SELECTION);
        
        getInstance().registerAction("JumpUp " + jumpAmount, jumpUp, PluginId.getId("Jumper"));
        getInstance().registerAction("JumpDown " + jumpAmount, jumpDown, PluginId.getId("Jumper"));
        getInstance().registerAction("JumpUpSelect " + jumpAmount, jumpUpWithSelection, PluginId.getId("Jumper"));
        getInstance().registerAction("JumpDownSelect " + jumpAmount, jumpDownWithSelection, PluginId.getId("Jumper"));
    }
    
    public static void UnregisterJumpAction(int jumpAmount)
    {
        getInstance().unregisterAction("JumpUp " + jumpAmount);
        getInstance().unregisterAction("JumpDown " + jumpAmount);
        getInstance().unregisterAction("JumpUpSelect " + jumpAmount);
        getInstance().unregisterAction("JumpDownSelect " + jumpAmount);
    }
    
    
}
