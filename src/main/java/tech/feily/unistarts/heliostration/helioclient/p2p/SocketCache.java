package tech.feily.unistarts.heliostration.helioclient.p2p;

import java.util.Set;

import org.java_websocket.WebSocket;

import com.google.common.collect.Sets;

import tech.feily.unistarts.heliostration.helioclient.model.ClientNodeModel;
import tech.feily.unistarts.heliostration.helioclient.model.MetaModel;

/**
 * Cache information for the current node.
 * 
 * @author Feily zhang
 * @version v.01
 */
public class SocketCache {
    
    /**
     * Cache all ws connected to this root node.
     */
    public static Set<WebSocket> wss = Sets.newConcurrentHashSet();
    
    /**
     * Cache network state, initialized according to root node response.
     * 
     * According to this field, judge whether the block is in the chain,
     * that is, whether there is a consensus between the service nodes.
     * 
     * This field is read-only after initialization.
     */
    private static MetaModel metaModel = new MetaModel();
    
    private static ClientNodeModel myself = new ClientNodeModel();
    
    /**
     * @return the myself
     */
    public static ClientNodeModel getMyself() {
        return myself;
    }

    /**
     * @param myself the myself to set
     */
    public static void setMyself(ClientNodeModel myself) {
        SocketCache.myself = myself;
    }

    /**
     * @return the metaModel
     */
    public static MetaModel getMetaModel() {
        return metaModel;
    }

    /**
     * @param metaModel the metaModel to set
     */
    public static void setMetaModel(MetaModel metaModel) {
        SocketCache.metaModel = metaModel;
    }

    /**
     * The following implements atomic operations for all MetaModels.
     */
    public static MetaModel getAndIncre() {
        synchronized (SocketCache.class) {
            MetaModel meta = metaModel;
            metaModel.setIndex(metaModel.getIndex() + 1);
            metaModel.setSize(metaModel.getSize() + 1);
            metaModel.setMaxf((metaModel.getSize() - 1) / 3);
            return meta;
        }
    }
    
    public static MetaModel getAndMinus() {
        synchronized (SocketCache.class) {
            MetaModel meta = metaModel;
            metaModel.setIndex(metaModel.getIndex() - 1);
            metaModel.setSize(metaModel.getSize() - 1);
            metaModel.setMaxf((metaModel.getSize() - 1) / 3);
            return meta;
        }
    }

    
    public static MetaModel increAndGet() {
        synchronized (SocketCache.class) {
            metaModel.setIndex(metaModel.getIndex() + 1);
            metaModel.setSize(metaModel.getSize() + 1);
            metaModel.setMaxf((metaModel.getSize() - 1) / 3);
            return metaModel;
        }
    }
    
    public static MetaModel minusAndGet() {
        synchronized (SocketCache.class) {
            metaModel.setIndex(metaModel.getIndex() - 1);
            metaModel.setSize(metaModel.getSize() - 1);
            metaModel.setMaxf((metaModel.getSize() - 1) / 3);
            return metaModel;
        }
    }
}
