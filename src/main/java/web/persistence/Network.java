package web.persistence;

import lombok.Getter;
import lombok.Setter;
import web.network.Edge;
import web.zap.bussiness.metrics.ConnectionMetrics;
import web.zap.bussiness.metrics.MessageMetrics;

import javax.persistence.*;
import java.util.List;

/**
 * Created by deinf.rsoares on 17/06/2016.
 */
@Getter
@Setter
@Entity
public class Network {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String owner;
    private String name;
    private String groupname;

    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL)
    private List<Edge> edges;

    @OneToOne(cascade = CascadeType.ALL)
    private ConnectionMetrics connectionMetrics;

    @OneToOne(cascade = CascadeType.ALL)
    private MessageMetrics messageMetrics;



}


