package com.github.nicsilver.jumpertest.action;


import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Menu action to clone a new caret based on an existing one.
 *
 * @see AnAction
 */
public class JumpCaretAction extends AnAction
{
    
    private int jumpAmount = 0;
    
    public JumpCaretAction()
    {
        super();
    }
    
    public JumpCaretAction(int jumpAmount)
    {
        this.jumpAmount = jumpAmount;
    }
    
    public JumpCaretAction(@Nullable String text, @Nullable String description, @Nullable Icon icon, int jmp)
    {
        super(text, description, icon);
        this.jumpAmount = jmp;
        
        
    }
    
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e)
    {
        // Editor is known to exist from update, so it's not null
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        // Get the action manager in order to get the necessary action handler...
        final EditorActionManager actionManager = EditorActionManager.getInstance();
        // Get the action handler registered to clone carets
        final EditorActionHandler actionHandler =
                actionManager.getActionHandler(IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN);
        
        for (int i = 0; i < jumpAmount; i++)
        {
            actionHandler.execute(editor, editor.getCaretModel().getPrimaryCaret(), e.getDataContext());
        }
    }
    
    @Override
    public void update(@NotNull final AnActionEvent e)
    {
//        final Project project = e.getProject();
//        final Editor editor = e.getData(CommonDataKeys.EDITOR);
//        // Make sure at least one caret is available
//        boolean menuAllowed = false;
//        if (editor != null && project != null) {
//            // Ensure the list of carets in the editor is not empty
//            menuAllowed = !editor.getCaretModel().getAllCarets().isEmpty();
//        }
//        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }
    
}