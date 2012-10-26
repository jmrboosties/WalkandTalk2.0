package com.jesse.game.utils;

import java.util.Timer;

import com.jesse.game.listeners.OnTimerCancelledListener;

public class TimerCancelable extends Timer {

	private OnTimerCancelledListener mListener;
	private boolean mCancelled = false;
	
	public void setOnTimerCancelledListener(OnTimerCancelledListener listener) {
		mListener = listener;
	}
	
	@Override
	public void cancel() {
		super.cancel();
		mListener.onTimerCancelled();
		mCancelled = true;
	}
	
	public boolean isCancelled() {
		return mCancelled;
	}
	
}
