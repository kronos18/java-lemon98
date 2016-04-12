package psp.resultManagement;

public class TimeResult {

	private int m_iTime;
	private Double m_dPuissanceTurbine;
	private Boolean m_dModeTurbine;
	private Double m_dPuissancePompe;
	private Boolean m_dModePompe;
	private Double m_dVariationHauteur;

	public TimeResult(int t, Double Ptt, Double Mtt , Double Ppt, Double Mpt){
		m_iTime = t;
		m_dPuissanceTurbine = Ptt;
		m_dModeTurbine = (Mtt == 1);
		m_dPuissancePompe = Ppt;
		m_dModePompe = (Mpt == 1);
		m_dVariationHauteur = null;
	}
	
	public TimeResult(int t, Double Ptt, Double Mtt , Double Ppt, Double Mpt, Double Ht){
		m_iTime = t;
		m_dPuissanceTurbine = Ptt;
		m_dModeTurbine = (Mtt == 1);
		m_dPuissancePompe = Ppt;
		m_dModePompe = (Mpt == 1);
		m_dVariationHauteur = Ht;
	}
	
	@Override
	public String toString(){
		String sRes = "\r\n\tIterration numéro " + m_iTime;
		
		if (m_dModeTurbine)
			sRes += "\r\n\t\tMode Turbine.\r\n\t\tPuissance = " + m_dPuissanceTurbine;
		else if (m_dModePompe)
			sRes += "\r\n\t\tMode Pompe.\r\n\t\tPuissance = " + m_dPuissancePompe;
		else
			sRes += "\r\n\t\tMode arrêt...";
		
		if (m_dVariationHauteur != null)
			sRes += "\r\n\t\tVariation de hauteur : " + m_dVariationHauteur;
		sRes += "\r\n\t--------------------------------------------------------";
		
		return sRes;
	}
	
	
}
