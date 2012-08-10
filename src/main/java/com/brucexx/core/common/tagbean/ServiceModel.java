package com.brucexx.core.common.tagbean;

public class ServiceModel {

	private String service;
	
	private String protocol;
	
	private String _interface;
	
    private String ref;

	/**
     * Getter method for property <tt>ref</tt>.
     * 
     * @return property value of ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * Setter method for property <tt>ref</tt>.
     * 
     * @param ref value to be assigned to property ref
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
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
}
