package zap.bussiness;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.list.SetUniqueList;
import util.ConfigProvider;
import web.network.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafa on 18/06/2016.
 */
@Getter
@Setter
public class NetworkResource {
    private Integer id;
    private String owner;
    private String name;
    private String groupname;
    private List<String> nodes = new ArrayList<>();
    private List<Edge> edges =  new ArrayList<>();

    public NetworkResource(Network network){
        if(network != null) {
            this.id = network.getId();
            this.owner = network.getOwner();
            this.name = network.getName();
            this.groupname = network.getGroupname();

                System.out.println(" network.getEdges().:" +  network.getEdges());
            List<String> nodestmp = SetUniqueList.decorate(new ArrayList<String>());
            String[] edgesdb = network.getEdges().split(ConfigProvider.getEdgesSeparator());
            for (String edgedb : edgesdb){
                String[] nodesdb = edgedb.split(ConfigProvider.getNodesSeparator());
                if(nodesdb.length == 2) {
                    nodestmp.add(nodesdb[0]);
                    nodestmp.add(nodesdb[1]);

                    Edge e = new Edge();
                    e.setSource(nodesdb[0]);
                    e.setTarget(nodesdb[1]);
                    this.edges.add(e);
                }
            }
            this.nodes.addAll(nodestmp);
        }

    }

    @Getter
    @Setter
    private class Edge {
        String source;
        String target;
    }

}
