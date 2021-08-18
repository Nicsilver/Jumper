package com.github.nicsilver.jumpertest.action;

import com.intellij.ide.DataManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ex.Settings;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.concurrency.EdtExecutorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class AppSettingsComponent implements ActionListener
{
    private final JPanel myMainPanel = new JPanel();
    private final JBTextField jumpAmount = new JBTextField();
    
    public AppSettingsComponent()
    {
        myMainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        
        JBLabel jbLabel = new JBLabel("Enter jump amount: ");
        JButton createJumpButton = new JButton("Create Jump");
        createJumpButton.setName("Create Jump");
        
        
        myMainPanel.add(jbLabel);
        myMainPanel.add(jumpAmount);
        myMainPanel.add(createJumpButton);
        
        JButton removeJumpButton = new JButton("Remove Jump");
        removeJumpButton.setName("Remove Jump");

//        myMainPanel.add(separator);
        myMainPanel.add(new JLabel(" | "));
        myMainPanel.add(removeJumpButton);
        removeJumpButton.addActionListener(this);
        createJumpButton.addActionListener(this);
        
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        
        myMainPanel.add(separator);
        
        JLabel currentlyAddedJumps = new JLabel("Currently added jumps:  ");
        myMainPanel.add(currentlyAddedJumps);
        
        for (Integer jumpCaretAction : JumperState.getInstance().jumpCaretActions)
        {
            myMainPanel.add(new JLabel(jumpCaretAction.toString() + "  "));
        }
        
        JButton changeKeybindsButton = new JButton("Change Keybinds");
        changeKeybindsButton.setName("Change Keybinds");
        
        myMainPanel.add(changeKeybindsButton);
        changeKeybindsButton.addActionListener(this);
    }
    
    public JPanel getPanel()
    {
        return myMainPanel;
    }
    
    public JComponent getPreferredFocusedComponent()
    {
        return jumpAmount;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        int jumpAmount;
        
        
        switch (e.getActionCommand())
        {
            case "Create Jump":
                try
                {
                    String jumpAmountText = this.jumpAmount.getText();
                    jumpAmount = Integer.parseInt(jumpAmountText);
                } catch (NumberFormatException ex)
                {
                    //TODO create message saying text must be an int.
                    ex.printStackTrace();
                    return;
                }
                JumpManager.AddJumpAction(jumpAmount);
                break;
            case "Remove Jump":
                try
                {
                    String jumpAmountText = this.jumpAmount.getText();
                    jumpAmount = Integer.parseInt(jumpAmountText);
                } catch (NumberFormatException ex)
                {
                    //TODO create message saying text must be an int.
                    ex.printStackTrace();
                    return;
                }
                JumpManager.RemoveJumpAction(jumpAmount);
                break;
            case "Change Keybinds":
                Settings settings = DataManager.getInstance().getDataContext((Component) e.getSource()).getData(Settings.KEY);
                if (settings != null)
                {
                    Configurable configurable = settings.find("preferences.keymap");
                    settings.select(configurable, '"' + "Jump" + '"').doWhenDone(() ->
                    {
                        // Remove once https://youtrack.jetbrains.com/issue/IDEA-212247 is fixed
                        EdtExecutorService.getScheduledExecutorInstance().schedule(() ->
                        {
                            settings.select(configurable, '"' + "Jump" + '"');
                        }, 100, TimeUnit.MILLISECONDS);
                    });
                }
                break;
        }
        myMainPanel.updateUI();
    }
}
