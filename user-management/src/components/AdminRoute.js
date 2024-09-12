import React from 'react';
import { Navigate } from 'react-router-dom';

// Admin route protection
const AdminRoute = ({ component: Component }) => {
    const jwtToken = localStorage.getItem('jwtToken');

    if (!jwtToken) {
        return <Navigate to="/login" />; // Redirect to login if not authenticated
    }

    // Decode token to check for admin role
    const tokenPayload = JSON.parse(atob(jwtToken.split('.')[1]));
    const hasAdminRole = tokenPayload.roles.includes('ROLE_ADMIN');

    return hasAdminRole ? <Component /> : <Navigate to="/" />;
};

export default AdminRoute;
