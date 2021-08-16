package com.github.nicsilver.jumpertest.action;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@State(
        
        name = "com.github.nicsilver.jumpertest.action.JumperState",
        storages = {@Storage("SdkSettingsPlugin.xml")}
)

public class JumperState implements PersistentStateComponent<JumperState>
{
    public ArrayList<Integer> jumpCaretActions = new ArrayList<>();
    
    public static JumperState getInstance()
    {
        return ApplicationManager.getApplication().getService(JumperState.class);
    }
    
    @Nullable
    @Override
    public JumperState getState()
    {
        return this;
    }
    
    @Override
    public void loadState(@NotNull JumperState state)
    {
        XmlSerializerUtil.copyBean(state, this);
    }
    
    
}
