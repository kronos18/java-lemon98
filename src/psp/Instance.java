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
	
}
