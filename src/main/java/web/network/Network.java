package web.network;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    @Lob
    @Column( length = 1000000 )
    private String sigmagraph;

    private class NetworkId {

    }

}


