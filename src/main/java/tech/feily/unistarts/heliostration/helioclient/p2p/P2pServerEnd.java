package tech.feily.unistarts.heliostration.helioclient.p2p;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import tech.feily.unistarts.heliostration.helioclient.pbft.Pbft;

/**
 * The server program of P2P node.
 * 
 * @author Feily Zhang
 * @version v0.1
 */
public class P2pServerEnd {

    private static Logger log = Logger.getLogger(P2pServerEnd.class);
    
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
                 * Active node minus one after disconnection.
                 */
                if (SocketCache.wss.contains(ws)) {
                    SocketCache.minusAndGet();
                    SocketCache.wss.remove(ws);
                }
                System.out.println(SocketCache.getMetaModel().toString());
            }

            @Override
            public void onMessage(WebSocket ws, String msg) {
                pbft.handle(ws, msg);
            }

            @Override
            public void onError(WebSocket ws, Exception ex) {
                /**
                 * Active node minus one after occuring error.
                 */
                if (SocketCache.wss.contains(ws)) {
                    SocketCache.minusAndGet();
                    SocketCache.wss.remove(ws);
                }
                System.out.println(SocketCache.getMetaModel().toString());
                log.info("Client connection error!");
            }

            @Override
            public void onStart() {
                log.info("Server start successfully!");
            }
            
        };
        socketServer.start();
        log.info("server listen port " + port);
    }
    
    /**
     * The method of sending a message to a node.
     * 
     * @param ws - websocket
     * @param msg - Messages to send.
     */
    public static void sendMsg(WebSocket ws, String msg) {
        log.info("To " + ws.getRemoteSocketAddress().getAddress().toString() + ":"
                + ws.getRemoteSocketAddress().getPort() + " : " + msg.toString());
        ws.send(msg);
    }
    
    /**
     * The method of broadcasting a massage to all nodes.
     * 
     * @param msg - Messages to send.
     */
    public static void broadcasts(String msg) {
        if (SocketCache.wss.size() == 0 || msg == null || msg.equals("")) {
            return;
        }
        log.info("Glad to say broadcast to clients being startted!");
        for (WebSocket ws : SocketCache.wss) {
            sendMsg(ws, msg);
        }
        log.info("Glad to say broadcast to clients being overred!");
    }

}
