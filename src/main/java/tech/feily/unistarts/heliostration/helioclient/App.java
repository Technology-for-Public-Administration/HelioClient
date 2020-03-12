package tech.feily.unistarts.heliostration.helioclient;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import tech.feily.unistarts.heliostration.helioclient.model.AddrPortModel;
import tech.feily.unistarts.heliostration.helioclient.model.ClientNodeModel;
import tech.feily.unistarts.heliostration.helioclient.model.ContentInfoModel;
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
        TimeUnit.MILLISECONDS.sleep(500);
        P2pClientEnd.connect(pbft, "ws://localhost:7001", new Gson().toJson(msg), msg);
        /**
         * Let the server start before sleeping for 500ms.
         */
        ContentInfoModel cim = new ContentInfoModel();
        cim.setFrom("Feily Zhang");
        cim.setContent("hello, world");
        cim.setTimestamp(System.currentTimeMillis());
        cim.setTo("Pengfei Zhang");
        cim.setType("email");
        TimeUnit.SECONDS.sleep(5);
        PbftContentModel pcm = new PbftContentModel();
        pcm.setTransaction(Arrays.asList(new Gson().toJson(cim)));
        pcm.setDigest(SHAUtil.sha256BasedHutool(pcm.getTransaction().toString()));
        pcm.setAp(ap);
        msg.setPcm(pcm);
        msg.setMsgType(MsgEnum.request);
        P2pClientEnd.connect(pbft, "ws://localhost:7001", new Gson().toJson(msg), msg);

        ContentInfoModel cim1 = new ContentInfoModel();
        cim1.setFrom("Pengfei Zhang");
        cim1.setContent("nihao, shijie");
        cim1.setTimestamp(System.currentTimeMillis());
        cim1.setTo("Feily Zhang");
        cim1.setType("email");
        TimeUnit.SECONDS.sleep(1);
        pcm.setTransaction(Arrays.asList(new Gson().toJson(cim1)));
        pcm.setDigest(SHAUtil.sha256BasedHutool(pcm.getTransaction().toString()));
        pcm.setAp(ap);
        msg.setPcm(pcm);
        msg.setMsgType(MsgEnum.request);
        P2pClientEnd.connect(pbft, "ws://localhost:7001", new Gson().toJson(msg), msg);

        ContentInfoModel cim2 = new ContentInfoModel();
        cim2.setFrom("Pengfei Zhang");
        cim2.setContent("nihao, shijie");
        cim2.setTimestamp(System.currentTimeMillis());
        cim2.setTo("Feily Zhang");
        cim2.setType("email");
        TimeUnit.SECONDS.sleep(1);
        pcm.setTransaction(Arrays.asList(new Gson().toJson(cim2)));
        pcm.setDigest(SHAUtil.sha256BasedHutool(pcm.getTransaction().toString()));
        pcm.setAp(ap);
        msg.setPcm(pcm);
        msg.setMsgType(MsgEnum.request);
        P2pClientEnd.connect(pbft, "ws://localhost:7001", new Gson().toJson(msg), msg);
    }
}
