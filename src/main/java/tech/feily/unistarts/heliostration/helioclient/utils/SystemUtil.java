package tech.feily.unistarts.heliostration.helioclient.utils;

import java.util.Date;

import tech.feily.unistarts.heliostration.helioclient.model.PbftMsgModel;

public class SystemUtil {

    public static void println(PbftMsgModel msg) {
        println("    - []          | no  | client node @" + msg.getAp().getAddr() + ":" + msg.getAp().getPort()
                 + " $ this is my address, no need to probe.");
    }
    
    public static void printHead() {
        System.out.println("  Time   |      MsgType      | Bc  |                     Details                     ");
        System.out.println("-------------------------------------------------------------------------------------");
    }
    
    public static void printlnIn(PbftMsgModel msg) {
        switch (msg.getMsgType()) {
            case hello :
                println("in  - [hello]     | no  | @rootnode $ reveive my accessKey.");
                println("    -             |     | " + msg.getMeta());   
                break;
            case note :
                println("in  - [note]      | no  | @rootnode $ new node joining, websocket to detect new nodes.");
                break;
            case confirm :
                println("in  - [confirm]   | no  | service node @" + msg.getAp().getAddr() + ":" + msg.getAp().getPort()
                         + " $ WebSocket of new node has been saved.");
                break;
            case detective :
                println("in  - [detective] | no  | service node @" + msg.getAp().getAddr() + ":" + msg.getAp().getPort()
                        + " $ the other side probes my websocket, and I gets its WebSocket.");
                break;
            case update :
                println("in  - [update]    | no  | @rootnode $ node exit, root node update metadata of p2p network.");
                println("    -             |     | " + msg.getMeta());   
                break;
            case service :
                println("in  - [update]    | no  | @rootnode $ new node joining, root node update metadata of p2p network.");
                println("    -             |     | " + msg.getMeta());   
                break;
            case reply :
                println("in  - [reply]     | yes | service node @" + msg.getAp().getAddr() + ":" + msg.getAp().getPort()
                         + " $ block in chain.");
                break;
            default:
                break;
        }
    }

    public static void printlnOut(PbftMsgModel msg) {
        switch (msg.getMsgType()) {
            case hello :
                println("out - [hello]     | no  | @rootnode $ request my accessKey.");  
                break;
            case detective :
                println("out - [detective] | no  | service node @" + msg.getAp().getAddr() + ":" + msg.getAp().getPort()
                        + " $ send detection packet.");
                break;
            case confirm :
                println("out - [confirm]   | no  | service node @" + msg.getAp().getAddr() + ":" + msg.getAp().getPort()
                        + " $ send my WebSocket.");
                break;
            case request :
                println("out - [request]   | yes | @rootnode $ request block to chain."); 
                break;
            default:
                break;
        }
    }
    
    public static void printlnClientCloseOrError(PbftMsgModel msg, String wsUrl) {
        switch (msg.getMsgType()) {
            case close :
                println("    - [close]     | no  | node @" + wsUrl.substring(4) + " $ closed.");
                break;
            case error :
                println("    - [error]     | no  | node @" + wsUrl.substring(4) + " $ occurs error.");
                break;
            case exception :
                println("    - [exception] | no  | node @" + wsUrl.substring(4) + " $ exception.");
                break;
            default:
                break;
        }
    }
    
    @SuppressWarnings("deprecation")
    public static void println(String line) {
        Date date = new Date();
        System.out.println(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " | " + line);
    }
    
}
