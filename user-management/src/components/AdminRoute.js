import React from 'react';
import { Route, Navigate } from 'react-router-dom';

const AdminRoute = ({ component: Component, ...rest }) => {
    const isAuthenticated = !!localStorage.getItem('jwtToken');
    const isAdmin = localStorage.getItem('role') === 'admin'; // Replace with your actual admin check

    return isAuthenticated && isAdmin ? <Component {...rest} /> : <Navigate to="/login" />;
};

export default AdminRoute;
