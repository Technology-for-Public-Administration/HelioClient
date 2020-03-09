package tech.feily.unistarts.heliostration.helioclient.p2p;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import tech.feily.unistarts.heliostration.helioclient.model.PbftMsgModel;
import tech.feily.unistarts.heliostration.helioclient.pbft.Pbft;
import tech.feily.unistarts.heliostration.helioclient.utils.SystemUtil;

/**
 * The server program of P2P node.
 * 
 * @author Feily Zhang
 * @version v0.1
 */
public class P2pServerEnd {

    //private static Logger log = Logger.getLogger(P2pServerEnd.class);
    
    /**
     * The method of starting node service in P2P network(as server).
     * 
     * @param pbft
     * @param port
     */
    public static void run(final Pbft pbft, int port) {
        final WebSocketServer socketServer = new WebSocketServer(new InetSocketAddress(port)) {

            @Override
            public void onOpen(WebSocket ws, ClientHandshake handshake) {
                /**
                 * Because all active nodes in the p2p network acquire the ws to this node,
                 * If these active nodes are cached,
                 * their every move will affect the meta value to determine whether the block is in the chain.
                 */
                SocketCache.wss.add(ws);
            }

            @Override
            public void onClose(WebSocket ws, int code, String reason, boolean remote) {
                /**
                 * In order to preventing metadata of the P2P network from decreasing repeatly, nothing to do here.
                 */
            }

            @Override
            public void onMessage(WebSocket ws, String msg) {
                pbft.handle(ws, msg);
            }

            @Override
            public void onError(WebSocket ws, Exception ex) {
                /**
                 * In order to preventing metadata of the P2P network from decreasing repeatly, nothing to do here.
                 */
            }

            @Override
            public void onStart() {
                System.out.println("Client start successfully!");
                System.out.println("------------------------------------------------------------------------------------");
                SystemUtil.printHead();
            }
            
        };
        socketServer.start();
        System.out.println("Client node starting...");
        System.out.println("server listen port " + port);
    }
    
    /**
     * The method of sending a message to a node.
     * 
     * @param ws - websocket
     * @param msg - Messages to send.
     */
    public static void sendMsg(WebSocket ws, String msg, PbftMsgModel pm) {
        ws.send(msg);
        SystemUtil.printlnOut(pm);
    }
    
    /**
     * The method of broadcasting a massage to all nodes.
     * 
     * @param msg - Messages to send.
     */
    public static void broadcasts(String msg, PbftMsgModel pm) {
        if (SocketCache.wss.size() == 0 || msg == null || msg.equals("")) {
            return;
        }
        for (WebSocket ws : SocketCache.wss) {
            sendMsg(ws, msg, pm);
        }
    }

}
