package psp;

/**
 * Classe repr?sentant une turbine pompe
 */
public class TurbinePompe {

    private double p_P_min, p_P_max, p_T_min, p_T_max;
    private double c_AP, c_PA, c_AT, c_TA;
    private double alpha_P, alpha_T;

    public TurbinePompe(double p_P_min, double p_P_max, double p_T_min, double p_T_max, double c_AP, double c_PA,
                        double c_AT, double c_TA, double alpha_P, double alpha_T) {
        this.p_P_min = p_P_min;
        this.p_P_max = p_P_max;
        this.p_T_min = p_T_min;
        this.p_T_max = p_T_max;
        this.c_AP = c_AP;
        this.c_PA = c_PA;
        this.c_AT = c_AT;
        this.c_TA = c_TA;
        this.alpha_P = alpha_P;
        this.alpha_T = alpha_T;
    }

    public double getP_P_min() {
        return p_P_min;
    }

    public void setP_P_min(double p_P_min) {
        this.p_P_min = p_P_min;
    }

    public double getP_P_max() {
        return p_P_max;
    }

    public void setP_P_max(double p_P_max) {
        this.p_P_max = p_P_max;
    }

    public double getP_T_min() {
        return p_T_min;
    }

    public void setP_T_min(double p_T_min) {
        this.p_T_min = p_T_min;
    }

    public double getP_T_max() {
        return p_T_max;
    }

    public void setP_T_max(double p_T_max) {
        this.p_T_max = p_T_max;
    }

    public double getC_AP() {
        return c_AP;
    }

    public void setC_AP(double c_AP) {
        this.c_AP = c_AP;
    }

    public double getC_PA() {
        return c_PA;
    }

    public void setC_PA(double c_PA) {
        this.c_PA = c_PA;
    }

    public double getC_AT() {
        return c_AT;
    }

    public void setC_AT(double c_AT) {
        this.c_AT = c_AT;
    }

    public double getC_TA() {
        return c_TA;
    }

    public void setC_TA(double c_TA) {
        this.c_TA = c_TA;
    }

    public double getAlpha_P() {
        return alpha_P;
    }

    public void setAlpha_P(double alpha_P) {
        this.alpha_P = alpha_P;
    }

    public double getAlpha_T() {
        return alpha_T;
    }

    public void setAlpha_T(double alpha_T) {
        this.alpha_T = alpha_T;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Pompe:: puissance: ");
        sb.append(p_P_min);
        sb.append(" ");
        sb.append(p_P_max);
        sb.append(" cout: ");
        sb.append(c_AP);
        sb.append(" ");
        sb.append(c_PA);
        sb.append("\n");
        sb.append("Turbine:: puissance: ");
        sb.append(p_T_min);
        sb.append(" ");
        sb.append(p_T_max);
        sb.append(" cout: ");
        sb.append(c_AT);
        sb.append(" ");
        sb.append(c_TA);
        sb.append("\n");
        return sb.toString();
    }
}
