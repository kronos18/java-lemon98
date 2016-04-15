package test.psp;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import psp.resultManagement.SolverResult;

import javax.swing.JScrollPane;
import java.awt.Toolkit;

public class IHM {

	private JFrame m_mainFrame;
	private JPanel m_panelTop;
	private JLabel m_labelBonjour;
	private JTextArea m_textareaResults;
	private JScrollPane m_scrollPane;
	
	public IHM(){
		m_mainFrame = new JFrame("A JFrame");
		m_mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Max\\Documents\\M2_MIAGE\\Analyse Fonctionnelle Energie\\Guillaume\\psp_student\\Data\\icon.png"));
		m_mainFrame.setTitle("Module d'optimisation pompe-turbine");
		m_mainFrame.setSize(600, 409);
		m_mainFrame.setLocation(300, 200);
		m_mainFrame.getContentPane().add(BorderLayout.CENTER, new JTextArea(10, 40));
		
		//Panel du haut
		m_panelTop = new JPanel(new BorderLayout());
		m_panelTop.setBorder(new EmptyBorder(10, 10, 10, 10));
		m_mainFrame.setContentPane(m_panelTop);
		
		//Label Bonjour, padding bottom = 10
		m_labelBonjour = new JLabel("Bonjour !");
		m_labelBonjour.setBorder(new EmptyBorder(0, 0, 10, 0));
		m_panelTop.add(m_labelBonjour, BorderLayout.NORTH);

		//zone de texte pour afficher les resultats
		m_textareaResults = new JTextArea(20, 10);
		m_textareaResults.setEditable(false);

		//Scroll pane for the textPane
		m_scrollPane = new JScrollPane(m_textareaResults);
		m_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Add textPane with scrollBar to the view
		m_panelTop.add(m_scrollPane);

		
		m_mainFrame.setVisible(true);
	}

	public void UpdateIHMAfterSolve(SolverResult res){
		m_textareaResults.setText(res.toString());
	}

	public void showMessageDialogError(Exception exc) {
		JOptionPane.showMessageDialog(m_mainFrame, "Une erreur a été rencontrée.", "Erreur", JOptionPane.ERROR_MESSAGE);
		
	}
}
