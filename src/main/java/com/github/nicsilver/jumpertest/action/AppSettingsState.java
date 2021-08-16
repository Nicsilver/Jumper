//package com.github.nicsilver.jumpertest.action;
//
//
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.components.PersistentStateComponent;
//import com.intellij.openapi.components.State;
//import com.intellij.openapi.components.Storage;
//import com.intellij.util.xmlb.XmlSerializerUtil;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
///**
// * Supports storing the application settings in a persistent way.
// * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
// * these persistent application settings are stored.
// */
//@State(
//        name = "org.intellij.sdk.settings.AppSettingsState",
//        storages = {@Storage("SdkSettingsPlugin.xml")}
//)
//public class AppSettingsState implements PersistentStateComponent<JumperState>
//{
//    public String userId = "John Q. Public";
//    public boolean ideaStatus = false;
//
//    public static JumperState getInstance()
//    {
//        return ApplicationManager.getApplication().getService(JumperState.class);
//    }
//
//    @Nullable
//    @Override
//    public JumperState getState()
//    {
//        return this;
//    }
//
//    @Override
//    public void loadState(@NotNull JumperState state)
//    {
//        XmlSerializerUtil.copyBean(state, this);
//    }
//
//}
