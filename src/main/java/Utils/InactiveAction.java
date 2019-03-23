package Utils;

import com.datastax.driver.core.Cluster;

public class InactiveAction extends Thread {
    private int nrSecInactiv;
    private int nrMaxSec;
    private Cluster cl;
    public InactiveAction(int nrSec,Cluster cl){
        nrSecInactiv = 0;
        nrMaxSec = nrSec;
        this.cl = cl;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                nrSecInactiv++;
                if (nrSecInactiv == nrMaxSec) {
                    nrSecInactiv = 0;
                    cl.close();
                    return;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void restart(Cluster cl){
        this.cl = cl;
        this.run();
    }
    public void SignalActive(){
        nrSecInactiv = 0;
    }

}
