package psp.resultManagement;

public class TimeResult {

	private int m_iTime;
	private Double m_dPuissanceTurbine;
	private Boolean m_dModeTurbine;
	private Double m_dPuissancePompe;
	private Boolean m_dModePompe;
	private Double m_dVariationHauteur;
	private Boolean m_dBat;
	private Boolean m_dBta;
	private Boolean m_dBap;
	private Boolean m_dBpa;

	public TimeResult(int t, Double Ptt, Double Mtt , Double Ppt, Double Mpt, Double Ht, Double Bat, Double Bta, Double Bap, Double Bpa){
		m_iTime = t;
		m_dPuissanceTurbine = Ptt;
		m_dModeTurbine = (Mtt == 1);
		m_dPuissancePompe = Ppt;
		m_dModePompe = (Mpt == 1);
		m_dVariationHauteur = Ht;
		m_dBat = (Bat == 1);
		m_dBta = (Bta == 1);
		m_dBap = (Bap == 1);
		m_dBpa = (Bpa == 1);
	}
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
		
		if (m_dBat != null && m_dBat)
			sRes += "\r\n\t\tChangement arret vers turbine";
		if (m_dBta != null && m_dBta)
			sRes += "\r\n\t\tChangement turbine vers arret";
		if (m_dBap != null && m_dBap)
			sRes += "\r\n\t\tChangement arret vers pompe";
		if (m_dBpa != null && m_dBpa)
			sRes += "\r\n\t\tChangement pompe vers arret";
		
		if (m_dVariationHauteur != null)
			sRes += "\r\n\t\tVariation de hauteur : " + m_dVariationHauteur;
		sRes += "\r\n\t--------------------------------------------------------";
		
		return sRes;
	}
	
	
}
