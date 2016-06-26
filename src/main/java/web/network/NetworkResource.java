package web.network;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.list.SetUniqueList;
import util.ConfigProvider;
import web.persistence.Network;
import web.zap.bussiness.metrics.ConnectionMetrics;
import web.zap.bussiness.metrics.MessageMetrics;

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
    private ConnectionMetrics connectionMetrics;

    private MessageMetrics messageMetrics;
    private List<String> nodes = new ArrayList<>();
    private List<Edge> edges =  new ArrayList<>();

    public NetworkResource(Network network){
        if(network != null) {
            this.id = network.getId();
            this.owner = network.getOwner();
            this.name = network.getName();
            this.groupname = network.getGroupname();
            this.connectionMetrics = network.getConnectionMetrics();
            this.messageMetrics = network.getMessageMetrics();


            this.edges = network.getEdges();

            List<String> nodestmp = SetUniqueList.decorate(new ArrayList<String>());

            this.edges.stream().forEach(edge -> {nodestmp.add(edge.getSource()); nodestmp.add(edge.getTarget());});

            this.nodes.addAll(nodestmp);
        }

    }
}
