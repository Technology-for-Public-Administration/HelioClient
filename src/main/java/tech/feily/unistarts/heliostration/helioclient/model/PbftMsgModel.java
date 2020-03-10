package tech.feily.unistarts.heliostration.helioclient.model;

import java.util.List;

/**
 * Message entity class of PBFT algorithm in the P2P network.
 * 
 * @author Feily Zhang
 * @version v0.1
 */
public class PbftMsgModel {

    private MsgEnum msgType;
    private ClientNodeModel client;
    private AddrPortModel ap;
    private MetaModel meta;
    private PbftContentModel pcm;
    private List<AddrPortModel> apm;
    
    /**
     * @return the msgType
     */
    public MsgEnum getMsgType() {
        return msgType;
    }
    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(MsgEnum msgType) {
        this.msgType = msgType;
    }
    
    /**
     * @return the client
     */
    public ClientNodeModel getClient() {
        return client;
    }
    /**
     * @param client the client to set
     */
    public void setClient(ClientNodeModel client) {
        this.client = client;
    }
    
    /**
     * @return the ap
     */
    public AddrPortModel getAp() {
        return ap;
    }
    /**
     * @param ap the ap to set
     */
    public void setAp(AddrPortModel ap) {
        this.ap = ap;
    }

    /**
     * @return the meta
     */
    public MetaModel getMeta() {
        return meta;
    }
    /**
     * @param meta the meta to set
     */
    public void setMeta(MetaModel meta) {
        this.meta = meta;
    }
    
    /**
     * @return the pcm
     */
    public PbftContentModel getPcm() {
        return pcm;
    }
    /**
     * @param pcm the pcm to set
     */
    public void setPcm(PbftContentModel pcm) {
        this.pcm = pcm;
    }
    
    
    /**
     * @return the apm
     */
    public List<AddrPortModel> getApm() {
        return apm;
    }
    /**
     * @param apm the apm to set
     */
    public void setApm(List<AddrPortModel> apm) {
        this.apm = apm;
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[PbftMsg] msgType = " + msgType.toString() + ", client = " + client.toString()
                + ", ap = " + ap.toString() + ", meta = " + meta.toString()
                + ", pcm = " + pcm.toString() + ", apm = " + apm.toString());
        return str.toString();
    }
    
}
