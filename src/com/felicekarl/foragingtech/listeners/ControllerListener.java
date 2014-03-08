package com.felicekarl.foragingtech.listeners;

public interface ControllerListener {
	public void setSpeedX(int speedX);
	public void setSpeedY(int speedY);
	public void setSpeedZ(int speedZ);
	public void setSpeedSpin(int speedSpin);
	
	public void takeOff();
	public void landing();
}
