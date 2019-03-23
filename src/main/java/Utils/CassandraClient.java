package Utils;

import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class CassandraClient {
    private Session session;
    private Integer nrSec = 5;
    private Cluster cluster;

    private InactiveAction inactive;

    private String node;
    public CassandraClient(String node){
        connect(node);
        inactive = new InactiveAction(nrSec,cluster);
        this.node = node;
        inactive.start();
    }

    public void connect(String node) {

            cluster = Cluster.builder().addContactPoint(node).build();
            Metadata metadata = cluster.getMetadata();
            System.out.println("Cassandra connection established");
            System.out.printf("Connected to cluster: %s\n",
                    metadata.getClusterName());
            for (Host host : metadata.getAllHosts()) {
                System.out.printf("Datatacenter: %s; Host: %s; Rack: %s \n",
                        host.getDatacenter(), host.getAddress(), host.getRack());
                session = cluster.connect();

            }


    }

//    public void createSchema() {
//
//        session.execute("CREATE TABLE if not exists mapappdb.studenti ("
//                + "stid int PRIMARY KEY,"
//                + "nume text,"
//                + "grupa text,"
//                + "email text"
//                + ");");
//        session.execute("CREATE TABLE if not exists mapappdb.laburi (" + "id int PRIMARY KEY,"
//                + "descriere text," + "deadline int, " + "start int,"
//                + ");");
//        session.execute("CREATE TABLE if not exists mapappdb.laburi (" + "id int PRIMARY KEY,"
//                + "descriere text," + "deadline int, " + "start int,"
//                + ");");
//
//    }


    public ResultSet exec(String s){

        if(cluster.isClosed()){
            connect(node);
            inactive = new InactiveAction(nrSec,cluster);
            inactive.start();
        }
        inactive.SignalActive();
        return session.execute(s);
    }

    public void close() {
        cluster.close();
    }
    public Session getSession(){
        return session;
    }


}
