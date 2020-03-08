package tech.feily.unistarts.heliostration.helioclient;

import com.google.gson.Gson;

import tech.feily.unistarts.heliostration.helioclient.model.AddrPortModel;
import tech.feily.unistarts.heliostration.helioclient.model.ClientNodeModel;
import tech.feily.unistarts.heliostration.helioclient.model.MsgEnum;
import tech.feily.unistarts.heliostration.helioclient.model.PbftMsgModel;
import tech.feily.unistarts.heliostration.helioclient.p2p.P2pClientEnd;
import tech.feily.unistarts.heliostration.helioclient.p2p.P2pServerEnd;
import tech.feily.unistarts.heliostration.helioclient.pbft.Pbft;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main( String[] args ) {
        int port = 7003;
        Pbft pbft = new Pbft(port);
        ClientNodeModel cli = new ClientNodeModel();
        cli.setClientId("159852");
        cli.setClientKey("456123");
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.hello);
        msg.setClient(cli);
        AddrPortModel ap = new AddrPortModel();
        ap.setAddr("/127.0.0.1");
        ap.setPort(port);
        msg.setAp(ap);
        P2pClientEnd.connect(pbft, "ws://localhost:7001", new Gson().toJson(msg), port);
        P2pServerEnd.run(pbft, port);
    }
}
