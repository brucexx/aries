package com.brucexx.aries.tag;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ReferenceModel {

    private String id;

    private String protocol;

    private String _interface;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String get_interface() {
        return _interface;
    }

    public void setInterface(String _interface) {
        this._interface = _interface;
    }

    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
