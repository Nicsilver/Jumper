package com.github.nicsilver.jumpertest.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class JumpCaretAction extends AnAction
{
    enum EditorActions
    {
        UP,
        DOWN,
        UP_WITH_SELECTION,
        DOWN_WITH_SELECTION
    }
    
    private final String action;
    private final int jumpAmount;
    
    public JumpCaretAction(@Nullable String text, @Nullable String description, @Nullable Icon icon, int jumpAmount, EditorActions action)
    {
        super(text, description, icon);
        this.jumpAmount = jumpAmount;
        this.action = GetEditorAction(action);
    }
    
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e)
    {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final EditorActionManager actionManager = EditorActionManager.getInstance();
        
        EditorActionHandler editorActionHandler = actionManager.getActionHandler(this.action);
        
        for (int i = 0; i < jumpAmount; i++)
        {
            editorActionHandler.execute(editor, editor.getCaretModel().getPrimaryCaret(), e.getDataContext());
        }
    }
    
    private String GetEditorAction(EditorActions action)
    {
        String ideActions;
        switch (action)
        {
            case UP:
                ideActions = IdeActions.ACTION_EDITOR_MOVE_CARET_UP;
                break;
            case DOWN:
                ideActions = IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN;
                break;
            case UP_WITH_SELECTION:
                ideActions = IdeActions.ACTION_EDITOR_MOVE_CARET_UP_WITH_SELECTION;
                break;
            case DOWN_WITH_SELECTION:
                ideActions = IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN_WITH_SELECTION;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
        
        return ideActions;
    }
    
}