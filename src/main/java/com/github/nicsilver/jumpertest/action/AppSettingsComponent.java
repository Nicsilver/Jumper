package com.github.nicsilver.jumpertest.action;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        
        if (e.getActionCommand().equals("Create Jump"))
        {
            JumpManager.AddJumpAction(jumpAmount);
        }
        else if (e.getActionCommand().equals("Remove Jump"))
        {
            JumpManager.RemoveJumpAction(jumpAmount);
        }
        myMainPanel.updateUI();
    }
}
