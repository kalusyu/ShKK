package com.shower;

public interface IShower {

	
	void addTemplatuer(int templature);
	
	void reduceTemplatuer(int templature);
	
	void addFlow(int flow);
	
	void reduceFlow(int flow);
	
	void setTemplature(int templature);
	
	int getTempalture();
	
	void setFlow(int flow);
	
	/**
	 * 流量以100分计算 33 代表一格
	 * TODO
	 * @return
	 * 2014年11月17日 下午9:07:06
	 */
	int getFlow();
	
	void dingpengOn();
	void dingpengOff();
	
	void cepengOn();
	void cepengOff();
	
	void pubuOn();
	void pubuOff();
	
	void shouchiOn();
	void shouchiOff();
	
	void startShower();
	void stopShower();
	
	/**
	 * 设置中间按钮状态
	 * @param status (101-on）,（100 -off） ,（102 -fail）
	 * 
	 */
	void setShowerButtonState(int status);
	
	int getShowerFlow();
	
	int getShowerTime();
	
	boolean setBulbColor(String color);
	
	void addBulbLight(int light);
	void reduceBulbLight(int light);
	void setBulbLight(int light);
	
}
