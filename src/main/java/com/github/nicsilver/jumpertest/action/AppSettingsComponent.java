package com.github.nicsilver.jumpertest.action;

import com.intellij.ide.DataManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ex.Settings;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.concurrency.EdtExecutorService;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AppSettingsComponent implements ActionListener
{
    private final int fontSize = 20;
    private final JPanel myMainPanel = new JPanel();
    private final JBTextField jumpAmount = new JBTextField();
    private final JLabel jumpCountLabel = createJLabelWithFontSize("", fontSize);
    
    public AppSettingsComponent()
    {
        
        JPanel flow1 = new JPanel();
        JPanel flow2 = new JPanel();
        JPanel flow3 = new JPanel();
        
        
        for (JPanel jPanel : Arrays.asList(flow1, flow2, flow3))
        {
            jPanel.setLayout(new HorizontalLayout());
        }
        
        myMainPanel.setLayout(new VerticalFlowLayout());
        JLabel jLabel = createJLabelWithFontSize("Enter jump amount: ", fontSize);
        JButton createJumpButton = new JButton("Create Jump");
        createJumpButton.setName("Create Jump");
        
        flow1.add(jLabel);
        flow1.add(jumpAmount);
        flow1.add(createJumpButton);
        
        JButton removeJumpButton = new JButton("Remove Jump");
        removeJumpButton.setName("Remove Jump");

//        flow1.add(separator);
        flow1.add(createJLabelWithFontSize(" | ", fontSize));
        flow1.add(removeJumpButton);
        removeJumpButton.addActionListener(this);
        createJumpButton.addActionListener(this);
        
        JLabel currentlyAddedJumps = createJLabelWithFontSize("Currently added jumps:  ", fontSize);
        flow2.add(currentlyAddedJumps);
        
        
        flow2.add(jumpCountLabel);
        changeJumpCountLabel();
        
        JButton changeKeybindsButton = new JButton("Change Keybinds");
        changeKeybindsButton.setName("Change Keybinds");
        changeKeybindsButton.addActionListener(AppSettingsComponent::changeKeybindsListener);
        
        flow3.add(changeKeybindsButton);
        
        myMainPanel.add(flow1);
        myMainPanel.add(flow2);
        myMainPanel.add(flow3);
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
        String actionCommand = e.getActionCommand();
        
        try
        {
            String jumpAmountText = this.jumpAmount.getText();
            jumpAmount = Integer.parseInt(jumpAmountText);
        } catch (NumberFormatException ex)
        {
            Messages.showErrorDialog("Invalid number", "Invalid Number");
            return;
//            ex.printStackTrace();
        }
        
        if (actionCommand.equals("Create Jump"))
        {
            if (!JumperState.getInstance().jumpCaretActions.contains(jumpAmount))
            {
                JumpManager.AddJumpAction(jumpAmount);
            }
        }
        else if (actionCommand.equals("Remove Jump"))
        {
            if (JumperState.getInstance().jumpCaretActions.contains(jumpAmount))
            {
                JumpManager.RemoveJumpAction(jumpAmount);
            }
        }
        changeJumpCountLabel();
    }
    
    private static void changeKeybindsListener(ActionEvent e)
    {
        Settings settings = DataManager.getInstance().getDataContext((Component) e.getSource()).getData(Settings.KEY);
        if (settings != null)
        {
            //This is taken from Intellij Terminal plugin. It's fucked, ohh well.
            Configurable configurable = settings.find("preferences.keymap");
            settings.select(configurable, '"' + "Jump" + '"').doWhenDone(() ->
                    EdtExecutorService.getScheduledExecutorInstance().schedule(() ->
                    {
                        settings.select(configurable, '"' + "Jump" + '"');
                    }, 100, TimeUnit.MILLISECONDS));
        }
    }
    
    private void changeJumpCountLabel()
    {
        StringBuilder labelText = new StringBuilder();
        
        for (Integer jumpCaretAction : JumperState.getInstance().jumpCaretActions)
        {
            labelText.append(jumpCaretAction).append(" :");
        }
        
        this.jumpCountLabel.setText(labelText.toString());
    }
    
    private JLabel createJLabelWithFontSize(String text, int size)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("", Font.PLAIN, size));
        return label;
    }
}
