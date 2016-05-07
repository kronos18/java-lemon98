package psp;

/**
 * Classe représetant une instance
 */
public class Instance {
	private TurbinePompe[] tPs;
	private Reservoir sup;
	private Reservoir inf;
	private double[] cout;
	private double[] regulation;
	private double delta_H;
    private boolean m_bReservoir       = true;
    private boolean m_bCoutChangement  = true;
    private boolean m_bRefroidissement = true;
    private boolean m_bRegulation      = true;
    private int m_iNbHoursRefroidissement      = 12;

	
	public Instance(TurbinePompe[] tps, Reservoir sup, Reservoir inf, double[] cout, double[] regulation, double delta_H) {
		this.tPs = tps;
		this.sup = sup;
		this.inf = inf;
		this.cout = cout;
		this.regulation = regulation;
		this.delta_H = delta_H;
	}

	public TurbinePompe getTP() {
		return tPs[0];
	}
	
	public TurbinePompe[] getTPs() {
		return tPs;
	}

	public Reservoir getSup() {
		return sup;
	}

	public Reservoir getInf() {
		return inf;
	}

	public double[] getCout() {
		return cout;
	}
	
	public double getDelta_H() {
		return this.delta_H;
	}
	
	public double[] getRegulation() {
		return regulation;
	}

	public void setDelta_H(double deltaH) {
		this.delta_H = deltaH;
	}

	public boolean isContrainteReservoirActivated() {
		return m_bReservoir;
	}

	public boolean isContrainteCoutChangementActivated() {
		return m_bCoutChangement;
	}

	public boolean isContrainteRefroidissementActivated() {
		return m_bRefroidissement;
	}

	public boolean isContrainteRegulationActivated() {
		return m_bRegulation;
	}

	public void setContrainteReservoirActivated(boolean bActivated) {
		m_bReservoir = bActivated;
	}

	public void setContrainteCoutChangementActivated(boolean bActivated) {
		m_bCoutChangement = bActivated;
	}

	public void setContrainteRefroidissementActivated(boolean bActivated) {
		m_bRefroidissement = bActivated;
	}

	public void setContrainteRegulationActivated(boolean bActivated) {
		m_bRegulation = bActivated;
	}

	public int getNbHoursRefroidissement() {
		return m_iNbHoursRefroidissement;
	}

	public void setNbHoursRefroidissement(int iNbHoursRefroidissement) {
		m_iNbHoursRefroidissement = iNbHoursRefroidissement;
	}

}
