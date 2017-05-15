/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Suliman
 */
public class About 
{

	private final int height = 150;
	private final int width = 300;
	private final JFrame mainFrame = new JFrame("About");
	private final JPanel mainPanel = new JPanel();
	private final JTextArea text = new JTextArea("This application was developed by: ");
	private final JTextArea text1 = new JTextArea("Ahmed Alsinan\nMurtada Almutawah\nSuliman Alothman");
	
	public About()
	{
		mainFrame.setSize(width, height);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.add(mainPanel, BorderLayout.CENTER);
		
		mainPanel.setLayout(new FlowLayout());
		//mainPanel.setBackground(Color.WHITE);
		text.setBackground(null);
		text.setFont(text.getFont().deriveFont(16f));
		text.setFont(text.getFont().deriveFont(2));
		
		text1.setBackground(null);
		text1.setFont(text1.getFont().deriveFont(14f));
		text1.setFont(text1.getFont().deriveFont(1));
		
		mainPanel.add(text, BorderLayout.NORTH);
		mainPanel.add(text1, BorderLayout.WEST);
		
		mainFrame.setVisible(true);
		
	}
	
}