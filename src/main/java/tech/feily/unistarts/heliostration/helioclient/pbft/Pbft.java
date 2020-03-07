package tech.feily.unistarts.heliostration.helioclient.pbft;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;

import com.google.gson.Gson;

import tech.feily.unistarts.heliostration.helioclient.model.FileReaderModel;
import tech.feily.unistarts.heliostration.helioclient.model.MsgEnum;
import tech.feily.unistarts.heliostration.helioclient.model.PbftMsgModel;
import tech.feily.unistarts.heliostration.helioclient.p2p.P2pClientEnd;
import tech.feily.unistarts.heliostration.helioclient.p2p.SocketCache;
import tech.feily.unistarts.heliostration.helioclient.utils.FileUtil;

public class Pbft {

    private Logger log = Logger.getLogger(Pbft.class);
    private Gson gson = new Gson();
    private String file;
    private int port;
    
    /**
     * The Pbft of the service node does not need to be initialized,
     * because the necessary cache information needs to be.
     */
    public Pbft(String file, int port) {
        /**
         * Nothing to do.
         */
        this.file = file;
        this.port = port;
    }
    
    public void handle(WebSocket ws, String msg) {
        log.info("From " + ws.getRemoteSocketAddress().getAddress().toString() + ":"
                + ws.getRemoteSocketAddress().getPort() + ", message is " + msg);
        PbftMsgModel msgs = gson.fromJson(msg, PbftMsgModel.class);
        switch (msgs.getMsgType()) {
            case hello :
                onHello(ws, msgs);
                break;
            case detective :
                onDetective(ws, msgs);
                break;
            default :
                break;
        }
    }

    /**
     * Processing detective request from service node.
     * 
     * @param ws
     * @param msgs
     */
    private void onDetective(WebSocket ws, PbftMsgModel msgs) {
        /**
         * Just return the confirm message directly to the other party.
         */
        System.out.println("helioclient");
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.confirm);
        FileReaderModel fm = FileUtil.openForR(file);
        Integer pt = msgs.getAp().getPort();
        String pot = FileUtil.selectByPort(fm, pt.toString());
        System.out.println("3333");
        Integer por = Integer.valueOf(pot);
        System.out.println("22222");
        if (por == port) {
            return;
        }
        System.out.println("11111");
        P2pClientEnd.connect(this, "ws:/" + ws.getRemoteSocketAddress().getAddress()
                + ":" + por.toString(), gson.toJson(msg), file, port, false);
        FileUtil.closeForR(fm);
    }

    /**
     * Handle the hello message from root node, and get own key.
     * 
     * @param ws
     * @param msgs
     */
    private void onHello(WebSocket ws, PbftMsgModel msgs) {
        /**
         * We need to determine whether the message comes from the root node. We will omit it for now.
         */
        /**
         * Get own key so that you can send a request to root node.
         */
        SocketCache.setMyself(msgs.getClient());
    }
    
}
