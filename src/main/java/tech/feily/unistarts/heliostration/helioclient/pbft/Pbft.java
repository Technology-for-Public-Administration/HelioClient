package tech.feily.unistarts.heliostration.helioclient.pbft;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;

import com.google.gson.Gson;

import tech.feily.unistarts.heliostration.helioclient.model.AddrPortModel;
import tech.feily.unistarts.heliostration.helioclient.model.MsgEnum;
import tech.feily.unistarts.heliostration.helioclient.model.PbftMsgModel;
import tech.feily.unistarts.heliostration.helioclient.p2p.P2pClientEnd;
import tech.feily.unistarts.heliostration.helioclient.p2p.SocketCache;

public class Pbft {

    private Logger log = Logger.getLogger(Pbft.class);
    private Gson gson = new Gson();
    private AddrPortModel ap;
    
    /**
     * The Pbft of the service node does not need to be initialized,
     * because the necessary cache information needs to be.
     */
    public Pbft(AddrPortModel ap) {
        /**
         * Nothing to do.
         */
        this.ap = ap;
    }
    
    public void handle(WebSocket ws, String msg) {
        log.info("From " + ws.getRemoteSocketAddress().getAddress().toString() + ":"
                + ws.getRemoteSocketAddress().getPort() + ", message is " + msg);
        PbftMsgModel msgs = gson.fromJson(msg, PbftMsgModel.class);
        switch (msgs.getMsgType()) {
            case hello :
                onHello(ws, msgs);
                break;
            case note :
                onNote(ws, msgs);
                break;
            case detective :
                onDetective(ws, msgs);
                break;
            case confirm :
                onConfirm(ws, msgs);
            case update :
                onUpdate(ws, msgs);
                break;
            default :
                break;
        }
    }

    private void onUpdate(WebSocket ws, PbftMsgModel msgs) {
        /**
         * We should first verify whether the detective message comes from the root node, and temporarily omit it.
         */
        SocketCache.setMetaModel(msgs.getMeta());
        System.out.println("meta缓存已更新" + SocketCache.getMetaModel().toString());
    }

    /**
     * Processing of node message.
     * 
     * @param ws
     * @param msgs
     */
    private void onConfirm(WebSocket ws, PbftMsgModel msgs) {
        /**
         * Nothing to do, because we just want to acquire ws of client.
         * When the client requests this node through the p2pclientend class, we have obtained the WS of the client.
         */
        System.out.println("Current P2P network metadata: " + SocketCache.getMetaModel().toString());
    }
    
    private void onNote(WebSocket ws, PbftMsgModel msgs) {
        if (msgs.getAp().getAddr().equals(ws.getLocalSocketAddress().getAddress().toString())
                && msgs.getAp().getPort() == ap.getPort()) {
            return;
        }
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.detective);
        AddrPortModel ap = new AddrPortModel();
        msg.setAp(ap);
        P2pClientEnd.connect(this, "ws:/" + msgs.getAp().getAddr() + ":" + msgs.getAp().getPort(), gson.toJson(msg));
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
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.confirm);
        msg.setAp(ap);
        P2pClientEnd.connect(this, "ws:/" + msgs.getAp().getAddr() + ":" + msgs.getAp().getPort(), gson.toJson(msg));
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
        SocketCache.setMetaModel(msgs.getMeta());
    }
    
}
