package com.dinogroup.repository;

import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;

/**
 * Repository storing current business and table ID
 */
public class PresenterSharedRepository {
	private String currentBuzID;
	private String currentTableID;

	public PresenterSharedRepository() {
		this.currentBuzID = "";
		this.currentTableID = "";
	}

	public String getCurrentBuzID() {
		return currentBuzID;
	}

	public void setCurrentBuzID(String currentBuzID) {
		this.currentBuzID = currentBuzID;
	}

	public String getCurrentTableID() {
		return currentTableID;
	}

	public void setCurrentTableID(String currentTableID) {
		this.currentTableID = currentTableID;
	}
}
