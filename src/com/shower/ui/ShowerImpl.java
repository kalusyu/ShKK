package com.shower.ui;


public class ShowerImpl implements IShower{

	MainActivity activity;
	
	public ShowerImpl(MainActivity mainActivity) {
		activity = mainActivity;
	}

	@Override
	public void addTemplatuer(int templature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reduceTemplatuer(int templature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFlow(int flow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reduceFlow(int flow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTemplature(int templature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFlow(int flow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dingpengOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dingpengOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cepengOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cepengOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pubuOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pubuOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shouchiOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shouchiOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startShower() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopShower() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTempalture() {
		return 22;
	}

	@Override
	public int getFlow() {
		// TODO Auto-generated method stub
		return 1;
	}

	
	@Override
	public void setShowerButtonState(int status) {
		activity.setShowerButtonState(status);
	}

	@Override
	public int getShowerFlow() {
		// TODO Auto-generated method stub
		return  (int)(Math.random() * 100);//test
	}

	@Override
	public int getShowerTime() {
		// TODO Auto-generated method stub
		return (int)(Math.random() * 1000);//test
	}

	@Override
	public boolean setBulbColor(String color) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addBulbLight(int light) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reduceBulbLight(int light) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBulbLight(int light) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getDefaultColor() {
		return activity.getDefaultColor();
	}

}
