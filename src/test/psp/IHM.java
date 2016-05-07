package test.psp;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import psp.Instance;
import psp.Mip;
import psp.Reservoir;
import psp.TurbinePompe;
import psp.resultManagement.SolverResult;

import javax.swing.JScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IHM {

	private JFrame m_mainFrame;
	private JPanel m_panelMainContainer;
	private JPanel m_panelOptionTurbine;
	private JPanel m_panelOptionPompe;
	private JPanel m_panelCoutChangementMode;
	private JPanel m_panelOptionReservoirs;
	private JPanel m_panelOptionCheckbox;
	private JPanel m_panelOptionRefroidissement;
	private JTextArea m_textareaResults;
	private JScrollPane m_scrollPane;
	private JCheckBox m_checkboxContrainteReservoir;
	private JCheckBox m_checkboxContrainteCoutChangement;
	private JCheckBox m_checkboxContrainteRefroidissement;
	private JCheckBox m_checkboxContrainteRegulation;
	private JTextField m_textfieldP_T_Min;
	private JTextField m_textfieldP_T_Max;
	private JTextField m_textfieldP_P_Min;
	private JTextField m_textfieldP_P_Max;
	private JTextField m_textfieldAlpha_T;
	private JTextField m_textfieldAlpha_P;
	private JTextField m_textfieldCout_A_T;
	private JTextField m_textfieldCout_T_A;
	private JTextField m_textfieldCout_A_P;
	private JTextField m_textfieldCout_P_A;
	private JTextField m_textfieldHauteur;
	private JTextField m_textfieldLargeur;
	private JTextField m_textfieldLongueur;
	private JTextField m_textfieldH_0_sup;
	private JTextField m_textfieldH_0_inf;
	private JTextField m_textfieldDelta_H;
	private JTextField m_textfieldNbHoursRefroidissement;
	private JButton m_btnLancerLeCalcul;
	private Instance m_instance;
	
	public IHM(){
		//Initialise le panel principale
		initMainFrame();
		
		//Panel Option checkbox
		initPanelOptionCheckbox();
		
		//Panel Option puissance Turbine Pompe
		initOptionPompeTurbine();
		
		//Panel option cout changement de mode
		initOptionCoutChangementMode();
		
		//Panel option reservoirs
		initOptionReservoirs();
		
		//Panel option refroidissement
		initOptionRefroidissement();
		
		//Initialise la zone de texte pour afficher les resultats
		initLogTextArea();
		
		//Initialise le bouton permettant de lancer le calcul
		initButtonLancerCalcul();
		
		m_mainFrame.setVisible(true);
	}
	
	private void initMainFrame(){
		m_mainFrame = new JFrame("A JFrame");
		m_mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Max\\Documents\\M2_MIAGE\\Analyse Fonctionnelle Energie\\Guillaume\\psp_student\\Data\\icon.png"));
		m_mainFrame.setTitle("Module d'optimisation pompe-turbine");
		m_mainFrame.setSize(900, 700);
		m_mainFrame.setLocation(300, 50);
		m_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_mainFrame.getContentPane().add(BorderLayout.CENTER, new JTextArea(10, 40));
		
		//Panel container
		m_panelMainContainer = new JPanel(new BorderLayout());
		m_panelMainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
		m_panelMainContainer.setLayout(new BoxLayout(m_panelMainContainer, BoxLayout.PAGE_AXIS));
		m_mainFrame.setContentPane(m_panelMainContainer);
	}

	private void initPanelOptionCheckbox(){
		//Border du panel 		
		m_panelOptionCheckbox = new JPanel();
		m_panelOptionCheckbox.setBorder(BorderFactory.createTitledBorder("Activation / Desactivation des contraintes")); 
		m_panelOptionCheckbox.setLayout(new GridLayout(0,2));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));		

	    //Checkbox reservoir
	    m_checkboxContrainteReservoir = new JCheckBox("Reservoir");
	    leftPanel.add(m_checkboxContrainteReservoir);

	    //Checkbox cout changement
	    m_checkboxContrainteCoutChangement = new JCheckBox("Cout Changement");
	    leftPanel.add(m_checkboxContrainteCoutChangement);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));		

	    //Checkbox Refroidissement
	    m_checkboxContrainteRefroidissement = new JCheckBox("Refroidissement");
	    rightPanel.add(m_checkboxContrainteRefroidissement);

	    //Cpa
	    m_checkboxContrainteRegulation = new JCheckBox("Regulation");
	    rightPanel.add(m_checkboxContrainteRegulation);

	    m_panelOptionCheckbox.add(leftPanel);
	    m_panelOptionCheckbox.add(rightPanel);
		m_panelMainContainer.add(m_panelOptionCheckbox);
	}
	
	private void initOptionPompeTurbine(){
		JPanel horizontalPanelOptionTurbinePompe = new JPanel();
		horizontalPanelOptionTurbinePompe.setLayout(new GridLayout(0,2));
		
		//Border du panel option turbine		
		m_panelOptionTurbine = new JPanel();
	    m_panelOptionTurbine.setBorder(BorderFactory.createTitledBorder("Options Turbine")); 
	    m_panelOptionTurbine.setLayout(new BoxLayout(m_panelOptionTurbine, BoxLayout.PAGE_AXIS));

	    //Puissance min
	    JPanel line1 = new JPanel();
	    m_textfieldP_T_Min = new JTextField("", 15);
	    line1.add(new JLabel("Puissance minimale : "));
	    line1.add(m_textfieldP_T_Min);
	    m_panelOptionTurbine.add(line1);

	    //Puissance max
	    JPanel line2 = new JPanel();
	    m_textfieldP_T_Max = new JTextField("", 15);
	    line2.add(new JLabel("Puissance maximale : "));
	    line2.add(m_textfieldP_T_Max);
	    m_panelOptionTurbine.add(line2);

	    //Alpha T
	    JPanel line3 = new JPanel();
	    m_textfieldAlpha_T = new JTextField("", 15);
	    line3.add(new JLabel("Alpha_T : "));
	    line3.add(m_textfieldAlpha_T);
	    m_panelOptionTurbine.add(line3);
	    	    
		

		//Border du panel option pompe		
		m_panelOptionPompe = new JPanel();
	    m_panelOptionPompe.setBorder(BorderFactory.createTitledBorder("Options Pompe")); 
	    m_panelOptionPompe.setLayout(new BoxLayout(m_panelOptionPompe, BoxLayout.PAGE_AXIS));

	    //Puissance min
	    JPanel linep1 = new JPanel();
	    m_textfieldP_P_Min = new JTextField("", 15);
	    linep1.add(new JLabel("Puissance minimale : "));
	    linep1.add(m_textfieldP_P_Min);
	    m_panelOptionPompe.add(linep1);

	    //Puissance max
	    JPanel linep2 = new JPanel();
	    m_textfieldP_P_Max = new JTextField("", 15);
	    linep2.add(new JLabel("Puissance maximale : "));
	    linep2.add(m_textfieldP_P_Max);
	    m_panelOptionPompe.add(linep2);

	    //Alpha P
	    JPanel linep3 = new JPanel();
	    m_textfieldAlpha_P = new JTextField("", 15);
	    linep3.add(new JLabel("Alpha_P : "));
	    linep3.add(m_textfieldAlpha_P);
	    m_panelOptionPompe.add(linep3);

	    horizontalPanelOptionTurbinePompe.add(m_panelOptionTurbine);
	    horizontalPanelOptionTurbinePompe.add(m_panelOptionPompe);
		m_panelMainContainer.add(horizontalPanelOptionTurbinePompe);
	}
	
	private void initOptionCoutChangementMode(){
		//Border du panel 		
		m_panelCoutChangementMode = new JPanel();
		m_panelCoutChangementMode.setBorder(BorderFactory.createTitledBorder("Couts changement de mode")); 
		m_panelCoutChangementMode.setLayout(new GridLayout(0,2));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));		

	    //Cat
	    JPanel line1 = new JPanel();
	    m_textfieldCout_A_T = new JTextField("", 15);
	    line1.add(new JLabel("Cout Arret -> Turbine : "));
	    line1.add(m_textfieldCout_A_T);
	    leftPanel.add(line1);

	    //Cta
	    JPanel line2 = new JPanel();
	    m_textfieldCout_T_A = new JTextField("", 15);
	    line2.add(new JLabel("Cout Turbine -> Arret : "));
	    line2.add(m_textfieldCout_T_A);
	    leftPanel.add(line2);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));		

	    //Cap
	    JPanel line3 = new JPanel();
	    m_textfieldCout_A_P = new JTextField("", 15);
	    line3.add(new JLabel("Cout Arret -> Pompe : "));
	    line3.add(m_textfieldCout_A_P);
	    rightPanel.add(line3);

	    //Cpa
	    JPanel line4 = new JPanel();
	    m_textfieldCout_P_A = new JTextField("", 15);
	    line4.add(new JLabel("Cout Pompe -> Arret : "));
	    line4.add(m_textfieldCout_P_A);
	    rightPanel.add(line4);

	    m_panelCoutChangementMode.add(leftPanel);
	    m_panelCoutChangementMode.add(rightPanel);
		m_panelMainContainer.add(m_panelCoutChangementMode);
	}
	
	private void initOptionReservoirs(){
		//Border du panel 		
		m_panelOptionReservoirs = new JPanel();
		m_panelOptionReservoirs.setBorder(BorderFactory.createTitledBorder("Caracteristiques des reservoirs")); 
		m_panelOptionReservoirs.setLayout(new GridLayout(0,2));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));		

	    //Hauteur
	    JPanel line1 = new JPanel();
	    m_textfieldHauteur = new JTextField("", 15);
	    line1.add(new JLabel("Hauteur : "));
	    line1.add(m_textfieldHauteur);
	    leftPanel.add(line1);

	    //Longueur
	    JPanel line2 = new JPanel();
	    m_textfieldLongueur = new JTextField("", 15);
	    line2.add(new JLabel("Longueur : "));
	    line2.add(m_textfieldLongueur);
	    leftPanel.add(line2);

	    //Largeur
	    JPanel line3 = new JPanel();
	    m_textfieldLargeur = new JTextField("", 15);
	    line3.add(new JLabel("Largeur : "));
	    line3.add(m_textfieldLargeur);
	    leftPanel.add(line3);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		
	    //H_0_sup
	    JPanel line4 = new JPanel();
	    m_textfieldH_0_sup = new JTextField("", 15);
	    line4.add(new JLabel("H_0_sup : "));
	    line4.add(m_textfieldH_0_sup);
	    rightPanel.add(line4);

	    //H_0_inf
	    JPanel line5 = new JPanel();
	    m_textfieldH_0_inf = new JTextField("", 15);
	    line5.add(new JLabel("H_0_inf : "));
	    line5.add(m_textfieldH_0_inf);
	    rightPanel.add(line5);

	    //H_0_inf
	    JPanel line6 = new JPanel();
	    m_textfieldDelta_H = new JTextField("", 15);
	    line6.add(new JLabel("Delta_H : "));
	    line6.add(m_textfieldDelta_H);
	    rightPanel.add(line6);

	    m_panelOptionReservoirs.add(leftPanel);
	    m_panelOptionReservoirs.add(rightPanel);
		m_panelMainContainer.add(m_panelOptionReservoirs);
	}

	private void initOptionRefroidissement(){
		//Border du panel 		
		m_panelOptionRefroidissement = new JPanel();
		m_panelOptionRefroidissement.setBorder(BorderFactory.createTitledBorder("Caracteristiques de refroidissement")); 
		m_panelOptionRefroidissement.setLayout(new GridLayout(0,2));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));		

	    //NbHoursRefroidissement
	    JPanel line1 = new JPanel();
	    m_textfieldNbHoursRefroidissement = new JTextField("", 15);
	    line1.add(new JLabel("Nombre d'heures avant surchauffe : "));
	    line1.add(m_textfieldNbHoursRefroidissement);
	    leftPanel.add(line1);


		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		
		
		m_panelOptionRefroidissement.add(leftPanel);
		m_panelOptionRefroidissement.add(rightPanel);
		m_panelMainContainer.add(m_panelOptionRefroidissement);
	}
	
	private void initLogTextArea(){
		m_textareaResults = new JTextArea(20, 10);
		m_textareaResults.setEditable(false);

		//Scroll pane for the textPane
		m_scrollPane = new JScrollPane(m_textareaResults);
		m_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Add textPane with scrollBar to the view
		m_panelMainContainer.add(m_scrollPane);
	}
	
	private void initButtonLancerCalcul(){		
		JPanel panelButton = new JPanel();
		panelButton.setBorder(new EmptyBorder(30, 0, 10, 0));
		panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.LINE_AXIS));
		
		m_btnLancerLeCalcul = new JButton();
		m_btnLancerLeCalcul.setText("Lancer le calcul");
		
		m_btnLancerLeCalcul.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		    	  cleanIHMBeforeSolve();
		    	  configInstanceFromIHM();
		    	  Mip mip = null;
		    	  
			  		try{
			  			mip = new Mip(m_instance);
			  		} catch(Exception exc){
			  			showMessageDialogError(exc, "Erreur lors de l'initialisation du modele.\n" + exc.getMessage());
			  			return;
			  		}
			  		
			  		SolverResult res = null;
		  		
		  		try{
		  			res = mip.solve();
				} catch(Exception exc){
					showMessageDialogError(exc, "Erreur lors du calcul de cplex.\n" + exc.getMessage());
					return;
				}
				
				//Update l'IHM une fois que les calculs sont termines pour afficher les resultats.
				if (res != null)
					updateIHMAfterSolve(res);
		      }
		    });
		
		panelButton.add(m_btnLancerLeCalcul);
		
		m_panelMainContainer.add(panelButton);
	}
	
	public void updateIHMAfterSolve(SolverResult res){
		m_textareaResults.setText(res.toString());
	}

	public void cleanIHMBeforeSolve(){
		m_textareaResults.setText("");
	}

	public void updateIHMWithValuesInFile(Instance instance){
		m_instance = instance;
		TurbinePompe tp = instance.getTP();
		Reservoir reservoirInf = instance.getInf();
		Reservoir reservoirSup = instance.getSup();
		
		m_checkboxContrainteReservoir.setSelected(instance.isContrainteReservoirActivated());
		m_checkboxContrainteCoutChangement.setSelected(instance.isContrainteCoutChangementActivated());
		m_checkboxContrainteRefroidissement.setSelected(instance.isContrainteRefroidissementActivated());
		m_checkboxContrainteRegulation.setSelected(instance.isContrainteRegulationActivated());
		m_textfieldP_T_Min.setText(String.valueOf(tp.getP_T_min()));
		m_textfieldP_T_Max.setText(String.valueOf(tp.getP_T_max()));
		m_textfieldP_P_Min.setText(String.valueOf(tp.getP_P_min()));
		m_textfieldP_P_Max.setText(String.valueOf(tp.getP_P_max()));
		m_textfieldAlpha_T.setText(String.valueOf(tp.getAlpha_T()));
		m_textfieldAlpha_P.setText(String.valueOf(tp.getAlpha_P()));
		m_textfieldCout_A_T.setText(String.valueOf(tp.getC_AT()));
		m_textfieldCout_T_A.setText(String.valueOf(tp.getC_TA()));
		m_textfieldCout_A_P.setText(String.valueOf(tp.getC_AP()));
		m_textfieldCout_P_A.setText(String.valueOf(tp.getC_PA()));
		m_textfieldHauteur.setText(String.valueOf(reservoirSup.getHauteur()));
		m_textfieldLargeur.setText(String.valueOf(reservoirSup.getLargeur()));
		m_textfieldLongueur.setText(String.valueOf(reservoirSup.getLongueur()));
		m_textfieldH_0_sup.setText(String.valueOf(reservoirSup.getH_0()));
		m_textfieldH_0_inf.setText(String.valueOf(reservoirInf.getH_0()));
		m_textfieldDelta_H.setText(String.valueOf(instance.getDelta_H()));
		m_textfieldNbHoursRefroidissement.setText(String.valueOf(instance.getNbHoursRefroidissement()));
	}
	
	private void configInstanceFromIHM(){
		try {
			m_instance.setContrainteReservoirActivated(m_checkboxContrainteReservoir.isSelected());
			m_instance.setContrainteCoutChangementActivated(m_checkboxContrainteCoutChangement.isSelected());
			m_instance.setContrainteRefroidissementActivated(m_checkboxContrainteRefroidissement.isSelected());
			m_instance.setContrainteRegulationActivated(m_checkboxContrainteRegulation.isSelected());
			m_instance.setDelta_H(Double.valueOf(m_textfieldDelta_H.getText()));
			m_instance.getInf().setH_0(Double.valueOf(m_textfieldH_0_inf.getText()));
			m_instance.getSup().setH_0(Double.valueOf(m_textfieldH_0_sup.getText()));
			m_instance.getSup().setHauteur(Double.valueOf(m_textfieldHauteur.getText()));
			m_instance.getInf().setHauteur(Double.valueOf(m_textfieldHauteur.getText()));
			m_instance.getSup().setLongueur(Double.valueOf(m_textfieldLongueur.getText()));
			m_instance.getInf().setLongueur(Double.valueOf(m_textfieldLongueur.getText()));
			m_instance.getSup().setLargeur(Double.valueOf(m_textfieldLargeur.getText()));
			m_instance.getInf().setLargeur(Double.valueOf(m_textfieldLargeur.getText()));
			m_instance.getTP().setC_PA(Double.valueOf(m_textfieldCout_P_A.getText()));
			m_instance.getTP().setC_AP(Double.valueOf(m_textfieldCout_A_P.getText()));
			m_instance.getTP().setC_AT(Double.valueOf(m_textfieldCout_A_T.getText()));
			m_instance.getTP().setC_TA(Double.valueOf(m_textfieldCout_T_A.getText()));
			m_instance.getTP().setAlpha_P(Double.valueOf(m_textfieldAlpha_P.getText()));
			m_instance.getTP().setAlpha_T(Double.valueOf(m_textfieldAlpha_T.getText()));
			m_instance.getTP().setP_P_max(Double.valueOf(m_textfieldP_P_Max.getText()));
			m_instance.getTP().setP_P_min(Double.valueOf(m_textfieldP_P_Min.getText()));
			m_instance.getTP().setP_T_max(Double.valueOf(m_textfieldP_T_Max.getText()));
			m_instance.getTP().setP_T_min(Double.valueOf(m_textfieldP_T_Min.getText()));
			m_instance.setNbHoursRefroidissement(Integer.valueOf(m_textfieldNbHoursRefroidissement.getText()));
		} catch (Exception exc) {
			showMessageDialogError(exc, "Mauvaises saisies !!!");
		}
	}
	
	public void showMessageDialogError(Exception exc, String sMessageSup) {
		JOptionPane.showMessageDialog(m_mainFrame, "Une erreur a ete rencontree :\n" + sMessageSup, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
}
