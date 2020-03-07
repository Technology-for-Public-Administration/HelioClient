package tech.feily.unistarts.heliostration.helioclient;

import com.google.gson.Gson;

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
        String file = "C:\\Users\\fei47\\Desktop\\mapper.txt";
        int port = 7003;
        Pbft pbft = new Pbft(file, port);
        ClientNodeModel cli = new ClientNodeModel();
        cli.setClientId("159852");
        cli.setClientKey("456123");
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.hello);
        msg.setClient(cli);
        P2pClientEnd.connect(pbft, "ws://localhost:7001", new Gson().toJson(msg), file, port, true);
        P2pServerEnd.run(pbft, port);
    }
}
