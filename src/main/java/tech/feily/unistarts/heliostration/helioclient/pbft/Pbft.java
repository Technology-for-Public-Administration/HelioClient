package tech.feily.unistarts.heliostration.helioclient.pbft;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;

import tech.feily.unistarts.heliostration.helioclient.model.AddrPortModel;
import tech.feily.unistarts.heliostration.helioclient.model.MsgEnum;
import tech.feily.unistarts.heliostration.helioclient.model.PbftMsgModel;
import tech.feily.unistarts.heliostration.helioclient.p2p.P2pClientEnd;
import tech.feily.unistarts.heliostration.helioclient.p2p.P2pServerEnd;
import tech.feily.unistarts.heliostration.helioclient.p2p.SocketCache;
import tech.feily.unistarts.heliostration.helioclient.utils.SystemUtil;

public class Pbft {

    //private Logger log = Logger.getLogger(Pbft.class);
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
        //log.info("From " + ws.getRemoteSocketAddress().getAddress().toString() + ":"
                //+ ws.getRemoteSocketAddress().getPort() + ", message is " + msg);
        PbftMsgModel msgs = gson.fromJson(msg, PbftMsgModel.class);
        SystemUtil.printlnIn(msgs);
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
                break;
            case update :
            case service :
                onUpdate(ws, msgs);
                break;
            case prePrepare :
            case prepare :
            case commit :
                onPreCom(ws, msgs);
                break;
            case reply :
                onReply(ws, msgs);
                break;
            default :
                break;
        }
    }

    private void onPreCom(WebSocket ws, PbftMsgModel msgs) {
        /**
         * Nothing to do.
         */
    }

    private void onReply(WebSocket ws, PbftMsgModel msgs) {
        /**
         * Nothing to do.
         */
    }

    private void onUpdate(WebSocket ws, PbftMsgModel msgs) {
        /**
         * We should first verify whether the detective message comes from the root node, and temporarily omit it.
         */
        SocketCache.setMetaModel(msgs.getMeta());
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
    }
    
    private void onNote(WebSocket ws, PbftMsgModel msgs) {/*
        if (msgs.getAp().getAddr().equals(ws.getLocalSocketAddress().getAddress().toString())
                && msgs.getAp().getPort() == ap.getPort()) {
            SystemUtil.println(msgs);
            return;
        }
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.detective);
        msg.setAp(ap);
        msgs.setMsgType(MsgEnum.detective);
        P2pClientEnd.connect(this, "ws:/" + msgs.getAp().getAddr() + ":" + msgs.getAp().getPort(), gson.toJson(msg), msgs);*/
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.detective);
        msg.setAp(ap);  // response me.
        if (msgs.getApm() != null) {
            for (AddrPortModel apm : msgs.getApm()) {
                if (apm.getAddr().equals(ap.getAddr()) && apm.getPort() == ap.getPort()) {
                    continue;
                } else {
                    PbftMsgModel prt = new PbftMsgModel();
                    prt.setMsgType(MsgEnum.detective);
                    // to who.
                    prt.setAp(apm);
                    P2pClientEnd.connect(this, "ws:/" + apm.getAddr() + ":" + apm.getPort(), gson.toJson(msg), prt);
                }
            }
        } else {
            PbftMsgModel prt = new PbftMsgModel();
            prt.setMsgType(MsgEnum.detective);
            // to who.
            prt.setAp(msgs.getAp());
            P2pClientEnd.connect(this, "ws:/" + msgs.getAp().getAddr() + ":" + msgs.getAp().getPort(), gson.toJson(msg), prt);
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
        PbftMsgModel msg = new PbftMsgModel();
        msg.setMsgType(MsgEnum.confirm);
        msg.setAp(ap);
        msgs.setMsgType(MsgEnum.confirm);
        //P2pClientEnd.connect(this, "ws:/" + msgs.getAp().getAddr() + ":" + msgs.getAp().getPort(), gson.toJson(msg), msgs);
        P2pServerEnd.sendMsg(ws, gson.toJson(msg), msgs);
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
