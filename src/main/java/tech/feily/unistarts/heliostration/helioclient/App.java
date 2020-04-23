package tech.feily.unistarts.heliostration.helioclient;

import java.util.Arrays;
import java.util.Scanner;
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
import tech.feily.unistarts.heliostration.helioclient.utils.PreCmd;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main( String[] args ) throws InterruptedException {
        System.out.println("Welcome to the HelioChain platform(Client Node).");
        System.out.println("Current application version : Alpha 0.1.0.0423");
        System.out.println("This application is licensed through GNU General Public License version 3 (GPLv3).");
        System.out.println("Copyright \u00A92020 tpastd.com. All rights reserved.\n");
        System.out.println("First, you need to add some configuration information to use.");
        System.out.println("------------------------------------------------------------------");
        PreCmd.run();
        ClientNodeModel cli = new ClientNodeModel();
        cli.setClientId(PreCmd.getParam().get("clientId"));
        cli.setClientKey(PreCmd.getParam().get("clientKey"));
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.hello);
        msg.setClient(cli);
        AddrPortModel ap = new AddrPortModel();
        ap.setAddr("/" + PreCmd.getParam().get("curtAddr"));
        ap.setPort(Integer.parseInt(PreCmd.getParam().get("curtPort")));
        msg.setAp(ap);
        Pbft pbft = new Pbft(ap);
        P2pServerEnd.run(pbft, Integer.parseInt(PreCmd.getParam().get("curtPort")));
        P2pClientEnd.connect(pbft, "ws://" + PreCmd.getParam().get("rootAddr") + ":" + PreCmd.getParam().get("rootPort"), new Gson().toJson(msg), msg);
        /**
         * Let the server start before sleeping for 500ms.
         */
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("---------------------------Let's start !--------------------------------");
        while (true) {
            ContentInfoModel cim = new ContentInfoModel();
            System.out.print("To: ");
            cim.setTo(scan.nextLine());
            System.out.print("Type: ");
            cim.setType(scan.nextLine());
            System.out.print("Content: ");
            cim.setContent(scan.nextLine());
            System.out.print("From: ");
            cim.setFrom(scan.nextLine());
            cim.setTimestamp(System.currentTimeMillis());
            PbftContentModel pcm = new PbftContentModel();
            pcm.setTransaction(Arrays.asList(new Gson().toJson(cim)));
            pcm.setDigest(SHAUtil.sha256BasedHutool(pcm.getTransaction().toString()));
            pcm.setAp(ap);
            msg.setPcm(pcm);
            msg.setMsgType(MsgEnum.request);
            //System.out.println(msgin.toString());
            P2pClientEnd.connect(pbft, "ws://" + PreCmd.getParam().get("rootAddr") + ":" + PreCmd.getParam().get("rootPort"), new Gson().toJson(msg), msg);
            System.out.println("Notice: Please wait a moment, the transaction is in progress...\n");
            TimeUnit.SECONDS.sleep(5);
        }
    }
    
}
