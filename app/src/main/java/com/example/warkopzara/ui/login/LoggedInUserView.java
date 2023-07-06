package com.example.warkopzara.ui.login;

import com.example.warkopzara.data.model.LoggedInUser;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private LoggedInUser loggedInUser;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    String getDisplayName() {
        return loggedInUser.getDisplayName();
    }
    String getToken() {
        return loggedInUser.getDisplayName();
    }
}