package tech.feily.unistarts.heliostration.helioclient;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import tech.feily.unistarts.heliostration.helioclient.model.AddrPortModel;
import tech.feily.unistarts.heliostration.helioclient.model.ClientNodeModel;
import tech.feily.unistarts.heliostration.helioclient.model.MsgEnum;
import tech.feily.unistarts.heliostration.helioclient.model.PbftContentModel;
import tech.feily.unistarts.heliostration.helioclient.model.PbftMsgModel;
import tech.feily.unistarts.heliostration.helioclient.p2p.P2pClientEnd;
import tech.feily.unistarts.heliostration.helioclient.p2p.P2pServerEnd;
import tech.feily.unistarts.heliostration.helioclient.pbft.Pbft;
import tech.feily.unistarts.heliostration.helioclient.utils.SHAUtil;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main( String[] args ) throws InterruptedException {
        int port = 7003;
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
        Pbft pbft = new Pbft(ap);
        P2pServerEnd.run(pbft, port);
        /**
         * Let the server start before sleeping for 500ms.
         */
        TimeUnit.MILLISECONDS.sleep(500);
        P2pClientEnd.connect(pbft, "ws://localhost:7001", new Gson().toJson(msg), msg);
        TimeUnit.SECONDS.sleep(3);
        PbftContentModel pcm = new PbftContentModel();
        pcm.setTransaction(Arrays.asList("hello, world"));
        pcm.setDigest(SHAUtil.sha256BasedHutool(pcm.getTransaction().toString()));
        msg.setPcm(pcm);
        pcm.setAp(ap);
        msg.setMsgType(MsgEnum.request);
        P2pClientEnd.connect(pbft, "ws://localhost:7001", new Gson().toJson(msg), msg);
    }
}
