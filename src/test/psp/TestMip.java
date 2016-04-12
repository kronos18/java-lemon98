package test.psp;

import ilog.concert.IloException;
import psp.Instance;
import psp.Mip;
import psp.Parser;

import java.io.File;
import java.io.IOException;

public class TestMip {
    public static void main(String[] args) throws IOException, IloException {
        Instance instance = Parser
                .lireInstance("Data" + File.separator + "Instances" + File.separator + "instance0.txt",
                              4);
        Mip mip = new Mip(instance);
        mip.solve();
    }
}
