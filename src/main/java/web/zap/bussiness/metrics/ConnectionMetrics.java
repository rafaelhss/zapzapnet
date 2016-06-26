package web.zap.bussiness.metrics;

import lombok.Getter;
import lombok.Setter;
import util.HashMapUtil;
import web.network.Edge;
import web.zap.bussiness.Connection;
import web.zap.bussiness.generators.EdgesListGenerator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rafa on 25/06/2016.
 */
@Getter
@Setter
@Entity
public class ConnectionMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String topSender;
    private String topTarget;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection
    private List<Edge> mainEdges;

    public ConnectionMetrics() {
    }
        public ConnectionMetrics(List<Connection> connections){
        HashMap<String, Integer> edges = new HashMap<>();
        HashMap<String, Integer> top = new HashMap<>();
            HashMap<String, Integer> topSender = new HashMap<>();
            HashMap<String, Integer> topTarget = new HashMap<>();


            this.mainEdges  = new EdgesListGenerator()
                    .generate(connections).stream()
                    .sorted(Comparator.comparing(Edge::getSize).reversed())
                    .limit(3)
                    .collect(Collectors.toList());

            for(Connection c:connections){
                HashMapUtil.incrementCount(topSender, c.getSender());
                HashMapUtil.incrementCount(topTarget, c.getReceiver());
            }

        this.setTopSender(HashMapUtil.findMax(topSender));
        this.setTopTarget(HashMapUtil.findMax(topTarget));
    }





}
