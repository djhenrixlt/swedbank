import React from 'react';
import { Navigate } from 'react-router-dom';

const AdminRoute = ({ component: Component }) => {
    const jwtToken = localStorage.getItem('jwtToken');

    if (!jwtToken) {
        // If no token, redirect to login
        return <Navigate to="/login" />;
    }

    // Try decoding the JWT token payload
    try {
        const tokenPayload = JSON.parse(atob(jwtToken.split('.')[1]));

        // Check if the token contains the ROLE_ADMIN role
        const hasAdminRole = tokenPayload.roles && tokenPayload.roles.includes('ROLE_ADMIN');

        if (!hasAdminRole) {
            // If not an admin, redirect to home
            return <Navigate to="/" />;
        }

        // If the user is an admin, return the component
        return <Component />;
    } catch (error) {
        // If token decoding fails, redirect to login
        console.error('Token decoding error:', error);
        return <Navigate to="/login" />;
    }
};

export default AdminRoute;
