package com.chein.crispcut;

public interface ActionResolver {
	public boolean getSignedInGPGS();
	public void loginGPGS();
	public void submitScoreGPGS(int score);
	public void unlockAchievementGPGS(String achivementID);
	public void getLeaderboardGPGS();
	public void getAchievementsGPGS();

}
