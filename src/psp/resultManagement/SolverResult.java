package psp.resultManagement;

import static org.junit.Assert.fail;

import java.util.ArrayList;

public class SolverResult {

	private Boolean m_bFailed;
	private String m_sSolutionStatus;
	private Double m_dObjValue;
	private ArrayList<TimeResult> m_lAllResults;

	public SolverResult(Boolean bFailed) {
		m_bFailed = bFailed;
		m_sSolutionStatus = "";
		m_dObjValue = 0.0;
		m_lAllResults = new ArrayList<TimeResult>();
	}

	@Override
	public String toString() {
		String sRes = "";

		if (m_bFailed)
			sRes += "Echec du calcul...";
		else {
			sRes += "Le calcul a réussi !";
			if (m_sSolutionStatus == "Optimal")
				sRes += " Une solution optimale a été trouvée.\n";
			else
				sRes += "Status de la solution : " + m_sSolutionStatus + ".\n";

			sRes += "Gain obtenu pour le fonctionnement des pompes-turbines : " + m_dObjValue + " euros.\n";
			
			sRes += "\nDétails du fonctionnement des pompes-turbines :\n";
			
			for(final TimeResult res : m_lAllResults)
				sRes += res.toString();	
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