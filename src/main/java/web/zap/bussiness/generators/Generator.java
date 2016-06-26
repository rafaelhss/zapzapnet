package web.zap.bussiness.generators;

import web.zap.bussiness.Connection;

import java.util.List;

/**
 * Created by deinf.rsoares on 17/06/2016.
 */
public interface Generator {
    public StringBuilder generate(List<Connection> connections);
}
