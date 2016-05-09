package psp.resultManagement;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.sun.java.swing.plaf.windows.WindowsDesktopManager;

public class SolverResult {

	private Boolean m_bFailed;
	private String m_sSolutionStatus;
	private Double m_dObjValue;
	private int m_iNbPSP;
	private ArrayList<TimeResult> m_lAllResults;

	public SolverResult(Boolean bFailed, int iNbPSP) {
		m_bFailed = bFailed;
		m_sSolutionStatus = "";
		m_dObjValue = 0.0;
		m_iNbPSP = iNbPSP;
		m_lAllResults = new ArrayList<TimeResult>();
	}

	@Override
	public String toString() {
		String sRes = "";

		if (m_bFailed)
			sRes += "Echec du calcul...";
		else {
			sRes += getShortString();

			sRes += "\nDétails du fonctionnement d'une pompe-turbine :\n";
			
			for(final TimeResult res : m_lAllResults)
				sRes += res.toString();	
		}

		return sRes;

	}
	
	public String getShortString()	{
		String sRes = "";

		if (!m_bFailed){
			if (m_sSolutionStatus == "Optimal")
				sRes += "Une solution optimale a été trouvée.\n";
			else
				sRes += "Status de la solution : " + m_sSolutionStatus + ".\n";

			String sObjValue = new java.text.DecimalFormat("0.##").format(m_dObjValue);
			sObjValue = sObjValue.replace(".", ",");
			
			sRes += "Gain obtenu pour le fonctionnement d'une pompe-turbine : " + sObjValue + " euros.\n";

			String sObjValueParTurbine = new java.text.DecimalFormat("0.##").format(m_dObjValue / m_iNbPSP);
			sObjValueParTurbine = sObjValueParTurbine.replace(".", ",");
			
			sRes += "Gain obtenu par pompes-turbines : " + sObjValueParTurbine + " euros.\n";
		}

		return sRes;
	}

	public ArrayList<TimeResult> getAllResults() {
		return m_lAllResults;
	}

	public void addTimeResult(TimeResult item) {
		m_lAllResults.add(item);
	}

	public Boolean bIsFailed() {
		return m_bFailed;
	}

	public String getSolutionStatus() {
		return m_sSolutionStatus;
	}

	public void setSolutionStatus(String sStatus) {
		m_sSolutionStatus = sStatus;
	}

	public Double getObjValue() {
		return m_dObjValue;
	}

	public void setObjValue(Double dValue) {
		m_dObjValue = dValue;
	}
}