import React from 'react';
import {Navigate} from 'react-router-dom';

const AdminRoute = ({component: Component}) => {
    const jwtToken = localStorage.getItem('jwtToken');

    if (!jwtToken) {
        // No token found, redirect to login
        console.log("No token found, redirecting to login");
        return <Navigate to="/login"/>;
    }

    try {
        // Decode the token payload
        const tokenPayload = JSON.parse(atob(jwtToken.split('.')[1]));
        console.log("Decoded token payload:", tokenPayload);

        // Check if the user has the ROLE_ADMIN role
        if (tokenPayload && tokenPayload.roles && Array.isArray(tokenPayload.roles)) {
            const hasAdminRole = tokenPayload.roles.includes('ROLE_ADMIN');

            if (!hasAdminRole) {
                // If user is not an admin, redirect to home
                console.log("User does not have admin role, redirecting to home");
                return <Navigate to="/"/>;
            }

            // User has admin role, allow access to the admin component
            return <Component/>;
        } else {
            console.log("Token does not contain roles or roles are incorrectly formatted");
            return <Navigate to="/"/>;
        }
    } catch (error) {
        console.error("Token decoding error:", error);
        return <Navigate to="/login"/>;
    }
};

export default AdminRoute;
