package com.glocalme.service.dto;

public class UserLogin {

		private boolean authenticated;
		private String userName;
		private String role; // Ideally, should be an array. But, Onewifi user can have only one role when logged in
		private String imie;
		
		public UserLogin(boolean authenticated, String userName, String roleName) {
			this.authenticated = authenticated;
			this.userName = userName;
			this.role = roleName;
		}
		public UserLogin(boolean authenticated, String userName, String roleName, String imie) {
			this.authenticated = authenticated;
			this.userName = userName;
			this.role = roleName;
			this.imie = imie;
		}
		
		public boolean isAuthenticated() {
			return authenticated;
		}

		public void setAuthenticated(boolean authenticated) {
			this.authenticated = authenticated;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getRole() {
			return role;
		}

		public String getImie() {
			return imie;
		}
		
		public void setImie(String imie) {
			this.imie = imie;
		}
}
