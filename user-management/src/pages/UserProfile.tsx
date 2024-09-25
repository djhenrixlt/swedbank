import React, { Component, ChangeEvent } from "react";
import { fetchUserProfile, updateUserProfile, deactivateUserAccount, User } from './authService';

type State = {
    currentUser: User | null;
    token: string | null;
    message: string;
};

export default class UserProfile extends Component<{}, State> {
    constructor(props: {}) {
        super(props);
        this.state = {
            currentUser: null,
            token: localStorage.getItem("userToken"), // Assuming token is stored in localStorage
            message: "",
        };

        this.fetchUserProfile = this.fetchUserProfile.bind(this);
        this.updateUserProfile = this.updateUserProfile.bind(this);
        this.deactivateUserAccount = this.deactivateUserAccount.bind(this);
    }

    componentDidMount() {
        // Fetch the user profile when the component is mounted
        this.fetchUserProfile();
    }

    // Fetch user profile
    async fetchUserProfile() {
        try {
            const { token } = this.state;
            if (token) {
                const user = await fetchUserProfile(token); // Correct function usage
                this.setState({ currentUser: user });
            } else {
                this.setState({ message: "No token found" });
            }
        } catch (error) {
            this.setState({ message: "Error fetching profile" });
        }
    }

    // Update user profile
    async updateUserProfile(updatedUser: User) {
        try {
            const { token } = this.state;
            if (token) {
                await updateUserProfile(token, updatedUser); // Correct function usage
                this.setState({ message: "Profile updated successfully" });
                this.fetchUserProfile();
            } else {
                this.setState({ message: "No token found" });
            }
        } catch (error) {
            this.setState({ message: "Error updating profile" });
        }
    }

    // Deactivate user account
    async deactivateUserAccount() {
        try {
            const { token } = this.state;
            if (token) {
                await deactivateUserAccount(token); // Correct function usage
                this.setState({ message: "Account deactivated successfully" });
                localStorage.removeItem("userToken");
                this.setState({ currentUser: null, token: null });
            }
        } catch (error) {
            this.setState({ message: "Error deactivating account" });
        }
    }

    render() {
        const { currentUser, message } = this.state;

        return (
            <div>
                {currentUser ? (
                    <div className="profile-form">
                        <h4>User Profile</h4>
                        <p>{`Name: ${currentUser.name}`}</p>
                        <p>{`Email: ${currentUser.email}`}</p>
                        <button
                            className="badge badge-danger"
                            onClick={this.deactivateUserAccount}
                        >
                            Deactivate Account
                        </button>
                    </div>
                ) : (
                    <div>
                        <p>No profile found, please log in</p>
                    </div>
                )}
                <p>{message}</p>
            </div>
        );
    }
}
